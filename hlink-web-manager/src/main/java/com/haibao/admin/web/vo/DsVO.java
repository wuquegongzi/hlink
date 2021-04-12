package com.haibao.admin.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * 数据源 实体类
 * </p>
 *
 * @author jobob
 * @since 2020-02-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("数据源  视图实体")
public class DsVO extends BaseVO {

    @ApiModelProperty(value = "主键id")
    private Long id;
    /**
     * 数据源名称
     */
    @ApiModelProperty(value = "数据源名称")
    @NotEmpty(message="数据源名称不可为空")
    @Size(max = 255,message = "数据源名称 长度不能超过255")
    private String dsName;

    /**
     * 数据源类型 0-源表 1-结果表 2-维表 4-UDF
     */
    @ApiModelProperty(value = "数据源类型 source-源表 sink-结果表 side-维表 udf-UDF自定义函数")
    @NotNull(message="功能类型必选")
    private String dsType;

    /**
     * 表类型，见字典表 dc_type_source、side、slink
     */
    @ApiModelProperty(value = "表类型，见字典表 dc_type_source、dc_type_side、dc_type_sink")
    @NotNull(message="表类型必选")
    private String tableType;

    /**
     * schema_type,默认0 json
     */
    @ApiModelProperty(value = "schema类型，默认0-json  , 1-avro")
    private Integer schemaType;

    /**
     * 结构类型，单一结构-0或者多层嵌套结构-1
     */
    @ApiModelProperty(value = "结构类型，单一结构-0或者多层嵌套结构-1")
    private Integer structureType;

    /**
     * schema文件
     */
    @ApiModelProperty(value = "schema文件内容")
    private String schemaFile;

    /**
     * 别名，用于SQL
     */
    @ApiModelProperty(value = "表的别名，用于特征加工")
    @NotEmpty(message = "别名不可为空")
    private String tableName;

    /**
     * 是否启用ddl,默认0 启用。1-不启用。目前 维表不使用ddl。保留该字段
     */
    @ApiModelProperty(value = "是否启用ddl,默认0 启用。1-不启用。目前 维表不使用ddl。保留该字段")
    private Integer ddlEnable;

    /**
     * ddl建表语句，维表不需要
     */
    @ApiModelProperty(value = "ddl建表语句，维表不需要")
    private String dsDdl;

    /**
     * 版本号，保留字段
     */
    @ApiModelProperty(value = "版本号，保留字段")
    private Integer dsVersion;

    /**
     * json schema列投影 业务实体对象
     */
    @ApiModelProperty(value = "schema列投影 业务实体对象")
    @Valid
    @NotNull(message = "schema列投影不可为空")
    private List<DsSchemaColumnVO> dsSchemaColumnVOS;

    /**
     * 个性化连接属性值 业务实体对象
     */
    @ApiModelProperty(value = "各个表类型特定属性值 业务实体对象")
    @Valid
    @NotNull(message = "表类型对应的属性值不可为空")
    private DsJsonFieldVO jsonFieldVO;

}
