package com.haibao.flink.cache;

/*
 * @Author ml.c
 * @Description 缓存枚举
 * @Date 18:15 2020-04-09
 **/
public enum CacheTypeEnum {
    /**
     * 无缓存
     */
    NONE("none"),
    /**
     * 缓存方式，基于jdk1.8+grovvy重构的LFU
     */
    W_TinyLFU("W_TinyLFU");

    String cacheType;

    CacheTypeEnum(String cacheType) {
        this.cacheType = cacheType;
    }

    public String getCacheType() {
        return cacheType;
    }
}
