package com.leyou.user.web;

import com.leyou.user.dto.AddressDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户地址管理
 */
@RestController
@RequestMapping("address")
public class AddressController {

    @GetMapping("hello")
    public ResponseEntity<String> hello(){
        // 校验JWT
        return ResponseEntity.ok("上海浦东新区航头镇航头路18号传智播客");
    }
    @GetMapping("hello2")
    public ResponseEntity<String> hello2(){
        // 校验JWT2
        return ResponseEntity.ok("上海浦东新区航头镇航头路18号黑马程序员");
    }


    /**
     * 根据
     * @param id 地址id
     * @return 地址信息
     */

    @GetMapping("{/id}")
    public ResponseEntity<AddressDTO> QueryAddressById(@PathVariable("id")Long id){

        AddressDTO address = new AddressDTO();
        address.setId(1L);
        address.setUserId(30L);
        address.setStreet("航头镇航头路18号传智播客 3号楼");
        address.setCity("上海");
        address.setDistrict("浦东新区");
        address.setAddressee("虎哥");
        address.setPhone("15800000000");
        address.setProvince("上海");
        address.setPostcode("210000");
        address.setIsDefault(true);

        return ResponseEntity.ok(address);  //get请求
    }
}