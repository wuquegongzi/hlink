package com.haibao.flink.enums;

/**
 *  @author: ml.c
 *  @Date: 2020/1/8 2:32 下午
 *  @Description: 检查点模式
 */
public enum CheckpointModeEnum {

    /**
     * 一次性语义
     */
    EXACTLY_ONCE(0,"EXACTLY_ONCE"),
    /**
     * AT_LEAST_ONCE
     */
    AT_LEAST_ONCE(1,"AT_LEAST_ONCE");

    int code;
    String mode;

    CheckpointModeEnum(int code, String mode){
        this.code = code;
        this.mode = mode;
    }

    public int getCode() {
        return code;
    }

    public String getMode() {
        return mode;
    }
}
