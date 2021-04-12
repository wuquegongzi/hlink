package com.haibao.flink.enums;

/**
*  @author: c.zh
*  @description: HTTP Medim 类型枚举
*  @date: 2020/3/10
**/
public enum HttpMedimTypeEnum {

    urlencoded("application/x-www-form-urlencoded"),
    JSON("application/json")/*,XML("application/xml")*/;

    private String type;

    private  HttpMedimTypeEnum(String type){
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
