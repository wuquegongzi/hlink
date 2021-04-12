package com.haibao.flink.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/*
 * @Author ml.c
 * @Description
 * @Date 16:42 2020-04-09
 **/
public class W_TinyLFUSideCache extends AbstractSideCache {

    protected transient Cache<Object, Object> cache;

    public W_TinyLFUSideCache(CacheParm cacheParm) {
        super(cacheParm);
    }

    /**
     * 缓存初始化
     */
    @Override
    public void initCache() {

        if (null != cacheParm && !CacheTypeEnum.NONE.getCacheType().equals(cacheParm.getCacheType()) && null == cache) {
            this.cache = Caffeine.newBuilder()
                    // 设置最后一次写入或访问后经过固定时间过期
                    .expireAfterWrite(cacheParm.getExpireAfterWrite(), TimeUnit.SECONDS)
                    // 初始的缓存空间大小
                    .initialCapacity(cacheParm.getInitialCapacity())
                    // 缓存的最大条数
                    .maximumSize(cacheParm.getMaximumSize())
                    .build();
        }
    }

    @Override
    public Object getFromCache(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void putCache(String key,Object value) {
        cache.put(key,value);
    }

    @Override
    public void removeCache(String key) {
        cache.invalidate(key);
    }

}
