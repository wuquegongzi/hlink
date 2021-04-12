package com.haibao.admin.config.flink;

import com.nextbreakpoint.flinkclient.api.FlinkApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
*  @description: 用来获取FlinkApi的工具类
*  @date: 2020/2/27
**/
@Component
public class FlinkApiClient {

    @Value("${flink.rest.connectTimeOut:3000}")
    private Long connectTimeOut;

    @Value("${flink.rest.writeTimeOut:6000}")
    private Long writeTimeOut;

    @Value("${flink.rest.readTimeOut:2000}")
    private Long readTimeOut;

    @Value("${flink.rest.debug:false}")
    private Boolean debug;

    /**
     * @author: c.zh
     * @param url
     * @return
     */
    public FlinkApi getFlinkApi(String url){

        if(!(url.startsWith("https://") || url.startsWith("http://"))){
            url = "http://".concat(url);
        }

        //创建Flink客户端
        FlinkApi flinkApi=new FlinkApi();

        //配置服务器的主机和端口
        flinkApi.getApiClient().setBasePath(url);

        //配置套接字超时
        flinkApi.getApiClient().getHttpClient().setConnectTimeout(connectTimeOut, TimeUnit.MILLISECONDS);
        flinkApi.getApiClient().getHttpClient().setWriteTimeout(writeTimeOut, TimeUnit.MILLISECONDS);
        flinkApi.getApiClient().getHttpClient().setReadTimeout(readTimeOut, TimeUnit.MILLISECONDS);

        //（可选）启用调试
        flinkApi.getApiClient().setDebugging(debug);

        return flinkApi;
    }
}
