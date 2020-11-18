package com.leyou.item.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.exception.LyException;
import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.entity.SpecGroup;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.service.SpecGroupService;
import com.leyou.item.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Package: com.leyou.item.service.impl
 * Description：
 * Author: wude
 * Date:  2020-11-17 13:33
 * Modified By:
 */
@Service
public class SpecGroupServiceImpl extends ServiceImpl<SpecGroupMapper, SpecGroup> implements SpecGroupService {
    @Autowired
    SpecParamService specParamService;
    @Override
    public List<SpecGroupDTO> queryspecList(Long categoryId) {
        List<SpecGroup> specGroupList = this.list();
        List<SpecGroupDTO> groupList = SpecGroupDTO.convertEntityList(specGroupList);
        if (CollectionUtils.isEmpty(groupList)) {
          throw   new LyException(404, "该分类下的规格组不存在");
        }
        List<SpecParamDTO> specParamDTOS = specParamService.queryParams(categoryId, null, null);
        Map<Long, List<SpecParamDTO>> map = specParamDTOS.stream().collect(Collectors.groupingBy(SpecParamDTO::getGroupId));
        for (SpecGroupDTO specGroupDTO : groupList) {
            specGroupDTO.setParams(map.get(specGroupDTO.getId()));
        }
        return groupList;
    }
}
