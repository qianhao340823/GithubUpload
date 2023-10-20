package com.sykj.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//表示该注解可用在方法上
@Target(ElementType.METHOD)
//设置生命周期
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicDatasource {

    //默认为 learn
    String value() default "learn";
}
