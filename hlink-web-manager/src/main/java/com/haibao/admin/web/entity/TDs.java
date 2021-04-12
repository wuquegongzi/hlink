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
 * 数据源表
 * </p>
 *
 * @author jobob
 * @since 2020-02-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_ds")
@ApiModel("数据源  持久层实体")
public class TDs extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据源名称
     */
    @ApiModelProperty(value = "数据源名称")
    @TableField(value = "ds_name")
    private String dsName;

    /**
     * 数据源类型 0-源表 1-结果表 2-维表
     */
    @ApiModelProperty(value = "数据源类型 0-源表 1-结果表 2-维表")
    @TableField(value = "ds_type")
    private String dsType;

    /**
     * 表类型，见字典表 dc_type_source、side、slink
     */
    @ApiModelProperty(value = "表类型，见字典表 dc_type_source、side、slink")
    @TableField(value = "table_type")
    private String tableType;

    /**
     * schema_type,默认0 json
     */
    @ApiModelProperty(value = "schema_type,默认0 json")
    @TableField(value = "schema_type")
    private Integer schemaType;

    /**
     * 结构类型，单一结构-0或者多层嵌套结构-1
     */
    @ApiModelProperty(value = "结构类型，单一结构-0或者多层嵌套结构-1")
    @TableField(value = "structure_type")
    private Integer structureType;

    /**
     * schema文件描述
     */
    @ApiModelProperty(value = "schema文件内容")
    @TableField(value = "schema_file")
    private String schemaFile;

    /**
     * 别名，用于SQL
     */
    @ApiModelProperty(value = "别名，用于SQL加工")
    @TableField(value = "table_name")
    private String tableName;

    /**
     * 是否启用ddl,默认0 启用。1-不启用。目前 维表不使用ddl。保留该字段
     */
    @ApiModelProperty(value = "是否启用ddl,默认0 启用。1-不启用。目前 维表不使用ddl。保留该字段")
    @TableField(value = "ddl_enable")
    private Integer ddlEnable;

    /**
     * ddl建表语句，维表不需要
     */
    @ApiModelProperty(value = "ddl建表语句")
    @TableField(value = "ds_ddl")
    private String dsDdl;

    /**
     * 版本号，保留字段
     */
    @ApiModelProperty(value = "版本号")
    @TableField(value = "ds_version")
    private Integer dsVersion;


}
