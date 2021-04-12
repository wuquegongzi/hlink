package com.haibao.tests;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author: c.zh
 * @description: TODO
 * @date: 2020/3/24
 **/

public class TestCache {

    public static void main(String[] args) {
        try {
            com.github.benmanes.caffeine.cache.CacheLoader loader = key -> createExpensiveGraph(key);
            AsyncLoadingCache<String, Object> loadingCache = Caffeine.newBuilder()
                    .maximumSize(10_000)
                    .refreshAfterWrite(2, TimeUnit.SECONDS)
                    .buildAsync(loader);

            CompletableFuture<Object> tt = loadingCache.get("1");
            System.out.println(tt.get().toString());
            Thread.sleep(10*1000);
            CompletableFuture<Object> tt1 = loadingCache.get("1");
            System.out.println(tt1.get().toString());
            CompletableFuture<Object> tt2 = loadingCache.get("1");
            System.out.println(tt2.get().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static Object createExpensiveGraph(Object key) {
        SecureRandom r = new SecureRandom();
        return r.nextInt(10);
    }


}
