package com.sykj.base;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        //从 threadLocal 中拿到数据源的key
        return RoutingDataSourceContext.getRoutingDataSourceKey();
    }

}
