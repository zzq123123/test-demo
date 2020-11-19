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
 * Package: com.leyou.item.dto
 * Description：
 * Author: wude
 * Date:  2020-11-17 13:26
 * Modified By:
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SpecParamDTO extends BaseDTO {
    private Long id;
    private Long categoryId;
    private Long groupId;
    private String name;
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;
    private String options;
    private Object value;
    public SpecParamDTO(BaseEntity baseEntity) {
        super(baseEntity);
    }
    public static <T extends BaseEntity> List<SpecParamDTO> convertEntityList(Collection<T> list) {
        if (list == null) {
            return Collections.emptyList();
        }

        return  list.stream().map(SpecParamDTO::new ).collect(Collectors.toList());
    }


}
