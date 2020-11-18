package com.leyou.item.web;
import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.entity.SpecGroup;
import com.leyou.item.entity.SpecParam;
import com.leyou.item.service.SpecGroupService;
import com.leyou.item.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.GroupSequence;
import java.util.List;
/**
 * Package: com.leyou.item.web
 * Description：
 * Author: wude
 * Date:  2020-11-17 13:36
 * Modified By:
 */
@RestController
@RequestMapping("/spec")
public class SpecController {//融合和specgroup specparam 二者接口相似(预定义函数)

    @Autowired
    private SpecParamService paramService;
    @Autowired
    private SpecGroupService groupService;


    /**
     * 根据商品分类查询规格组
     *
     * @param id 商品分类id
     * @return 规格组集合
     */
    @GetMapping("/groups/of/category")
    public ResponseEntity<List<SpecGroupDTO>> queryGroupByCategory(
            @RequestParam("id") Long id) {
        List<SpecGroup> category_id = groupService.query().eq("category_id", id).list();
        List<SpecGroupDTO> specGroupDTOS = SpecGroupDTO.convertEntityList(category_id);
        return ResponseEntity.ok(specGroupDTOS);
    }
    /**
     * 查询规格参数集合
     * @param categoryId 分类id
     * @param groupId 规格组id
     * @param searching 是否搜索
     * @return 参数集合
     */
    @GetMapping("/params")
    //@ResponseStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
    public ResponseEntity<List<SpecParamDTO>> queryParams(@RequestParam(value = "categoryId",required = false) Long categoryId,@RequestParam(value = "groupId",required = false) Long groupId,@RequestParam(value = "searching",required = false) Boolean searching ){
        return ResponseEntity.ok(paramService.queryParams(categoryId, groupId, searching));
    }
    /**
     * 新增规格组
     * @param groupDTO 规格组信息
     * @return 无
     */

    @PostMapping("/group")
    public  ResponseEntity<Void> saveGroup(@RequestBody SpecGroupDTO groupDTO) { //反序列化
         groupService.save(groupDTO.toEntity(SpecGroup.class));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /**
     * 修改规格组
     * @param groupDTO 规格组信息
     * @return 无
     */
    @PutMapping("group")
    public ResponseEntity<Void> updateGroup(@RequestBody SpecGroupDTO groupDTO){
        groupService.updateById(groupDTO.toEntity(SpecGroup.class));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    /**
     * 新增规格参数
     * @param specParamDTO 规格组信息
     * @return 无
     */

    @PostMapping("/param")
    public ResponseEntity<Void> saveParam(@RequestBody SpecParamDTO specParamDTO) {
        paramService.save(specParamDTO.toEntity(SpecParam.class));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改规格参数
     * @param specParamDTO 规格组信息
     * @return 无
     */
    @PutMapping("/param")
    public ResponseEntity<Void> updateParam(@RequestBody SpecParamDTO specParamDTO){

        paramService.updateById(specParamDTO.toEntity(SpecParam.class));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    // [ {  params: [ {} {} ]   ,  ,   } ,        ]
        
    @GetMapping("list")
    public ResponseEntity<List<SpecGroupDTO>> querySpecList(@RequestParam(value = "id",required = false) Long categoryId){
        return ResponseEntity.ok(groupService.queryspecList(categoryId));
    }




















































    
}

