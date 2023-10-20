package com.sykj.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//标注为配置类
@Configuration
public class MyDataSourceAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(MyDataSourceAutoConfiguration.class);

    //加入到IoC容器,如果没有key默认为该方法名 -> learnDataSource
    @Bean
    //此处能扫描到yml中的配置信息
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.learn")
    public DataSource learnDataSource() {
        log.info("创建 learn 数据源   ...");
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.learnslave")
    public DataSource learnSlaveDataSource() {
        log.info("创建 learnslave 数据源   ...");
        return DataSourceBuilder.create().build();
    }

    @Bean
    //@Primary 该类返回有多个 DataSource 需要指定一个默认的
    @Primary
    public DataSource masterDataSource(
            //@Qualifier -> 因IoC容器中有多个DataSource ,所以需要通过key再次指定
            @Autowired @Qualifier("learnDataSource") DataSource learnDataSource,
            @Autowired @Qualifier("learnSlaveDataSource") DataSource learnSlaveDataSource
    ) {
        Map<Object, Object> map = new HashMap<>();
        map.put("learn", learnDataSource);
        map.put("learnslave", learnSlaveDataSource);
        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(map);
        return routingDataSource;
    }

}
