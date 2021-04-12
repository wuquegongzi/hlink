package com.haibao.admin.web.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2020-04-08
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@ApiModel(value = "作业日志对象",description = "持久层实体")
public class TJobLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "映射作业主键")
    private Long jobId;

    @ApiModelProperty(value = "作业日志")
    private String jobLog;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "modify_time")
    private Date modifyTime;

    @JsonIgnore  //坑爹的swagger
    @TableField(exist = false)
    private String logType;

    @JsonIgnore
    @TableField(exist = false)
    private int msgType;

}
