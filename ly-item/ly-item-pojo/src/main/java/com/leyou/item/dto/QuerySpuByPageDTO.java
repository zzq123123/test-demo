package com.leyou.item.dto;
import com.leyou.common.dto.BaseDTO;
import com.leyou.common.entity.BaseEntity;
import com.leyou.item.annotation.Mymin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Package: com.leyou.item.dto
 * Description：
 * Author: wude
 * Date:  2020-11-17 21:44
 * Modified By:
 */
@Data
@AllArgsConstructor()
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class QuerySpuByPageDTO extends BaseDTO {
    @Min(value = 1,message = "页数必须大于等于1")
    private Integer page=1; //默认就是1
    @Mymin(message = "页面大小必须是大于等于5")
    private Integer rows=5;

    private Boolean saleable;
    private Long categoryId;//可以为false 就是null
    private Long brandId;
    private Long id;

    public QuerySpuByPageDTO(BaseEntity entity) {
        super(entity);
    }

    public static <T extends BaseEntity> List<QuerySpuByPageDTO> convertEntityList(Collection<T> list){

        if (list ==null) {
            return Collections.EMPTY_LIST;
        }
        return list.stream().map(QuerySpuByPageDTO::new).collect(Collectors.toList());//栈板上面的元素来调用方发的时候才能使用 类::非静态方法  都要满足消费者提供者模型
    }
}