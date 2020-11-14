package com.leyou.item.web;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.leyou.common.entity.Category;
import com.leyou.item.dto.CategoryDTO;
import com.leyou.item.entity.CategoryBrand;
import com.leyou.item.service.CategorySerice;
import com.leyou.item.service.impl.CategoryBrandServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * Package: com.leyou.item.web
 * Description：
 * Author: wude
 * Date:  2020-11-14 20:14
            * Modified By:
            */
    @RestController //自带 响应头按照json解析 数据
    @RequestMapping("category")
    public class CategoryController {
        @Autowired
    private CategorySerice categoryservice;

/*
    @Autowired //这里是CategoryController 不要把 CategoryBrandServiceImpl 引来
    private CategoryBrandServiceImpl brandService;
*/
    @GetMapping("{id}")
    public ResponseEntity<CategoryDTO> queryCategoryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new CategoryDTO(categoryservice.getById(id)));
    }
    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> queryCategoryByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.status(HttpStatus.OK).body(CategoryDTO.convertEntityList(categoryservice.listByIds(ids)));
    }
    @GetMapping("/of/brand")//多对多 就是两个 一对多   //接口文档的变量名是id 这里为了好看写成brandId就好了 接口文档规范的是输入和输出 输入名字落脚点在前段 输出的名字落脚点在后端
    public ResponseEntity<List<CategoryDTO>> queryCategoryBybrand(@RequestParam("id") Long brandId) {
/*//        这里面不要写太多的代码
        QueryChainWrapper<CategoryBrand> brand_id = brandService.query().eq("brand_id", brandId);*/

        List<CategoryDTO> categoryDTOS = categoryservice.queryCategoryBybrand(brandId);

        return ResponseEntity.status(HttpStatus.OK).body(categoryDTOS);
    }
    @GetMapping("of/parent")
    public ResponseEntity<List<CategoryDTO>> queryCategoryByParentId(@RequestParam("pid") Long parentId) {
        List<Category> parentIdList = categoryservice.query().eq("parent_id", parentId).list();
        return ResponseEntity.status(HttpStatus.OK).body(CategoryDTO.convertEntityList(parentIdList));
    }
}