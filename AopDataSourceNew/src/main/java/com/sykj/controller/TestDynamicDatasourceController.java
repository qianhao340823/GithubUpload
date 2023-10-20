package com.sykj.controller;

import com.sykj.base.DynamicDatasource;
import com.sykj.entity.User;
import com.sykj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author xuxu
 * @Date 2022/6/24
 * @Describe 测试动态数据源controller
 */
@RestController
public class TestDynamicDatasourceController {
    @Autowired
    private UserService userService;

    @GetMapping("getLearn")
    @DynamicDatasource
    public List<User> getLearn() {
        return userService.findAllUser();
    }

    @GetMapping("getLearnSlave")
    @DynamicDatasource("learnslave")
    public List<User> getLearnSlave(){
        return userService.findAllUser();
    }

}
