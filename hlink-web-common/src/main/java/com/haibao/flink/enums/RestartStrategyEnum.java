package com.haibao.flink.enums;

/**
 * @ClassName RestartStrategyEnum
 * @Description 重启策略
 * @Author ml.c
 * @Date 2020/3/9 4:24 下午
 * @Version 1.0
 */
public enum RestartStrategyEnum {

    NO_RESTART(0,"No_Restart","无重启 (No Restart) 策略"),
    FIXED_DELAY(1,"Fixed_Delay","固定间隔 (Fixed Delay) 重启策略"),
    FAILURE_RATE(2,"Failure_Rate","失败率 (Failure Rate) 重启策略");

    int code;
    String label;
    String remark;

    RestartStrategyEnum(int code,String label,String remark){
        this.code = code;
        this.label = label;
        this.remark = remark;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public String getRemark() {
        return remark;
    }
}
