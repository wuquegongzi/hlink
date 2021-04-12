package com.haibao.admin.utils;

import cn.hutool.core.net.NetUtil;

import javax.servlet.http.HttpServletRequest;

/*
 * @Author ml.c
 * @Description //ip工具类
 * @Date 15:40 2020-03-31
 **/
public class IpUtil {

    /**
     * 获取请求用户的IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        if(null == request){
            return "127.0.0.1";
        }

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    public static Long getIp2Long(HttpServletRequest request) {

        return NetUtil.ipv4ToLong(getIpAddr(request));
    }
}
