package com.leyou.page.handler;

import com.leyou.item.dto.SkuDTO;
import com.leyou.page.Entry.SkuPO;
import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

/**
 * Package: com.leyou.page.handler
 * Description：
 * Author: wude
 * Date:  2020-11-28 19:48
 * Modified By:
 */
@Component
@CanalTable(value = "tb_sku")
public class SkuHandler implements EntryHandler<SkuPO>{
    @Autowired
  private    PageService pageService;

    @Override
    public void insert(SkuPO skuPO) {
      pageService.loadSkuListData(skuPO.getSpuId()); //数据库发生改变的时候查询sku list存入redis 后台人员修改的数据库
    }

    @Override
    public void update(SkuPO before, SkuPO after) {
      pageService.loadSkuListData((after.getSpuId()));
    }

    @Override
    public void delete(SkuPO skuPO) {
      pageService.deleteSkuList(skuPO.getSpuId());
    }
}
