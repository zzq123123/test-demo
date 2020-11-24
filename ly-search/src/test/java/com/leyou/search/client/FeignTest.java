package com.leyou.search.client;

import com.leyou.item.client.ItemClient;
import com.leyou.item.dto.SpecParamDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;



@RunWith(SpringRunner.class)
@SpringBootTest
public class FeignTest {

    @Autowired
    private ItemClient itemClient;

    @Test
    public void testQuerySpecValues(){
        List<SpecParamDTO> list = itemClient.querySpecsValues(114L, true);

        list.forEach(System.out::println);
    }
}
