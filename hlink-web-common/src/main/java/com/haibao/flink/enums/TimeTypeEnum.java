package com.haibao.flink.enums;

/**
 * 时间类型
 */
public enum TimeTypeEnum {

    INGESTIONTIME(0,"IngestionTime"),
    PROCESSINGTIME(1,"ProcessingTime"),
    EVENTTIME(2,"EventTime");

    int code;
    String type;

    TimeTypeEnum(int code,String type){

        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
