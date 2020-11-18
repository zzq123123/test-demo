package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.entity.SpecParam;

import java.util.List;

/**
 * Package: com.leyou.item.service
 * Description：
 * Author: wude
 * Date:  2020-11-17 13:33
 * Modified By:
 */
public interface SpecParamService extends IService<SpecParam> {
    List<SpecParamDTO> queryParams(Long categoryId, Long groupId, Boolean searching);
}
