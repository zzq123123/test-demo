package cn.itcast.elasticsearch.test;

import cn.itcast.elasticsearch.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import javax.annotation.Resource;

/**
 * Package: cn.itcast.elasticsearch.test
 * Description：
 * Author: wude
 * Date:  2020-11-25 19:12
 * Modified By:
 */
public class T1 {
    @Resource
   static   ElasticsearchTemplate template;
    public static void main(String[] args) {
        template.createIndex(Item.class);
    }


}
