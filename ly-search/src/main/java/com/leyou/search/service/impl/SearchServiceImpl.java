package com.leyou.search.service.impl;
import com.leyou.common.dto.PageDTO;
import com.leyou.common.exception.LyException;
import com.leyou.item.client.ItemClient;
import com.leyou.item.dto.*;
import com.leyou.search.dto.SearchParamDTO;
import com.leyou.search.entity.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import com.leyou.starter.elastic.dto.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import javax.annotation.Resource;
import javax.naming.directory.SearchResult;
import java.util.*;
import java.util.stream.Collectors;
import static com.leyou.search.constants.SearchConstants.*;
/**
 * @author 虎哥
 */
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    @Resource
    private RestHighLevelClient client;


    @Resource
   private GoodsRepository goodsRepository;

    @Resource
    private ItemClient itemClient;  // 直接赋值 构造器赋值 非静态代码块赋值

 /*   private final GoodsRepository goodsRepository;

    private final ItemClient itemClient;

    public SearchServiceImpl(@Qualifier GoodsRepository goodsRepository, ItemClient itemClient) {
        this.goodsRepository = goodsRepository;
        this.itemClient = itemClient;
    }*/
    @Override
    public void createIndexAndMapping() {
        // 删除已经存在的索引库
        try {
            goodsRepository.deleteIndex();     //动态多态对象调用接口方法
        } catch (Exception e) {
            log.error("删除失败，可能索引库不存在！", e);
        }
        // 然后创建一个新的
        goodsRepository.createIndex("{\n" +
                "  \"settings\": {\n" +
                "    \"analysis\": {\n" +
                "      \"analyzer\": {\n" +
                "        \"my_pinyin\": {\n" +
                "          \"tokenizer\": \"ik_smart\",\n" +
                "          \"filter\": [\n" +
                "            \"py\"\n" +
                "          ]\n" +
                "        }\n" +
                "      },\n" +
                "      \"filter\": {\n" +
                "        \"py\": {\n" +
                "\t\t  \"type\": \"pinyin\",\n" +
                "          \"keep_full_pinyin\": true,\n" +
                "          \"keep_joined_full_pinyin\": true,\n" +
                "          \"keep_original\": true,\n" +
                "          \"limit_first_letter_length\": 16,\n" +
                "          \"remove_duplicated_term\": true\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"mappings\": {\n" +
                "    \"properties\": {\n" +
                "      \"id\": {\n" +
                "        \"type\": \"keyword\"\n" +
                "      },\n" +
                "      \"suggestion\": {\n" +
                "        \"type\": \"completion\",\n" +
                "        \"analyzer\": \"my_pinyin\",\n" +
                "        \"search_analyzer\": \"ik_smart\"\n" +
                "      },\n" +
                "      \"title\":{\n" +
                "        \"type\": \"text\",\n" +
                "        \"analyzer\": \"my_pinyin\",\n" +
                "        \"search_analyzer\": \"ik_smart\"\n" +
                "      },\n" +
                "      \"image\":{\n" +
                "        \"type\": \"keyword\",\n" +
                "        \"index\": false\n" +
                "      },\n" +
                "      \"updateTime\":{\n" +
                "        \"type\": \"date\"\n" +
                "      },\n" +
                "      \"specs\":{\n" +
                "        \"type\": \"nested\",\n" +
                "        \"properties\": {\n" +
                "          \"name\":{\"type\": \"keyword\" },\n" +
                "          \"value\":{\"type\": \"keyword\" }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }//没设置的自动就是long 数字类型
//查询mysql放入到es

    @Override
    public void loadData() {
        int page = 1, rows = 100; // 分页查询不然Mysql会承受不起
        while (true) {
            log.info("开始导入第{}页数据",page);
            // 分页查询已经上架的spu            page, rows, true, null, null, null

            QuerySpuByPageDTO querySpuByPageDTO = new QuerySpuByPageDTO();
            querySpuByPageDTO.setPage(page);
            querySpuByPageDTO.setRows(rows);
            querySpuByPageDTO.setSaleable(true); //是否上下架
            querySpuByPageDTO.setId(null);
            querySpuByPageDTO.setBrandId(null);
            querySpuByPageDTO.setCategoryId(null);

            PageDTO<SpuDTO> result = itemClient.querySpuByPage(querySpuByPageDTO); //通过eureka 来调用item的api返回结果
            List<SpuDTO> list = result.getItems(); //获取当前页面的数据
            // 遍历Spu集合，把SpuDTO通过buildGoods方法转为Goods
            List<Goods> goodsList = list.stream().map(this::buildGoods).collect(Collectors.toList()); //spudto 变成goods
            //自动启动原理得到的动态代理多态对象
            // 批量写入Elasticsearch
            goodsRepository.saveAll(goodsList);
            log.info("导入第{}页数据结束。", page);//1
            // 翻页
            page++;//2

            Long totalPage = result.getTotalPage();
            if (page>totalPage) {
                //结束循环
                break;
            }
        }
    }
    private Goods buildGoods(SpuDTO spu) {
        //分词搜索的是谁?
        //dto String names = categories.stream().map(Category::getName).collect(Collectors.joining("/"));
        List<String> suggestion = new ArrayList<>(Arrays.asList(StringUtils.split(spu.getCategoryName(), "/")));  //分类名
        suggestion.add(spu.getName()); //商品名
        suggestion.add(spu.getBrandName());  // 参数如果变为双列集合那么映射非常多 转换成数组对象里面放双列集合那么映射变为两个    //品牌名   很多分词表   到时候建议查询头匹配  小文档揪出来 直接用小文档来分词查询
        //suggestion 是一个数组对象里面直接存的值所以不是嵌套对象  Lucene是不支持对象数据的 : 支持写成 [a a a]
        // 2.sku的价格集合
        // 2.1.查询sku集合
        List<SkuDTO> skuList = spu.getSkus();
        if (CollectionUtils.isEmpty(skuList)) {
            // 没有sku，我们去查询         api
            skuList = itemClient.querySkuBySpuId(spu.getId());
        }
            // 2.2.获取价格集合
        Set<Long> prices = skuList.stream().map(SkuDTO::getPrice).collect(Collectors.toSet());   //一个spu下面的所有价格 set 去重复
        // 3.商品销量   spu的销量总和
            long sold = skuList.stream().mapToLong(SkuDTO::getSold).sum();
            // 4.sku的某个图片
//            获取第一个sku的图片地址 得到spu的第一个sku的第一个图片
            String image = StringUtils.substringBefore(skuList.get(0).getImages(), ",");
            // 5.规格参数
       List<Map<String,Object>> specs   = new ArrayList<>();
            // 5.1.查询规格参数name和value键值对，只查询参与搜索的
        List<SpecParamDTO> params = itemClient.querySpecsValues(spu.getId(), true); //得到的是 spu对应的分类下的 规格参数集合 里面的每个数据 都根据同spu下的一个规格参数字段(是一个对象)查到了自己的value 也就是同一个spu 对应的 规格参数可以通过他自己所属于的类来查询 而每一个规格参数的值可以通过spu下的spec对象字段来查询  得到了一个spu的params

            // 5.2.封装
            for (SpecParamDTO param : params) {
                HashMap<String, Object> map = new HashMap<>(2);
                map.put("name", param.getName());
                map.put("value", chooseSegment(param));  //  [ {name:前置摄像头,value:501-1200万} {name:品牌,value:360} ]
                specs.add(map);
            }
            // 创建Goods对象，并封装数据

            Goods goods = new Goods();
            goods.setUpdateTime(new Date());

            // 自动补全的提示字段
            goods.setSuggestion(suggestion);


            // 规格参数

            goods.setSpecs(specs);

            // 商品销量
            goods.setSold(sold);
        //卖点 + 分类名 +品牌名 +商品名  这个大字符串就是分词搜索的目标    最难以理解的nested就是specs 用来聚合
        goods.setTitle(spu.getTitle() + StringUtils.join(suggestion, " ")); //把所有的字符串都拼串成一个超级长的字符串 这样会在分词搜索的时候好找 然后揪出来整行进行回填 字符串还带上空格有利于分词明确
            // sku的价格集合
            goods.setPrices(prices);
            // sku的某个图片
            goods.setImage(image);
            goods.setCategoryId(spu.getCid3());
            goods.setBrandId(spu.getBrandId());
            goods.setId(spu.getId());
            return goods;
        }

    private Object chooseSegment(SpecParamDTO p) {
        Object value = p.getValue();
        if (value == null || StringUtils.isBlank(value.toString())) {
            return "其它";
        }
        if (!p.getNumeric() || StringUtils.isBlank(p.getSegments()) || value instanceof Collection) { //不是数字 没段  非单个  把字符串返回
            return value;
        }

//来到这里就是数字了
        double val = parseDouble(value.toString());  //value -> val
        String result = "其它";
    //                                                                5
        String[] split = p.getSegments().split(",");   //1-2,6-7 , 10-13
        for (String segment : split) {
            String[] segs = segment.split("-");//1-2        1 2
            double begin = parseDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = parseDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val <= end) {
//来到这里说明 下面匹配到了上面
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;   //500万以下 501-1200万
    }

    @Override
    public Mono<List<String>> getSUggestion(String keyPrefix) {
        if (StringUtils.isBlank(keyPrefix)) {
            throw new LyException(400, "请求参数不能为空");

        }
        return goodsRepository.suggestBySingleField(SUGGESTION_FIELD, keyPrefix); //对suggession进行自动补全
    }

    @Override
    public Mono<PageInfo<Goods>> searchGoods(SearchParamDTO param) {
        //1.构建条件查询的工厂对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 1.1.source过滤
        sourceBuilder.fetchSource(DEFAULT_SOURCE_FIELD, new String[0]);
        // 1.2.查询条件
        MatchQueryBuilder matchQueryBuilder = buildBasicQuery(param);
        sourceBuilder.query(matchQueryBuilder);  //只会限制_source 不会限制_aggres
        // 1.3.分页条件
        sourceBuilder.from(param.getFrom());
        sourceBuilder.size(param.getSize());
        // 1.4.排序条件
        if(StringUtils.isNotBlank(param.getSortBy())){
            // 排序字段存在，才去排序
            sourceBuilder.sort(param.getSortBy(), param.getDesc() ? SortOrder.DESC: SortOrder.ASC);
        }

        // 1.5.高亮条件
        sourceBuilder.highlighter(new HighlightBuilder().field(DEFAULT_SEARCH_FIELD)
                .preTags(DEFAULT_PRE_TAG).postTags(DEFAULT_POST_TAG));
        return goodsRepository.queryBySourceBuilderForPageHighlight(sourceBuilder);
    }

    private double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0;
        }
    }


    @Override
    public Mono<Map<String, List<?>>> getFilterList(SearchParamDTO param) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        MatchQueryBuilder matchQueryBuilder = buildBasicQuery(param);
        //query
        sourceBuilder.query(matchQueryBuilder).size(0); //避免查太多
        //聚合
        //聚合分类
        sourceBuilder.aggregation(AggregationBuilders.terms("categoryAgg").field("categoryId").size(20));//词条桶聚合
        sourceBuilder.aggregation(AggregationBuilders.terms("brandAgg").field("brandId").size(20));//词条桶聚合
        sourceBuilder.aggregation(AggregationBuilders.nested("specAgg", "specs")

                .subAggregation(AggregationBuilders.terms("specNameAgg").field("specs.name").size(20)

                        .subAggregation(AggregationBuilders.terms("specValueAgg").field("specs.value").size(20)
                        )));   //词条桶聚合

        //1.请求对象
        SearchRequest request = new SearchRequest();
        //2准备请求参数
        request.source(sourceBuilder);
//3发出请求
        //异步结果用mono封装
        return Mono.create(monoSink -> {


            client.searchAsync(request, RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {
                @Override
                public void onResponse(SearchResponse response) {
                    //4解析结果
                    Map<String, List<?>> filterList = new LinkedHashMap<>();
                    Aggregations aggregations = response.getAggregations();
                    Terms categoryAgg = aggregations.get("categoryAgg");
                    List<? extends Terms.Bucket> buckets = categoryAgg.getBuckets();
                    //获取分类id
       /* List<Long> categoryIds = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {

            long id = bucket.getKeyAsNumber().longValue();
            categoryIds.add(id);
        }*/
                    List<Long> categoryIds = buckets.stream().map(Terms.Bucket::getKeyAsNumber).map(Number::longValue).collect(Collectors.toList());
                    List<CategoryDTO> categories = itemClient.queryCategoryByIds(categoryIds);
                    //存入map 加构 和 解构
                    filterList.put("分类", categories);


                    Terms brandAgg = aggregations.get("brandAgg");
                    List<? extends Terms.Bucket> BrandBuckets = brandAgg.getBuckets();
                    //获取分类id
       /* List<Long> categoryIds = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {

            long id = bucket.getKeyAsNumber().longValue();
            categoryIds.add(id);
        }*/
                    List<Long> brandIds = BrandBuckets.stream().map(Terms.Bucket::getKeyAsNumber).map(Number::longValue).collect(Collectors.toList());
                    List<BrandDTO> brandDTOS = itemClient.queryBrandByIds(brandIds);
                    //存入map 加构 和 解构
                    filterList.put("品牌", brandDTOS);
                    //解析规格参数
                    Nested specAgg = aggregations.get("specAgg");
                    Terms specNameAgg = specAgg.getAggregations().get("specNameAgg");
                    if (specNameAgg == null) {
                        monoSink.success(filterList);
                    }
                    for (Terms.Bucket nameBucket : specNameAgg.getBuckets()) {
                        //规格参数名称
                        String nameString = nameBucket.getKeyAsString();
                        //获取 该name 下的 value聚合结果
                        Terms specValueAgg = nameBucket.getAggregations().get("specValueAgg");
                        List<String> valueList = specValueAgg.getBuckets().stream()
                                .map(Terms.Bucket::getKeyAsString).collect(Collectors.toList());
                        filterList.put(nameString, valueList);
                    }
                    //发射结果
                    monoSink.success(filterList);
                }

                @Override
                public void onFailure(Exception e) {
                    monoSink.error(e);
                }
            });
        });
//        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
    }

    private MatchQueryBuilder buildBasicQuery(SearchParamDTO param) {
        String key = param.getKey();
        if (StringUtils.isBlank(key)) {
            // 搜索条件为null，返回异常
            throw new LyException(400, "搜索条件不能为空！");
        }
        return QueryBuilders.matchQuery(DEFAULT_SEARCH_FIELD, key);
    }

    @Override
    public void saveSpuById(Long spuId) {
        //构建goods
        SpuDTO spuDTO = itemClient.queryGoodsById(spuId);
        Goods goods = buildGoods(spuDTO);
        goodsRepository.save(goods);
    }

    @Override
    public void deleteById(Long spuId) {
        //删除一行文档
        goodsRepository.deleteById(spuId);
    }
}