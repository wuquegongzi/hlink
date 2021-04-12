package com.haibao.admin.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * <p>
 * 数据源的schema解析后的属性
 * </p>
 *
 * @author jobob
 * @since 2020-02-25
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@ApiModel("数据源列投影  视图实体")
public class DsSchemaColumnVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "虚拟id，采用雪花算法")
    @NotNull
    private String virtualId;

    @ApiModelProperty(value = "虚拟父级id，默认顶级为0")
    @NotNull
    private String virtualPid;

    /**
     * 关联数据源ID，指定所属数据源
     */
    @ApiModelProperty(value = "关联数据源ID，指定所属数据源")
    private Long dsId;

    /**
     * 字段属性名
     */
    @ApiModelProperty(value = "字段属性名")
    @NotEmpty(message = "列投影 属性名不可为空")
    @Size(max = 20,message = "列投影 属性名长度不可超过20")
    private String name;

    /**
     * 字段属性类型
     */
    @ApiModelProperty(value = "Flink SQL属性类型")
    @NotEmpty(message = "列投影 FlinkSQL属性类型不可为空")
    private String flinkType;

    @ApiModelProperty(value = "基础数据类型")
    @NotEmpty(message = "列投影 基础数据类型不可为空")
    private String  basicType;

    /**
     * 是否是连接key
     */
    @ApiModelProperty(value = "是否是连接key,只有维表类型需要该字段")
    private Integer joinKey;

    /**
     * 是否是事件时间
     */
    @ApiModelProperty(value = "是否是事件时间")
    private Integer eventTime;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String comment;

    @ApiModelProperty(value = "层级")
    private Integer level;

    /**
     * 备用字段1
     */
    @ApiModelProperty(value = "备用字段1")
    private String res1;

    @ApiModelProperty(value = "备用字段2")
    private String res2;

    @ApiModelProperty(value = "备用字段3")
    private String res3;

    @ApiModelProperty(value = "备用字段4")
    private String res4;

    @ApiModelProperty(value = "备用字段5")
    private String res5;

}
