package com.haibao.flink.connectors.side.http;

import com.haibao.flink.enums.SideConstants;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author: c.zh
 * @description: 异步http请求工具类
 * @date: 2020/3/9
 **/
public class HttpAsyUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpAsyUtils.class);

    private static CloseableHttpAsyncClient closeableHttpAsyncClient;

    /**
     * 默认超时时间:10分钟，获取连接的timeout和获取socket数据的timeout都是10分钟
     */
    private static int timeOutMilliSecond = 60 * 10 * 1000;

    private static final int MAX_TOTAL = 1000;

    /**
     * 连接池链接耗尽等待时间
     */
    private static final int CONNECTION_REQUEST_TIMEOUT = 2000;

    private static final int MAX_PER_ROUTE = 10;

    private static final Object SYNC_LOCK = new Object();

    private static HttpAsyncClientBuilder httpBuilder;

//    private static Boolean HTTP_PROXY_ENABLE = Boolean.valueOf(ProperitesUtil.getPropertyValue("http.proxy.enable"));
//
//    private static String HTTP_PROXY_HOST = ProperitesUtil.getPropertyValue("http.proxy.host");
//
//    private static Integer HTTP_PROXY_PORT = Integer.valueOf(ProperitesUtil.getPropertyValue("http.proxy.port"));
//
//    private static  String HTTP_BODY_FIELD = ProperitesUtil.getPropertyValue("http.body.field");

    static {

        //配置io线程
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().
                setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .setSoKeepAlive(true)
                .build();
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            logger.error(e.getMessage(), e);
        }
        // 初始化连接池管理器，设置http的状态参数
        PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(ioReactor);
        //设置连接池大小
        connectionManager.setMaxTotal(MAX_TOTAL);
        connectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);

        httpBuilder = HttpAsyncClients.custom();
        httpBuilder.setConnectionManager(connectionManager);
    }

    /**
     * 获取连接
     *
     * @return CloseableHttpClient
     */
    private static CloseableHttpAsyncClient getHttpClient() {
        if (closeableHttpAsyncClient == null) {
            synchronized (SYNC_LOCK) {
                if (closeableHttpAsyncClient == null) {
                    closeableHttpAsyncClient = createHttpClient();
                }
            }
        }
        return closeableHttpAsyncClient;
    }


    /**
     * 创建连接
     *
     * @return CloseableHttpClient
     */
    private static CloseableHttpAsyncClient createHttpClient() {
        Builder builder = RequestConfig.custom().setSocketTimeout(timeOutMilliSecond).setConnectTimeout(timeOutMilliSecond).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);
        RequestConfig defaultRequestConfig = builder.build();
        HttpAsyncClientBuilder httpClientBuilder = httpBuilder.setDefaultRequestConfig(defaultRequestConfig);

        // 添加代理配置
//        if(HTTP_PROXY_ENABLE) {
//            httpClientBuilder = httpClientBuilder.setProxy(new HttpHost(HTTP_PROXY_HOST, HTTP_PROXY_PORT));
//        }

        CloseableHttpAsyncClient httpClient = httpClientBuilder.build();
        httpClient.start();
        return httpClient;
    }

    /**
     * GET方式调用
     *
     * @param url url
     * @return String
     */
    public static void get(String url, Integer timeout, FutureCallback<HttpResponse> callback,Map<String, String> queries) throws Exception {
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        HttpGet httpGet = new HttpGet(sb.toString());
        initConfig(httpGet, timeout);
        getHttpClient().execute(httpGet, callback);
        logger.info("[HttpUtils-get:]url:{},paramMap:{}", url,queries);
    }

    /**
     * GET方式调用
     *
     * @param url url
     * @return String
     */
    public static void get(String url, Integer timeout, FutureCallback<HttpResponse> callback) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        initConfig(httpGet, timeout);
        getHttpClient().execute(httpGet, callback);
        logger.info("[HttpUtils-get:]url:{}", url);
    }


    /**
     * POST方式调用-参数MAP
     *
     * @param url      url
     * @param paramMap 参数map
     * @return string
     * @throws Exception
     */
    public static void post(String url, Map<String, String> paramMap, Integer timeout, FutureCallback<HttpResponse> callback,String charset) throws Exception {
        CloseableHttpAsyncClient httpClient = getHttpClient();
        logger.info("[HttpUtils-post-request:]url:{},paramMap:{}", url, paramMap);
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramsPair = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            paramsPair.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        initConfig(httpPost, timeout);
        httpPost.setEntity(new UrlEncodedFormEntity(paramsPair, charset));
        httpClient.execute(httpPost, callback);
        logger.info("[HttpUtils-post-Map:]url:{},paramMap:{}", url, paramMap);
    }

    /**
     * POST方式调用-字符串
     *
     * @param url         url
     * @param postContent 入参字符串
     * @return string
     * @throws Exception
     */
    public static void post(String url, String postContent, Integer timeout, FutureCallback<HttpResponse> callback,String mimeType,String charset) throws Exception {
        logger.info("[HttpUtils-post-request:]url:{},postContent:{}", url, postContent);
        CloseableHttpAsyncClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        initConfig(httpPost, timeout);
        httpPost.setEntity(new StringEntity(postContent,ContentType.create(mimeType,charset)));
        httpClient.execute(httpPost, callback);
        logger.info("[HttpUtils-post-string:]url:{},postContent:{},mimeType:{},charset:{}", url, postContent,mimeType,charset);
    }

    /**
    *  @author: c.zh
    *  @description: 根据methType 请求方式统一出路的方法
    *  @date: 2020/3/9
    **/
    public static void execute(String url, Object param, Integer timeout, FutureCallback<HttpResponse> callback,String methodType,String charset,String mimeType) throws Exception {
        if(SideConstants.METHOD_POST.equals(methodType)){
            if(param instanceof Map){
                Map<String, String> paramMap=(Map)param;
                post(url,paramMap,timeout,callback,charset);
            }else if(param instanceof String){
                String postContent=(String)param;
                post(url,postContent,timeout,callback,mimeType,charset);
            }
        }else if(SideConstants.METHOD_GET.equals(methodType)){
            Map<String, String> paramMap=(Map)param;
            get(url,timeout,callback,paramMap);
        }
    }


    public static void initConfig(HttpGet httpGet, Integer timeout) {
        if (null == timeout || timeout <= 0) {
            timeout = timeOutMilliSecond;
        }
        Builder builder = RequestConfig.custom();
        // 设置请求和传输超时时间
        builder.setSocketTimeout(timeout).setConnectTimeout(timeout).setConnectionRequestTimeout(timeout);
        RequestConfig requestConfig = builder.build();
        httpGet.setConfig(requestConfig);
    }

    public static void initConfig(HttpPost httpPost, Integer timeout) {
        if (null == timeout || timeout <= 0) {
            timeout = timeOutMilliSecond;
        }
        Builder builder = RequestConfig.custom();
        // 设置请求和传输超时时间
        builder.setSocketTimeout(timeout).setConnectTimeout(timeout).setConnectionRequestTimeout(timeout);
        RequestConfig requestConfig = builder.build();
        httpPost.setConfig(requestConfig);
    }
}
