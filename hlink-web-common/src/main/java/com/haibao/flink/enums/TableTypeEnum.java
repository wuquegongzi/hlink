package com.haibao.flink.enums;

/**
 *  @author: ml.c
 *  @Date: 2020/1/8 2:32 下午
 *  @Description:
 */
public enum TableTypeEnum {
    //源表
    SOURCE_KAFKA(0,"kafka","source"),

    //目的表
    SINK_KAFKA(0,"kafka","sink"),
    SINK_MYSQL(1,"mysql","sink"),
    SINK_ORACLE(2,"oracle","sink"),
    SINK_HBASE(3,"hbase","sink"),

    //维表
    SIDE_KAFKA(0,"kafka","side"),
    SIDE_MYSQL(1,"mysql","side"),
    SIDE_ORACLE(2,"oracle","side"),
    SIDE_HTTP(3,"http","side");

    int code;
    String type;
    String dsType;

    TableTypeEnum(int code,String type,String dsType){

        this.code = code;
        this.type = type;
        this.dsType = dsType;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getDsType() {
        return dsType;
    }
}
