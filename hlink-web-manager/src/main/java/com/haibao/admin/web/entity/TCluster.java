package com.haibao.admin.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2020-02-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_cluster")
@ApiModel(value = "集群",description = "持久层实体")
public class TCluster extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 集群名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "集群名称")
    private String name;

    /**
     * 集群类型,枚举,
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "集群类型,枚举")
    private Integer type;

    /**
     * 集群地址
     */
    @TableField(value = "address")
    @ApiModelProperty(value = "集群地址")
    private String address;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 删除标识，逻辑删除。0 未启用,1 启用,-1删除
     */
    @TableField(value = "del_flag")
    @ApiModelProperty(value = "删除标识，逻辑删除。0 未启用,1 启用,-1删除")
    private Integer delFlag;


}
