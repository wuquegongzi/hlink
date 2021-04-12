package com.haibao.flink.enums;

/**
 * @ClassName DsTypeEnum
 * @Description 枚举类 ：功能类型
 * @Author ml.c
 * @Date 2020/2/26 5:10 下午
 * @Version 1.0
 */
public enum DsTypeEnum {

    /**
     * 源表
     */
    SOURCE(0,"source","源表"),
    /**
     * 目标表
     */
    SINK(1,"sink","目标表"),
    /**
     * 维表
     */
    SIDE(2,"side","维表"),
    /**
     * 自定义函数
     */
    UDF(3,"udf","自定义函数");

    private int code;
    private String type;
    private String desc;

    DsTypeEnum(int code,String type, String desc) {
        this.code = code;
        this.type = type;
        this.desc = desc;
    }

    DsTypeEnum() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
