package com.haibao.flink.model;

import java.io.Serializable;

/**
 * <p>
 * 自定义函数信息定义表
 * </p>
 *
 * @author jobob
 * @since 2020-02-25
 */
public class Udf implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 函数名
     */
    private String udfName;

    /**
     * 名称
     */
    private String name;

    /**
     * 加载的class
     */
    private String udfClass;

    /**
     * 存储路径
     */
    private String udfPath;

    /**
     * 函数描述
     */
    private String udfDesc;

    /**
     * jar包名称
     */
    private String jarName;


    /**
     * 自定义函数类型 SCALA ,TABLE,AGGREGATE,TABLEAGG
     */
    private String udfType;


    public String getUdfName() {
        return udfName;
    }

    public void setUdfName(String udfName) {
        this.udfName = udfName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUdfClass() {
        return udfClass;
    }

    public void setUdfClass(String udfClass) {
        this.udfClass = udfClass;
    }

    public String getUdfPath() {
        return udfPath;
    }

    public void setUdfPath(String udfPath) {
        this.udfPath = udfPath;
    }

    public String getUdfDesc() {
        return udfDesc;
    }

    public void setUdfDesc(String udfDesc) {
        this.udfDesc = udfDesc;
    }

    public String getJarName() {
        return jarName;
    }

    public void setJarName(String jarName) {
        this.jarName = jarName;
    }

    public String getUdfType() {
        return udfType;
    }

    public void setUdfType(String udfType) {
        this.udfType = udfType;
    }
}
