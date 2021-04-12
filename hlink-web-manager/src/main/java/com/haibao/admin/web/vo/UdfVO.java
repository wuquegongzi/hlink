package com.haibao.admin.web.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自定义函数信息定义表
 * </p>
 *
 * @author jobob
 * @since 2020-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UdfVO extends BaseVO {

    private Long id;

    /**
     * 函数名
     */
    @TableField(value = "udf_name")
    private String udfName;

    /**
     * 加载的class
     */
    @TableField(value = "udf_class")
    private String udfClass;

    /**
     * 存储路径
     */
    @TableField(value = "udf_path")
    private String udfPath;

    /**
     * 函数描述
     */
    @TableField(value = "udf_desc")
    private String udfDesc;

}
