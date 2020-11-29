package com.leyou.page;
import com.leyou.common.annotation.EnableExceptionAdvice;
import com.leyou.item.config.FeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
/**
 *
 * 品牌 分类 spec都在本地缓存但是到了10分钟还回去redis中查
 *
 * 所以 brand categorys spec sku spu 都需要 和数据库保存同步    监听变化条用其他微服务去数据库中查询 然后写入到redis
 *
 * 这次 page 微服务 通过自己依赖的api模块 来fein调用 其他模块 虽然api离得我远 离itemservice近  但是api依然是我的一部分我用他来调用其他微服务
 *
 * 我的一部分需要别人来给我写
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * @author 虎哥
 */
@EnableExceptionAdvice
@SpringBootApplication
 public class LyPageApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyPageApplication.class, args);
    }
}