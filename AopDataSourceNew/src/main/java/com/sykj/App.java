package com.sykj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Hello world!
 *
 */
//排除spring自身的数据源获取类
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//配置mapper路径
@MapperScan({"com.sykj.mapper"})
public class App 
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }
}
