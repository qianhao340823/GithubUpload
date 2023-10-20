package org.example.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.api.InfoService;
import org.springframework.stereotype.Component;

// dubbo提供的Service注解，用于声明对外暴露服务
// Service引入的是org.apache.dubbo.config.annotation.Service包
@Component
@DubboService
public class InfoServiceImpl implements InfoService {

    @Override
    public String getInfo() {

        return "hello，这里是dubbo-provider模块！";
    }
}
