package com.haibao.flink.utils;


import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.functions.AggregateFunction;
import org.apache.flink.table.functions.ScalarFunction;
import org.apache.flink.table.functions.TableAggregateFunction;
import org.apache.flink.table.functions.TableFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
*  @author: c.zh
*  @description: 自定义函数注册工具类
*  @date: 2020/3/17
**/

public class UdfUtil {

    private static final Logger logger = LoggerFactory.getLogger(UdfUtil.class);

    private static final String SCALAR="SCALAR";
    private static final String TABLE="TABLE";
    private static final String AGGREGATE="AGGREGATE";
    private static final String TABLEAGG="TABLEAGG";


    /**
     * TABLE|SCALA|AGGREGATE
     * 注册UDF到table env
     */
    public static void registerUDF(String type, String classPath, String funcName, TableEnvironment tableEnv, ClassLoader classLoader){
        if(SCALAR.equalsIgnoreCase(type)){
            registerScalaUDF(classPath, funcName, tableEnv, classLoader);
        }else if(TABLE.equalsIgnoreCase(type)){
            registerTableUDF(classPath, funcName, tableEnv, classLoader);
        }else if(AGGREGATE.equalsIgnoreCase(type)){
            registerAggregateUDF(classPath, funcName, tableEnv, classLoader);
        }else if(TABLEAGG.equalsIgnoreCase(type)){
            registerTableAggregateUDF(classPath, funcName, tableEnv, classLoader);
        }else{
            throw new RuntimeException("not support of UDF which is not in (TABLE, SCALA, AGGREGATE)");
        }

    }

    /**
     * 注册自定义方法到env上
     * @param classPath
     * @param funcName
     * @param tableEnv
     */
    public static void registerScalaUDF(String classPath, String funcName, TableEnvironment tableEnv, ClassLoader classLoader){
        try{
            ScalarFunction udfFunc = Class.forName(classPath, false, classLoader)
                    .asSubclass(ScalarFunction.class).newInstance();
            tableEnv.registerFunction(funcName, udfFunc);
            logger.info("register scala function:{} success.", funcName);
        }catch (Exception e){
            logger.error("{}", e);
            throw new RuntimeException("register UDF exception:", e);
        }
    }

    /**
     * 注册自定义TABLEFFUNC方法到env上
     *
     * @param classPath
     * @param funcName
     * @param tableEnv
     */
    public static void registerTableUDF(String classPath, String funcName, TableEnvironment tableEnv, ClassLoader classLoader){
        try {
            TableFunction udfFunc = Class.forName(classPath, false, classLoader)
                    .asSubclass(TableFunction.class).newInstance();
            if(tableEnv instanceof StreamTableEnvironment){
                ((StreamTableEnvironment)tableEnv).registerFunction(funcName, udfFunc);
            }else if(tableEnv instanceof BatchTableEnvironment){
                ((BatchTableEnvironment)tableEnv).registerFunction(funcName, udfFunc);
            }else{
                throw new RuntimeException("no support registerTableUDF,tableEnvironment class for " + tableEnv.getClass().getName());
            }

            logger.info("register table function:{} success.", funcName);
        }catch (Exception e){
            logger.error("{}", e);
            throw new RuntimeException("register Table UDF exception:", e);
        }
    }

    /**
     * 注册自定义Aggregate FUNC方法到env上
     *
     * @param classPath
     * @param funcName
     * @param tableEnv
     */
    public static void registerAggregateUDF(String classPath, String funcName, TableEnvironment tableEnv, ClassLoader classLoader) {
        try {
            AggregateFunction udfFunc = Class.forName(classPath, false, classLoader)
                    .asSubclass(AggregateFunction.class).newInstance();
            if (tableEnv instanceof StreamTableEnvironment) {
                ((StreamTableEnvironment) tableEnv).registerFunction(funcName,  udfFunc);
            } else if (tableEnv instanceof BatchTableEnvironment) {
                ((BatchTableEnvironment) tableEnv).registerFunction(funcName,  udfFunc);
            } else {
                throw new RuntimeException("no support registerAggregateUDF ,tableEnvironment class for " + tableEnv.getClass().getName());
            }

            logger.info("register Aggregate function:{} success.", funcName);
        } catch (Exception e) {
            logger.error("{}", e);
            throw new RuntimeException("register Aggregate UDF exception:", e);
        }
    }

    /**
     * 注册自定义Aggregate FUNC方法到env上
     *
     * @param classPath
     * @param funcName
     * @param tableEnv
     */
    public static void registerTableAggregateUDF(String classPath, String funcName, TableEnvironment tableEnv, ClassLoader classLoader) {
        try {
            TableAggregateFunction udfFunc = Class.forName(classPath, false, classLoader)
                    .asSubclass(TableAggregateFunction.class).newInstance();
            if(tableEnv instanceof StreamTableEnvironment) {
                ((StreamTableEnvironment) tableEnv).registerFunction(funcName, udfFunc);
            }else {
                throw new RuntimeException("no support registerTableUDF,tableEnvironment class for " + tableEnv.getClass().getName());
            }
            logger.info("register TableAggregate function:{} success.", funcName);
        } catch (Exception e) {
            logger.error("{}", e);
            throw new RuntimeException("register TableAggregate UDF exception:", e);
        }
    }

    public static URLClassLoader loadExtraJar(URL url, URLClassLoader classLoader) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = ReflectionUtils.getDeclaredMethod(classLoader, "addURL", URL.class);

        if(method == null){
            throw new RuntimeException("can't not find declared method addURL, curr classLoader is " + classLoader.getClass());
        }

        method.setAccessible(true);
        method.invoke(classLoader, url);

        return classLoader;
    }
}