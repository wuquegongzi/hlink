package com.haibao.admin.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <p>
 * 自定义函数信息定义表
 * </p>
 *
 * @author zc
 * @since 2020-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_udf")
@ApiModel(value = "自定义函数对象",description = "持久层实体")
public class TUdf extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 函数名
     */
    @TableField(value = "udf_name")
    @ApiModelProperty(value = "函数名")
    @NotBlank(message = "函数名不能为空")
    @Size(min = 1,max = 255,message = "函数名长度最大不能超过255")
    private String udfName;


    /**
     * 加载的class
     */
    @TableField(value = "udf_class")
    @ApiModelProperty(value = "加载的class")
    @Size(max = 255,message = "加载class的长度最大不能超过255")
    private String udfClass;

    /**
     * 存储路径
     */
    @TableField(value = "udf_path")
    @ApiModelProperty(value = "存储路径")
    @Size(max = 255,message = "存储路径长度最大不能超过255")
    private String udfPath;

    /**
     * 函数描述
     */
    @TableField(value = "udf_desc")
    @ApiModelProperty(value = "函数描述")
    private String udfDesc;


    /**
     * jar包名称
     */
    @TableField(value = "jar_name")
    @ApiModelProperty(value = "jar包名称")
    @Size(max = 255,message = "jar包名称长度最大不能超过255")
    private String jarName;


    /**
     * 自定义函数类型 SCALA ,TABLE,AGGREGATE,TABLEAGG
     */
    @TableField(value = "udf_type")
    @ApiModelProperty(value = "自定义函数类型 SCALA ,TABLE,AGGREGATE,TABLEAGG")
    private String udfType;


    /**
     * 删除标记(0:保存 1:已删除)
     */
    @TableField(value = "delete_flag")
    @ApiModelProperty(value = "删除标记(0:保存 1:已删除)")
    private Integer deleteFlag;
}
