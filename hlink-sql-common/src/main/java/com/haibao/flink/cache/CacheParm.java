package com.haibao.flink.cache;

import java.io.Serializable;

/*
 * @Author ml.c
 * @Description 维表 Caffeine缓存配置参数对象
 * @Date 17:06 2020-04-08
 **/
public class CacheParm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 是否开启缓存
     */
    private String cacheType;
    /**
     * 初始的缓存空间大小(整数)
     */
    private Integer initialCapacity;

    /**
     * 缓存最大数（整数)
     */
    private Long maximumSize;

    /**
     * 过期时间(s)
     */
    private Long expireAfterWrite;


    private CacheParm(Builder builder) {
        this.cacheType = builder.cacheType;
        this.initialCapacity = builder.initialCapacity;
        this.maximumSize = builder.maximumSize;
        this.expireAfterWrite = builder.expireAfterWrite;
    }

    public static final class Builder {
        private String cacheType;
        private Integer initialCapacity;
        private Long maximumSize;
        private Long expireAfterWrite;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder setCacheType(String cacheType) {
            this.cacheType = cacheType;
            return this;
        }
        public Builder setInitialCapacity(Integer initialCapacity) {
            this.initialCapacity = initialCapacity;
            return this;
        }
        public Builder setMaximumSize(Long maximumSize) {
            this.maximumSize = maximumSize;
            return this;
        }
        public Builder setExpireAfterWrite(Long expireAfterWrite) {
            this.expireAfterWrite = expireAfterWrite;
            return this;
        }

        public CacheParm build() {
            return new CacheParm(this);
        }
    }

    public void setCacheType(String cacheType) {
        this.cacheType = cacheType;
    }

    public void setInitialCapacity(Integer initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    public void setMaximumSize(Long maximumSize) {
        this.maximumSize = maximumSize;
    }

    public void setExpireAfterWrite(Long expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
    }

    public String getCacheType() {
        return cacheType;
    }

    public Integer getInitialCapacity() {
        return initialCapacity;
    }

    public Long getMaximumSize() {
        return maximumSize;
    }

    public Long getExpireAfterWrite() {
        return expireAfterWrite;
    }
}
