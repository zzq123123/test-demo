package com.leyou.search.repository;

        import com.leyou.search.entity.Goods;
        import com.leyou.starter.elastic.repository.Repository;

/**
 * @author 虎哥
 */
public interface GoodsRepository extends Repository<Goods, Long> { //项目启动的时候会starter 原理 创建动态代理多态对象放入到ioc中
}