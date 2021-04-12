package com.haibao.flink.model;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 数据源表
 * </p>
 *
 * @author jobob
 * @since 2020-02-17
 */
public class Ds implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 数据源名称
     */
    private String dsName;

    /**
     * 数据源类型 0-源表 1-结果表 2-维表
     */
    private String dsType;

    /**
     * 表类型，见字典表 dc_type_source、side、slink
     */
    private String tableType;

    /**
     * schema_type,默认0 json
     */
    private Integer schemaType;

    /**
     * 结构类型，单一结构-0或者多层嵌套结构-1
     */
    private Integer structureType;

    /**
     * schema文件描述
     */
    private String schemaFile;

    /**
     * 别名，用于SQL
     */
    private String tableName;

    /**
     * 是否启用ddl,默认0 启用。1-不启用。目前 维表不使用ddl。保留该字段
     */
    private Integer ddlEnable;

    /**
     * ddl建表语句，维表不需要
     */
    private String dsDdl;

    /**
     * 版本号，保留字段
     */
    private Integer dsVersion;

    /**
     * 模版属性 key - value
     */
    private String jsonValue;

    /**
     * 属性列表
     */
    private List<DsSchemaColumn> dsSchemaColumnList;


    public List<DsSchemaColumn> getDsSchemaColumnList() {
        return dsSchemaColumnList;
    }

    public void setDsSchemaColumnList(List<DsSchemaColumn> dsSchemaColumnList) {
        this.dsSchemaColumnList = dsSchemaColumnList;
    }

    public String getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public String getDsType() {
        return dsType;
    }

    public void setDsType(String dsType) {
        this.dsType = dsType;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public Integer getSchemaType() {
        return schemaType;
    }

    public void setSchemaType(Integer schemaType) {
        this.schemaType = schemaType;
    }

    public Integer getStructureType() {
        return structureType;
    }

    public void setStructureType(Integer structureType) {
        this.structureType = structureType;
    }

    public String getSchemaFile() {
        return schemaFile;
    }

    public void setSchemaFile(String schemaFile) {
        this.schemaFile = schemaFile;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getDdlEnable() {
        return ddlEnable;
    }

    public void setDdlEnable(Integer ddlEnable) {
        this.ddlEnable = ddlEnable;
    }

    public String getDsDdl() {
        return dsDdl;
    }

    public void setDsDdl(String dsDdl) {
        this.dsDdl = dsDdl;
    }

    public Integer getDsVersion() {
        return dsVersion;
    }

    public void setDsVersion(Integer dsVersion) {
        this.dsVersion = dsVersion;
    }
}
