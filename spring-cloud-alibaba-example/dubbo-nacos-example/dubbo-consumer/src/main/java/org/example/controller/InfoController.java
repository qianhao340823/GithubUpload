package org.example.controller;


import org.apache.dubbo.config.annotation.DubboReference;
import org.example.api.InfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    //dubbo提供的Reference注解，用于调用远程服务
    @DubboReference(check = false)
    private InfoService infoService;

    @GetMapping("/getInfo")
    public String getInfo(){

        return infoService.getInfo();
    }
}