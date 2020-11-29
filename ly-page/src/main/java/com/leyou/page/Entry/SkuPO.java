package com.leyou.page.Entry;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Package: com.leyou.page.Entry
 * Description：
 * Author: wude
 * Date:  2020-11-28 19:51
 * Modified By:
 */
@Data
@Table(name="tb_sku")
public class SkuPO {
    @Id
    private Long id;
    @Column(name="spu_id")
    private Long spuId;
}
