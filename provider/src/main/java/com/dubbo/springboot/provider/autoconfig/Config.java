package com.dubbo.springboot.provider.autoconfig;

import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sai.luo on 2018-6-27.
 */
@Configuration
public class Config {

    @Bean("dubbo")
    public ProtocolConfig dubboProtocolConfig(){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(12345);
        protocolConfig.setId("dubbo");
        protocolConfig.setServer(null);
        return protocolConfig;
    }

    @Bean("rest")
    public ProtocolConfig restProtocolConfig(){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("rest");
        protocolConfig.setPort(9002);
        protocolConfig.setId("rest");
        protocolConfig.setServer("tomcat");
        return protocolConfig;
    }
}
