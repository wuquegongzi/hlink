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
@ApiModel(value = "作业与数据源映射",description = "持久层实体,目标表专用")
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class TJobDsSink  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "映射主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据源ID
     */
    @ApiModelProperty(value = "数据源ID")
    private Long dsId;

    /**
     * 对应的作业ID
     */
    @ApiModelProperty(value = "对应的作业ID")
    private Long jobId;

    /**
     * 特征加工语句
     */
    @ApiModelProperty(value = "特征加工语句")
    private String runSql;


}
