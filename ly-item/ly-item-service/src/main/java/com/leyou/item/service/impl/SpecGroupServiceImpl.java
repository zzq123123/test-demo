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
        //查询分类下规格组
        List<SpecGroup> groups = query().eq("category_id", categoryId).list();
        List<SpecGroupDTO> groupDTOList = SpecGroupDTO.convertEntityList(groups);
        if (CollectionUtils.isEmpty(groupDTOList)) {
            return groupDTOList;
        }
        // 2.所有的规格组 及 组内参数的映射, key 是groupId，值是该group下的所有param的集合
        // 2.1.查询出所有的param
        List<SpecParamDTO> allParams = specParamService.queryParams(categoryId, null, null);
        Map<Long, List<SpecParamDTO>> map = allParams.stream().collect(Collectors.groupingBy(SpecParamDTO::getGroupId));
        // 3.查询每个组的规格参数
        for (SpecGroupDTO groupDTO : groupDTOList) {
            // 2.1.获取groupId
            Long groupId = groupDTO.getId();
            // 2.2.根据groupId查询param
            List<SpecParamDTO> params = map.get(groupId);
            // 2.3.设置params
            groupDTO.setParams(params);
        }
        return groupDTOList;
    }
}
