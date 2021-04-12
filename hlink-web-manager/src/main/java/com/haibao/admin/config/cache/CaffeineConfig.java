package com.haibao.admin.config.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.haibao.admin.utils.TDFileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;


@Configuration
//@EnableCaching //开启缓存
public class CaffeineConfig {

    public static final int DEFAULT_MAXSIZE = 10000;
    public static final int DEFAULT_TTL = 60;

    @Bean
    public Cache<String, Object> dictCache() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterWrite(DEFAULT_TTL, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(DEFAULT_MAXSIZE)
                .recordStats()
                .removalListener(((key, value, cause) -> {
                    System.out.println(cause);
                }))
                .build();
    }

/*    Caffeine中被淘汰的原因有很多种:

    EXPLICIT: 这个原因是，用户造成的，通过调用remove方法从而进行删除。
    REPLACED: 更新的时候，其实相当于把老的value给删了。
    COLLECTED: 用于我们的垃圾收集器，也就是我们上面减少的软引用，弱引用。
    EXPIRED： 过期淘汰。
    SIZE: 大小淘汰，当超过最大的时候就会进行淘汰。*/

    @Bean
    public Cache<String, Object> dsCache() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterWrite(DEFAULT_TTL, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(DEFAULT_MAXSIZE)
                .build();
    }


    @Bean("flinkKeywordsCache")
    public Cache<String, Integer> flinkKeywordsCache() {

        Cache<String, Integer> cache=
                Caffeine.newBuilder()
                        // 设置最后一次写入或访问后经过固定时间过期
                        .expireAfterWrite(3600, TimeUnit.DAYS)
                        // 初始的缓存空间大小
                        .initialCapacity(100)
                        // 缓存的最大条数
                        .maximumSize(DEFAULT_MAXSIZE)
                        .build();

        //加载keywords
        String keywordsPath = "templates/doc/keywords.txt";
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(keywordsPath);

        try {
            String keywordsStr = TDFileUtils.readFile(inputStream);
            String[] keywords = keywordsStr.split(",");
            for (int i = 0; i < keywords.length; i++) {
                cache.put(keywords[i].trim(),i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cache;
    }

    /**
     * 配置缓存管理器
     *
     * @return 缓存管理器
     */
//    @Bean("caffeineCacheManager")
//    public CacheManager cacheManager() {
//        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
//        cacheManager.setCaffeine(Caffeine.newBuilder()
//                // 设置最后一次写入或访问后经过固定时间过期
//                .expireAfterAccess(DEFAULT_TTL, TimeUnit.SECONDS)
//                // 初始的缓存空间大小
//                .initialCapacity(100)
//                // 缓存的最大条数
//                .maximumSize(DEFAULT_MAXSIZE));
//        return cacheManager;
//    }

}
