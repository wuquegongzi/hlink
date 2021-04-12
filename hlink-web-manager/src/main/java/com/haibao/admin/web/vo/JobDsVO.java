package com.haibao.admin.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 作业对应的数据源选择结果,源表、维表、目标表 共用 视图实体
 * 分别 对应持久层 :
 * 源表、维表  -> TJobDs
 * 目标表 -> TJobDsSink
 * </p>
 *
 * @author jobob
 * @since 2020-03-05
 */
@ApiModel(value = "作业与数据源映射",description ="视图层实体" )
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class JobDsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 元数据类型  源表、结果表、维表、视图、自定义函数 
     */
    @ApiModelProperty(value = "数据源类型  source、sink、side")
    private String dsType;

    /**
     * 选择的数据源类型ID
     */
    @ApiModelProperty(value = "选择的数据源类型ID")
    private Long dsId;

    @ApiModelProperty(value = "对应的作业ID，新增作业的时候默认为0即可")
    private Long jobId;

    @ApiModelProperty(value = "特征加工语句，只有数据源类型为sink目标表的时候，才需要填写该值")
    private String runSql;
}
