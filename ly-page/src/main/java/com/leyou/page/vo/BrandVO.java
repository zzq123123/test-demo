package com.leyou.page.vo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 虎哥
 */
@Data
@Table(name = "tb_brand")
public class BrandVO {
    @Id
    private Long id;
    @Column(name="name")
    private String name;
}