package com.haibao.admin.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by baoyu on 2019/6/26.
 * http请求类
 */
@Component
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private CloseableHttpClient localHttpClient;

    // 连接池链接耗尽等待时间
    @Value("${flink.rest.connectTimeOut}")
    private int connectionRequestTimeout;

    // 默认超时时间:1分钟，获取连接的timeout和获取socket数据的timeout都是1分钟
    @Value("${flink.rest.readTimeOut}")
    private int timeOutMilliSecond;

    private String charset = "utf-8";

    private int maxTotal = 1000;

    private int maxPerRoute = 10;

    private final static Object SYNC_LOCK = new Object();

    private PoolingHttpClientConnectionManager connectionManager = null;

    private HttpClientBuilder httpBulder = null;

    public HttpUtil() {
        //初始化连接池管理器，设置http的状态参数
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(maxPerRoute);
        httpBulder = HttpClients.custom();
        httpBulder.setConnectionManager(connectionManager);
    }
    private CloseableHttpClient getHttpClient() {
        if (localHttpClient == null) {
            synchronized (SYNC_LOCK) {
                if (localHttpClient == null) {
                    localHttpClient = createHttpClient();
                }
            }
        }
        return localHttpClient;

    }
    private CloseableHttpClient createHttpClient() {
        RequestConfig.Builder builder = RequestConfig.custom().setSocketTimeout(timeOutMilliSecond)
                .setConnectTimeout(timeOutMilliSecond)
                .setConnectionRequestTimeout(connectionRequestTimeout);
        RequestConfig defaultRequestConfig = builder.build();
        HttpClientBuilder httpClientBuilder = httpBulder.setDefaultRequestConfig(defaultRequestConfig);
        CloseableHttpClient httpClient = httpClientBuilder.build();

        return httpClient;
    }

    public String get(String url) throws IOException {
        long start=System.currentTimeMillis();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient().execute(httpget, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, charset);
            EntityUtils.consume(entity);
            return result;
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
            logger.info("http调用{},耗时：{}",url,System.currentTimeMillis()-start);
        }
    }

    public String post(String url, Map<String, String> paramMap) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramsPair = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            paramsPair.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramsPair, charset));

        return execute(httpPost);

    }
    public String postjson(String url, String json) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(new StringEntity(json,charset));
        return execute(httpPost);

    }

    private String execute(HttpPost httpPost) throws IOException {
        long start=System.currentTimeMillis();
        CloseableHttpResponse response=null;
        try {
            CloseableHttpClient httpClient = getHttpClient();
            response = httpClient.execute(httpPost, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, charset);
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
            logger.info("http调用{},耗时：{}",httpPost.getURI(),System.currentTimeMillis()-start);
        }
    }
}
