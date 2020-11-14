package com.leyou.item.dto;

import com.leyou.common.dto.BaseDTO;
import com.leyou.common.entity.BaseEntity;
import com.sun.org.apache.xpath.internal.functions.FuncFalse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Package: com.leyou.item.dto
 * Description：
 * Author: wude
 * Date:  2020-11-14 20:51
 * Modified By:
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CategoryDTO extends BaseDTO {
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;

    public CategoryDTO(BaseEntity entity) {
        super(entity);
    }

    public static <T extends BaseEntity> List<CategoryDTO> convertEntityList(Collection<T> list){

        if (list ==null) {
            return Collections.EMPTY_LIST;
        }
        return list.stream().map(CategoryDTO::new).collect(Collectors.toList());//栈板上面的元素来调用方发的时候才能使用 类::非静态方法  都要满足消费者提供者模型
    }


}
