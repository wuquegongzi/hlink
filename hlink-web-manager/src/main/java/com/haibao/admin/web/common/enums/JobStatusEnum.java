package com.haibao.admin.web.common.enums;

import lombok.Getter;

/**
 * Created by baoyu on 2020/2/11.
 * Describe 作业状态枚举
 * JobDetailsInfo.StateEnum
 */
public enum  JobStatusEnum {
    UNCOMMITTED(0,"未提交","UNCOMMITTED"),
    CREATED(1,"已生成","CREATED"),
    STARTING(2,"启动中","STARTING"),
    RUNNING(3,"运行中","RUNNING"),
    FAILING(4,"失败中","FAILING"),
    FAILED(5,"已失败","FAILED"),
    CANCELLING(6,"停止中","CANCELLING"),
    CANCELED(7,"已停止","CANCELED"),
    FINISHED(8,"已完成","FINISHED"),
    RESTARTING(9,"重启中","RESTARTING"),
    SUSPENDING(10,"暂停中","SUSPENDING"),
    SUSPENDED(11,"已暂停","SUSPENDED"),
    RECONCILING(12,"调度中","RECONCILING");


    @Getter
    private Integer status;

    @Getter
    private String desc;

    @Getter
    private String value;

    JobStatusEnum(Integer status,String desc,String value){
        this.status=status;
        this.desc=desc;
        this.value=value;
    }

    public static JobStatusEnum getById(Integer status) {
        for (JobStatusEnum element : values()) {
            if (element.getStatus().intValue() == status.intValue()) {
                //获取指定的枚举
                return element;
            }
        }
        return null;
    }

    public static Integer getStatusByValue(String value) {
        for (JobStatusEnum element : values()) {
            if (element.getValue().equals(value)) {
                return element.getStatus();
            }
        }
        return null;
    }


}
