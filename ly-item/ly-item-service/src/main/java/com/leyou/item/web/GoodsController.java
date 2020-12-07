package com.leyou.item.web;
import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.*;
import com.leyou.item.entity.Sku;
import com.leyou.item.entity.Spu;
import com.leyou.item.service.SkuService;
import com.leyou.item.service.SpecParamService;
import com.leyou.item.service.SpuDetailService;
import com.leyou.item.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("goods")
public class GoodsController {

    private final SpuService spuService;
    private final SpuDetailService detailService;
    private final SkuService skuService;

    public GoodsController(SpuService SpuService, SpuDetailService detailService, SkuService skuService) {
        this.spuService = SpuService;
        this.detailService = detailService;
        this.skuService = skuService;
    }

@Autowired
    private SpecParamService specParamService;


    @GetMapping("/spu/page")  //校验  并且打印web层日志 最后被统一异常管理
    public ResponseEntity<PageDTO<SpuDTO>> querySpuByPage(@Valid QuerySpuByPageDTO querySpuByPageDTO, Errors errors) {
        return ResponseEntity.ok(spuService.querySpecByPage(querySpuByPageDTO));
    }

    /**
     * 新增商品
     *
     * @param spuDTO 页面提交商品信息
     * @return 无
     */


    @PostMapping("spu")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuDTO spuDTO) { //商品
        spuService.saveGoods(spuDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * @param spuId
     * @param saleable 上下架  核心逻辑是 新增 商品肯定是下架    想要修改修改和删除必须先下架
     */
    @PutMapping("/saleable")
    public ResponseEntity<Void> updateSaleble(@RequestParam("id") Long spuId, @RequestParam("saleable") Boolean saleable) {

        spuService.updateSaleble(spuId, saleable);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpuDTO> queryGoodsById(@PathVariable("id") Long id) {
        SpuDTO spuDTO = spuService.queryGoodsById(id);

        return ResponseEntity.ok(spuDTO);
    }


    @PutMapping
    public ResponseEntity<Void> updateGoods(@RequestBody SpuDTO spuDTO) { //商品
        spuService.updateGoods(spuDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/spu/detail")
    public ResponseEntity<SpuDetailDTO> queryDetailBySpuId(@RequestParam("id") Long spuId) {
        SpuDetailDTO spuDetailDTO = detailService.queryDetailBySpuId(spuId);
        return ResponseEntity.ok(spuDetailDTO);
    }

    @GetMapping("/sku/of/spu")
    public ResponseEntity<List<SkuDTO>> querySkuBySpuId(@RequestParam("id") Long spuId) {
        List<SkuDTO> skuDTOS = skuService.querySkuBySpuId(spuId);
        return ResponseEntity.ok(skuDTOS);
    }
    @GetMapping("/sku/list")
    public ResponseEntity<List<SkuDTO>> querySkuByIds(@RequestParam("ids") List<Long> ids){
        List<Sku> skuList = skuService.listByIds(ids);
        List<SkuDTO> skuDTOS = SkuDTO.convertEntityList(skuList);
        return ResponseEntity.ok(skuDTOS);
    }
        //根据id 查商品Spu

    @GetMapping("/spu/{id}")
    public ResponseEntity<SpuDTO> querySpuById(@PathVariable("id")Long id ){
        Spu spu = spuService.getById(id);
        return ResponseEntity.ok(new SpuDTO(spu));
    }

    /**
     * 参数键值对
     * @return
     */
    @GetMapping("/spec/value")
    public ResponseEntity<List<SpecParamDTO>> querySpecsValues(
            @RequestParam("id") Long id, @RequestParam(value = "searching", required = false) Boolean searching){//可以不传
        return ResponseEntity.ok(detailService.querySpecValues(id, searching));
    }


/**
 * 扣减库存
 * @param cartMap 商品集合
 */

                                       //前端的字符转  自动绑定成对象map 对象
@PutMapping("/stock/minus")
    public ResponseEntity<Void> deductStock(@RequestBody Map<Long,Integer> cartMap) {
    skuService.deductStock(cartMap);

  return   ResponseEntity.status(HttpStatus.NO_CONTENT).build();

}
    /**
     * 加库存
     * @param cartMap 商品id及数量的map
     */
    @PutMapping("/stock/plus")
    public ResponseEntity<Void> addStock (@RequestBody Map<Long, Integer> cartMap){
        skuService.addStock(cartMap);
   return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 查询是200  删改204没内容 新增是201
    }

}