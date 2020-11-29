package com.leyou.item.web;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.entity.Brand;
import com.leyou.item.entity.CategoryBrand;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * Package: com.leyou.item.web
 * Description：
 * Author: wude
 * Date:  2020-11-16 12:44
 * Modified By:
 */
@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;//非动态 多态
    @Autowired
    private CategoryBrandService categoryBrandService;
    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> queryBrandById(@PathVariable("id") Long id ){
        return ResponseEntity.ok(new BrandDTO(brandService.getById(id)));
    }

    @GetMapping("/list")
    public ResponseEntity<List<BrandDTO>> queryBrandByIds(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(BrandDTO.convertEntityList(brandService.listByIds(ids)));
    }

    @GetMapping("/page")
    public ResponseEntity<PageDTO<BrandDTO>> queryBrandByIds(@RequestParam(value = "key" ,required = false) String key, @RequestParam(value = "page" ,defaultValue = "1") Integer page, @RequestParam(value = "rows",defaultValue = "5") Integer rows){
      return   ResponseEntity.ok(brandService.queryBrandByPage(key, page, rows));
    }

    /**
     * 根据分类id查询到品牌
     * @param id
     * @return
     */
    @GetMapping("/brand/of/categpry")
    public ResponseEntity<List<BrandDTO>> queryBrandBycategory(@RequestParam("id") Long id) {
        return ResponseEntity.ok(brandService.queryBrandBycategory(id));//成功有内容
    }

    @PostMapping
    public ResponseEntity<Void> saveBrand(BrandDTO brandDTO) {
        brandService.saveBrand(brandDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build(); //成功并且创建没内容
    }

    @PutMapping
    public ResponseEntity<Void> uodateBrand(BrandDTO brandDTO){
        brandService.updateBrand(brandDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable("id") Long brandId){

        categoryBrandService.removeBrand(brandId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //成功没内容
    }
}