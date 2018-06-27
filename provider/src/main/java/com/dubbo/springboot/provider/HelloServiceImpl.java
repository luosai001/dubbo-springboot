package com.dubbo.springboot.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.dubbo.springboot.api.HelloService;
import com.dubbo.springboot.api.User;

/**
 * Created by sai.luo on 2018-6-21.
 */

@Service(
        version = "${demo.service.version}",
        protocol ="dubbo"
)
public class HelloServiceImpl implements HelloService {
    @Override

    public String hello(String hello) {
        System.out.println(" hello - ");
        return "hello: "+hello;
    }
}
