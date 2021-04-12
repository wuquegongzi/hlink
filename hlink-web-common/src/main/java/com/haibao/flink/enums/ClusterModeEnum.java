package com.haibao.flink.enums;

/**
 *  @author: ml.c
 *  @Date: 2020/1/8 10:39 上午
 *  @Description: 部署模式
 */
public enum ClusterModeEnum {

    /**
     * 本地模式
     */
    local(0),
    /**
     * 独立集群模式
     */
    standalone(1),
    /**
     * yarn 集群模式
     */
    yarn(2),
    /**
     * yarn 独立集群模式
     */
    yarnPer(3);

    private int type;

    ClusterModeEnum(int type){
        this.type = type;
    }

    public int getType(){
        return this.type;
    }
}
