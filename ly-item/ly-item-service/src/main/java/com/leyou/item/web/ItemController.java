package com.leyou.item.web;
import com.leyou.item.entity.Item;
import com.leyou.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 虎哥
 */
@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<Item> saveItem(Item item){

        Item result = itemService.saveItem(item);
        // 新增成功, 返回201  如果这里是编译时异常那么就自己处理
        return ResponseEntity.status(HttpStatus.CREATED).body(result);

 /*

        try {
            Item result = itemService.saveItem(item);
            // 新增成功, 返回201
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            // 失败，返回400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }*/
    }
}