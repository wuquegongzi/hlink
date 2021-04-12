package com.haibao.flink.enums;

/**
 *  @author: ml.c
 *  @Date: 2020/1/8 2:32 下午
 *  @Description: 检查点清除模式
 */
public enum CheckpointCleanupModeEnum {

    /**
     * RETAIN_ON_CANCELLATION
     */
    RETAIN_ON_CANCELLATION(0,"RETAIN_ON_CANCELLATION"),
    /**
     * DELETE_ON_CANCELLATION
     */
    DELETE_ON_CANCELLATION(1,"DELETE_ON_CANCELLATION");

    int code;
    String mode;

    CheckpointCleanupModeEnum(int code, String mode){
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
