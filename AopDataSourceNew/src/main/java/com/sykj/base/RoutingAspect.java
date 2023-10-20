package com.sykj.base;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//标注为是一个AOP切面类
@Aspect
//@Component 把类实例化到容器中,相当于配置文件中的 <bean id="" class=""/>
@Component
public class RoutingAspect {
    //@Around环绕通知 -> SpringAOP 增强注解
    //@Around("execution(* com.ziye.controller.*.*(..))") -> 执行controller包下任意方法时均会进行增强
    @Around("@annotation(dynamicDatasource)")
    //参数 1: ProceedingJoinPoint -> 正在执行的连接点
    //参数 2: 传入的实例注解
    public Object routingAspect(ProceedingJoinPoint point, DynamicDatasource dynamicDatasource) throws Throwable {
        // 拿到注解上的参数
        String key = dynamicDatasource.value();
        new RoutingDataSourceContext(key);
        //继续执行方法
        return point.proceed();
    }
}
