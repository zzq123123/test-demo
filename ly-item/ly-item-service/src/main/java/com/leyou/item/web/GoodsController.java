package com.leyou.item.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyou.item.dto.QuerySpuByPageDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.service.SkuService;
import com.leyou.item.service.SpuDetailService;
import com.leyou.item.service.SpuService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
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
    @GetMapping("/spu/page")  //校验  并且打印web层日志 最后被统一异常管理
    public ResponseEntity<Page<SpuDTO>> querySpuByPage(@Valid QuerySpuByPageDTO querySpuByPageDTO, Errors errors){
        return ResponseEntity.ok(spuService.querySpecByPage(querySpuByPageDTO));
    }
}