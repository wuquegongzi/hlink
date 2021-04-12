package com.haibao.admin.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 作业对应的数据源选择结果
 * </p>
 *
 * @author jobob
 * @since 2020-03-05
 */
@ApiModel(value = "作业与数据源映射",description = "持久层实体,源表和维表专用")
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class TJobDs  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "映射主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 元数据类型  源表、结果表、维表、视图、自定义函数 
     */
    @ApiModelProperty(value = "元数据类型  对应字典：ds_type")
    private String dsType;

    /**
     * 选择的对应类型ID
     */
    @ApiModelProperty(value = "数据源对应类型ID")
    private Long dsId;

    @ApiModelProperty(value = "作业ID")
    private Long jobId;


}
