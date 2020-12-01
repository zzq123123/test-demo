package com.leyou.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leyou.common.entity.BaseEntity;
import lombok.Data;

/**
 * @author 虎哥
 */
@Data
@TableName("tb_client_info")
public class ClientInfo extends BaseEntity {
    @TableId//yml配置自增长
    private Long id;
    private String clientId;
    private String secret;
    private String info;
}
