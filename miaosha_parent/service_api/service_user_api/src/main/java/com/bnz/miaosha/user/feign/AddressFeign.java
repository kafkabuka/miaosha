package com.bnz.miaosha.user.feign;

import com.bnz.miaosha.entity.Result;
import com.bnz.miaosha.user.pojo.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "user")
public interface AddressFeign {

    @GetMapping("/address/list")
    public Result<List<Address>> list();
}
