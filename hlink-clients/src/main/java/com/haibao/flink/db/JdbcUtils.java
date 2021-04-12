package com.haibao.flink.db;

import com.haibao.flink.utils.PwdAuthUtils;

import com.haibao.flink.utils.YmlPropUtils;
import java.sql.*;

/**
 * @ClassName DBUtils
 * @Description JDBC连接工具类，杀鸡焉用牛刀
 * @Author ml.c
 * @Date 2020/3/3 7:32 下午
 * @Version 1.0
 */
public class JdbcUtils {

    public static void main(String[] args) throws Exception {
        JdbcUtils jdbcUtils = new JdbcUtils();
        Connection conn = jdbcUtils.getCon("dev");

        System.out.println(conn);
    }

    /**
     * 建立一个连接
     * @return
     * @throws Exception
     */
    public Connection getCon(String profilesActive) throws Exception{

        Connection con = null;
        YmlPropUtils ymlPropUtils = YmlPropUtils.getInstance(profilesActive);
        boolean openEncryption = Boolean.valueOf((String)ymlPropUtils.getProperty("spring.datasource.open-encryption"));
        String driverClassName = (String) ymlPropUtils.getProperty("spring.datasource.driver-class-name");
        String url = (String) ymlPropUtils.getProperty("spring.datasource.url");
        String username = (String) ymlPropUtils.getProperty("spring.datasource.username");
        String password = (String) ymlPropUtils.getProperty("spring.datasource.password");
        if(openEncryption){
            PwdAuthUtils pwdAuthUtils = new PwdAuthUtils();
            password = pwdAuthUtils.decrypt(password);
        }

        Class.forName(driverClassName);
        con = DriverManager.getConnection(url, username, password);

        return con;
    }

}

