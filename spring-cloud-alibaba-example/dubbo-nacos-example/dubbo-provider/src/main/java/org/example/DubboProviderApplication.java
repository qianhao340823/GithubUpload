package org.example;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication
public class DubboProviderApplication {

    public static void main(String[] args) {

        SpringApplication.run(DubboProviderApplication.class, args);
        System.out.println("dubbo服务提供者8180启动了");
    }

}