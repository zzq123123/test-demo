package com.leyou.page.handler;


import com.leyou.common.utils.JsonUtils;
import com.leyou.page.service.PageService;
import com.leyou.page.vo.BrandVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

import javax.annotation.Resource;
import javax.persistence.Table;

/**
 * Package: com.leyou.page.handler
 * Description：
 * Author: wude
 * Date:  2020-11-28 20:15
 * Modified By:
 */
@Component
@CanalTable(value = "tb_brand")
public class BrandHandler implements EntryHandler<BrandVO> {
    private static final String BRAND_KEY_PREFIX = "page:brand:id:";

    @Resource
    PageService pageService;
    @Override

    public void insert(BrandVO brandVO) {
        pageService.saveBrand2Redis(brandVO.getId(),JsonUtils.toJson(brandVO),BRAND_KEY_PREFIX);
    }

    @Override
    public void update(BrandVO before, BrandVO after) {
        pageService.saveBrand2Redis(after.getId(),JsonUtils.toJson(after),BRAND_KEY_PREFIX);
    }

    @Override
    public void delete(BrandVO brandVO) {
        pageService.deleteBrandById(brandVO.getId());
    }
}
