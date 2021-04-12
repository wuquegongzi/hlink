package com.haibao.admin.web.vo;

import com.haibao.admin.web.vo.templete.JsonField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 数据源非公共属性值
 * </p>
 *
 * @author jobob
 * @since 2020-02-25
 */
@ApiModel("数据源特定表类型对应的属性模版定义  视图实体")
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class DsJsonFieldVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;


    /**
     * 关联数据源定义表t_ds
     */
    @ApiModelProperty(value = "关联数据源定义表t_ds主键")
    private Long dsId;

    /**
     * 属性值
     */
    @ApiModelProperty(value = "属性值")
    @NotNull
    private List<JsonField> jsonValue;


}
