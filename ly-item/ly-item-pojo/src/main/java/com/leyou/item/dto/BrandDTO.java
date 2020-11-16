package com.leyou.item.dto;

import com.leyou.common.dto.BaseDTO;
import com.leyou.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huyi.zhang
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BrandDTO extends BaseDTO {
    private Long id;
    private String name;
    private String image;
    private Character letter;
    private List<Long> categoryIds; //返回的时候对象 序列化成json字符串可以配置jackson让字段为Null的值忽略字段

    public BrandDTO(BaseEntity entity) {
        super(entity);
    }

    public static <T extends BaseEntity> List<BrandDTO> convertEntityList(Collection<T> list) {
        if(list == null){
            return Collections.emptyList();
        }
        return list.stream().map(BrandDTO::new).collect(Collectors.toList());
    }
}