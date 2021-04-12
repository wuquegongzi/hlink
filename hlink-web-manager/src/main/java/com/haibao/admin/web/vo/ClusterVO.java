package com.haibao.admin.web.vo;

import lombok.Data;

/**
 * Created by baoyu on 2020/2/19.
 * Describe 集群信息包装类
 */
@Data
public class ClusterVO extends BaseVO {
    /**
     * 集群ID
     * */
    private Long id;
    /**
     * 集群名称
     */
    private String name;

    /**
     * 集群类型,枚举,
     */
    private Integer type;

    /**
     * 集群地址
     */
    private String address;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标识，逻辑删除。0 未启用,1 启用,-1删除
     */
    private Integer delFlag;

    /**
     * taskManger数量
     * */
    private Integer taskManagerCount;
    /**
     * slot总数量
     * */
    private Integer slotTotal;
    /**
     * slot可用数量
     * */
    private Integer slotAvailableCount;
    /**
     * slot运行中数量
     * */
    private Integer slotRunningCount;
    /**
     * 作业 完成数量
     * */
    private Integer jobCompleteCount;
    /**
     * 作业 取消数量
     * */
    private Integer jobCancelCount;
    /**
     * 作业 失败数量
     * */
    private Integer jobFailureCount;
}
