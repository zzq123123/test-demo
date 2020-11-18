package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.entity.SpecGroup;

import java.util.List;

/**
 * Package: com.leyou.item.service
 * Description：
 * Author: wude
 * Date:  2020-11-17 13:32
 * Modified By:
 */

public interface SpecGroupService extends IService<SpecGroup> {
    List<SpecGroupDTO> queryspecList(Long categoryId);
}
