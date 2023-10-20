package com.sykj.base;

public class RoutingDataSourceContext {
    //存储到 ThreadLocal 中
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public RoutingDataSourceContext(String key) {
        threadLocal.set(key);
    }

    //方便取当前threadLocal中的key
    public static String getRoutingDataSourceKey() {
        String key = threadLocal.get();
        return key == null ? "learn" : key;
    }
}
