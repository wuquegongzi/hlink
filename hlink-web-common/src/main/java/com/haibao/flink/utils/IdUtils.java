package com.haibao.flink.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/*
 * @Author ml.c
 * @Description
 * @Date 18:50 2020-03-24
 **/
public class IdUtils {

    static Snowflake snowflake = IdUtil.getSnowflake(1, 1);

    public static Long getId(){
        return  snowflake.nextId();
    }

}
