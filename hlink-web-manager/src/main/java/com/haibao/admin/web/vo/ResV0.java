package com.haibao.admin.web.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: c.zh
 * @description:
 * @date: 2020/2/25
 **/

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class ResV0 extends  BaseVO {

    private Long id;
    /**
     * 集群ID
     */
    private Long clusterId;

    /**
     * 资源原始名称
     */
    private String resName;

    /**
     * 资源类型 0-jar
     */
    private  Integer resType;

    /**
     * 资源新名称
     */
    private String resUname;

    /**
     * 文件大小
     */
    private Long resSize;


    /**
     * 启动类
     */
    private String entryClass;
}
