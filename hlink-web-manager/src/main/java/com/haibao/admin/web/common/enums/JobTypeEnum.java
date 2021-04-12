package com.haibao.admin.web.common.enums;

import lombok.Getter;

/**
 * Created by baoyu on 2020/2/25.
 * Describe
 */
public enum JobTypeEnum {
    /**
     * SQL 型
     */
    SQL(0),
    /**
     * 自定义 jar
     */
    JAR(1);

    @Getter
    private Integer type;

    JobTypeEnum(Integer type){
        this.type=type;
    }

    public static JobTypeEnum getById(Integer type) {
        for (JobTypeEnum element : values()) {
            if (element.getType().intValue() == type.intValue()) {
                //获取指定的枚举
                return element;
            }
        }
        return null;
    }
}
