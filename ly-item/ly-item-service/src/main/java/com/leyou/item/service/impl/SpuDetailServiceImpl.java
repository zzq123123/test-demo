package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.dto.SpuDetailDTO;
import com.leyou.item.entity.Spu;
import com.leyou.item.entity.SpuDetail;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.service.SpuDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 虎哥
 */
@Service
public class SpuDetailServiceImpl extends ServiceImpl<SpuDetailMapper, SpuDetail> implements SpuDetailService {
    @Resource
    SpuMapper spuMapper;
    @Resource
    SpecParamServiceImpl paramService;

    @Override
    @Transactional
    public SpuDetailDTO queryDetailBySpuId(Long id) {
        SpuDetail spuDetail = this.getById(id);
        SpuDetailDTO spuDetailDTO = new SpuDetailDTO(spuDetail);

        return spuDetailDTO;
    }

    @Override
    public List<SpecParamDTO> querySpecValues(Long id, Boolean searching) {
        // 1.准备规格参数value
        // 1.1.根据id查询spuDetail
        SpuDetail detail = getById(id);
        // 1.2.获取其中的规格参数键值对，转为一个map
        Map<Long, Object> specValues = JsonUtils.toMap(detail.getSpecification(), Long.class, Object.class);
        //{k=v    } k:参数id 参数值 所以现在需要一行一行的参数对象()来和map对应想要获得这个对象集合必须先要获得分类信息

        // 2.准备规格参数对象
        // 2.1.根据id查询spu，获取商品分类信息
        Spu spu = spuMapper.selectById(id);// 多态动态代理对象
        // 2.2.根据分类id查询规格参数集合，如果searching不为空，还要加上searching条件

        List<SpecParamDTO> params = paramService.queryParams(spu.getCid3(), null, searching);
        // 3.找到param的value，并存储
        for (SpecParamDTO param : params) {
            param.setValue(specValues.get(param.getId())); //spu对应的分类下的spec的集合的每个对象分别
            //用id去spu对应的map中去取值赋值给spu 对应的分类下的 spec得每个对象的value 能对应上
        }
        return params;
    }
}
