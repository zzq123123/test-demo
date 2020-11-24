package com.leyou.search.service.impl;

import com.leyou.common.dto.PageDTO;
import com.leyou.common.exception.LyException;
import com.leyou.item.client.ItemClient;
import com.leyou.item.dto.QuerySpuByPageDTO;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.search.dto.SearchParamDTO;
import com.leyou.search.entity.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import com.leyou.starter.elastic.dto.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
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
        int page = 1, rows = 100;
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
            //自动启动原理得到的动态代理多态对象 都不是我写的
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
        //dto String names = categories.stream().map(Category::getName).collect(Collectors.joining("/"));
        List<String> suggestion = new ArrayList<>(Arrays.asList(StringUtils.split(spu.getCategoryName(), "/")));
        suggestion.add(spu.getName()); //商品名
        suggestion.add(spu.getBrandName());  // 参数如果变为双列集合那么映射非常多 转换成数组对象里面放双列集合那么映射变为两个
        //suggestion 是一个数组对象里面直接存的值所以不是嵌套对象  Lucene是不支持对象数据的 : 支持写成 [a a a]
        // 2.sku的价格集合
        // 2.1.查询sku集合
        List<SkuDTO> skuList = spu.getSkus();
        if (CollectionUtils.isEmpty(skuList)) {
            // 没有sku，我们去查询         api
            skuList = itemClient.querySkuBySpuId(spu.getId());
        }
            // 2.2.获取价格集合
            Set<Long> prices = skuList.stream().map(SkuDTO::getPrice).collect(Collectors.toSet());
            // 3.商品销量
            long sold = skuList.stream().mapToLong(SkuDTO::getSold).sum();
            // 4.sku的某个图片
//            获取第一个sku的图片地址
            String image = StringUtils.substringBefore(skuList.get(0).getImages(), ",");
            // 5.规格参数
       List<Map<String,Object>> specs   = new ArrayList<>();
            // 5.1.查询规格参数name和value键值对，只查询参与搜索的
            List<SpecParamDTO> params = itemClient.querySpecsValues(spu.getId(), true);

            // 5.2.封装
            for (SpecParamDTO param : params) {
                HashMap<String, Object> map = new HashMap<>(2);
                map.put("name", param.getName());
                map.put("value", chooseSegment(param));
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

            goods.setTitle(spu.getTitle()+StringUtils.join(suggestion, " ")); //把所有的字符串都拼串成一个超级长的字符串 这样在分词搜索的时候会好找 然后揪出来整行进行回填 字符串还带上空格有利于分词明确
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
        if(!p.getNumeric() || StringUtils.isBlank(p.getSegments()) || value instanceof Collection){
            return value;
        }  //不是数字 没段  非单个  把字符串返回


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
            if (val >= begin && val < end) {
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
        return result;
    }

    @Override
    public Mono<List<String>> getSUggestion(String keyPrefix) {
        if (StringUtils.isBlank(keyPrefix)) {
            throw new LyException(400, "请求参数不能为空");

        }
        return goodsRepository.suggestBySingleField(SUGGESTION_FIELD,keyPrefix);
    }

    @Override
    public Mono<PageInfo<Goods>> searchGoods(SearchParamDTO param) {
        //1.构建条件查询的工厂对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 1.1.source过滤
        sourceBuilder.fetchSource(DEFAULT_SOURCE_FIELD, new String[0]);
        // 1.2.查询条件
        String key = param.getKey();
        if (StringUtils.isBlank(key)) {
            // 搜索条件为null，返回异常
            throw new LyException(400, "搜索条件不能为空！");
        }
        sourceBuilder.query(QueryBuilders.matchQuery(DEFAULT_SEARCH_FIELD, key));
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
}