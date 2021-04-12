package com.haibao.admin.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数据源非公共属性值
 * </p>
 *
 * @author jobob
 * @since 2020-02-25
 */
@ApiModel("数据源特定表类型对应的属性模版定义  持久层实体")
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class TDsJsonField{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 关联数据源定义表t_ds
     */
    @ApiModelProperty(value = "关联数据源定义表t_ds主键")
    private Long dsId;

    /**
     * 属性值
     */
    @ApiModelProperty(value = "属性json存储")
    private String jsonValue;


}
