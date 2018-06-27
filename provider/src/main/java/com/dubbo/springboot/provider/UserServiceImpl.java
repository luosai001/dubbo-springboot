package com.dubbo.springboot.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.dubbo.springboot.api.User;
import com.dubbo.springboot.api.UserService;

/**
 * Created by sai.luo on 2018-6-27.
 */
@Service(
        version = "${demo.service.version}",
        protocol ="rest"
)
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(Long id) {
        return new User(id,"zs"+id);
    }
}
