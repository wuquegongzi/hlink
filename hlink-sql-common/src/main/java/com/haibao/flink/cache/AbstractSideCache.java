package com.haibao.flink.cache;

import java.io.Serializable;

/*
 * @Author ml.c
 * @Description 抽象缓存层
 * @Date 16:34 2020-04-09
 **/
public abstract class AbstractSideCache implements Serializable {

    private static final long serialVersionUID = 1L;

    protected CacheParm cacheParm;

    public AbstractSideCache(CacheParm cacheParm){
        this.cacheParm = cacheParm;
    }

    public abstract void initCache();

    public abstract  Object getFromCache(String key);

    public abstract void putCache(String key, Object value);

    public abstract void removeCache(String key);
}
