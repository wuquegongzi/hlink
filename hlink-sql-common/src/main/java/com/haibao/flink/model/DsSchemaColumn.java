package com.haibao.flink.model;

import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.Serializable;

/**
 * <p>
 * 数据源的schema解析后的属性
 * </p>
 *
 * @author jobob
 * @since 2020-02-25
 */
public class DsSchemaColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 虚拟id，采用雪花算法
     */
    private String virtualId;

    /**
     * 虚拟父级id，默认顶级为0
     */
    private String virtualPid;

    /**
     * 关联数据源ID，指定所属数据源
     */
    private Long dsId;

    /**
     * 字段属性名
     */
    private String name;

    /**
     * 字段属性类型
     */
    private String flinkType;

    /**
     * 基础数据类型
     */
    private String  basicType;

    private TypeInformation typeInformation;

    /**
     * 是否是连接key
     */
    private int joinKey;

    /**
     * 是否是事件时间
     */
    private int eventTime;

    /**
     * 描述
     */
    private String comment;

    private Integer level;

    /**
     * 备用字段1
     */
    private String res1;

    private String res2;

    private String res3;

    private String res4;

    private String res5;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDsId() {
        return dsId;
    }

    public void setDsId(Long dsId) {
        this.dsId = dsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlinkType() {
        return flinkType;
    }

    public void setFlinkType(String flinkType) {
        this.flinkType = flinkType;
    }

    public int getJoinKey() {
        return joinKey;
    }

    public void setJoinKey(int joinKey) {
        this.joinKey = joinKey;
    }

    public int getEventTime() {
        return eventTime;
    }

    public void setEventTime(int eventTime) {
        this.eventTime = eventTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRes1() {
        return res1;
    }

    public void setRes1(String res1) {
        this.res1 = res1;
    }

    public String getRes2() {
        return res2;
    }

    public void setRes2(String res2) {
        this.res2 = res2;
    }

    public String getRes3() {
        return res3;
    }

    public void setRes3(String res3) {
        this.res3 = res3;
    }

    public String getRes4() {
        return res4;
    }

    public void setRes4(String res4) {
        this.res4 = res4;
    }

    public String getRes5() {
        return res5;
    }

    public void setRes5(String res5) {
        this.res5 = res5;
    }

    public String getBasicType() {
        return basicType;
    }

    public void setBasicType(String basicType) {
        this.basicType = basicType;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public TypeInformation getTypeInformation() {
        return typeInformation;
    }

    public void setTypeInformation(TypeInformation typeInformation) {
        this.typeInformation = typeInformation;
    }

    public String getVirtualId() {
        return virtualId;
    }

    public void setVirtualId(String virtualId) {
        this.virtualId = virtualId;
    }

    public String getVirtualPid() {
        return virtualPid;
    }

    public void setVirtualPid(String virtualPid) {
        this.virtualPid = virtualPid;
    }
}
