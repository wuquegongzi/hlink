package com.haibao.admin.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @author c.zh
 * @since 2020-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "jar包资源",description = "持久层实体")
public class TRes extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 集群ID
     */
    @ApiModelProperty(value = "集群ID")
    private Long clusterId;

    /**
     * 资源原始名称
     */
    @ApiModelProperty(value = "资源原始名称")
    private String resName;

    /**
     * 资源类型 0-jar
     */
    @ApiModelProperty(value = "资源类型 0:jar")
    private  Integer resType;

    /**
     * 资源新名称
     */
    @ApiModelProperty(value = "资源新名称")
    private String resUname;

    /**
     * 文件大小
     */
    @ApiModelProperty(value = "文件大小")
    private Long resSize;

    /**
     * 辅助字段
     */
    @TableField(exist = false)
    private String resSizeStr;


    /**
     * 启动类
     */
    @ApiModelProperty(value = "启动类")
    private String entryClass;

    /**
     * jar包状态 0:jar包缺失 1:正常
     */
    @ApiModelProperty(value = "资源状态")
    private Integer status;


}
