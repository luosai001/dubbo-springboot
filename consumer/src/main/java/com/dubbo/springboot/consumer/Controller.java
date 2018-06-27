package com.dubbo.springboot.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dubbo.springboot.api.HelloService;
import com.dubbo.springboot.api.User;
import com.dubbo.springboot.api.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sai.luo on 2018-6-21.
 */
@RestController
public class Controller {
    @Reference(
            version = "${demo.service.version}"
    )
    HelloService helloService;
    @Reference(
            version = "${demo.service.version}"
    )
    UserService userService;

    @RequestMapping("/")
    public String hello( String world){
        return helloService.hello(world);
    }

    @RequestMapping("/{id:\\d+}")
    public User getUser(@PathVariable("id") Long id){
        return userService.getUser(id);
    }
}
