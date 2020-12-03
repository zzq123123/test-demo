package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.exception.LyException;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.entity.Sku;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.service.SkuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 虎哥
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {


    private static final String DEDUCT_STOCK_STATEMENT = "com.leyou.item.mapper.SkuMapper.deductStock";
    @Override
    @Transactional
    public List<SkuDTO> querySkuBySpuId(Long id) {
        List<Sku> skuList = this.query().eq("spu_id", id).list();
        List<SkuDTO> skuDTOS = SkuDTO.convertEntityList(skuList);
        return skuDTOS;
    }

    @Override
    @Transactional
    public void deductStock(Map<Long, Integer> cartMap) {
 /*       for (Map.Entry<Long, Integer> entry : cartMap.entrySet()) {
            Long skuId = entry.getKey();
            Integer num = entry.getValue();
            //查询sku
            Sku sku = this.getById(skuId);
            if (sku.getStock()<num) {
                //库存不足直接抛出异常 这里不要锁了 直接两个人一起来吧
                throw new LyException(400, "库存不足");
            }
            //两人都来
            HashMap<String, Object> param = new HashMap<>();
            //如果充足 扣减库存 update tb_sku set stock = stock -num,sold = sold +num where id = 1;
            param.put("id", skuId);
            param.put("num", num);
            getBaseMapper().deductStock(param);
        }
    }*/


        this.executeBatch(sqlSession ->
        {
            for (Map.Entry<Long, Integer> entry : cartMap.entrySet()) {

                HashMap<String , Object> param = new HashMap<>();
                param.put("id", entry.getKey());
                param.put("num", entry.getValue());

                sqlSession.update(DEDUCT_STOCK_STATEMENT,param);
            }
            sqlSession.flushStatements();
        });

    }
}