package com.haibao.flink.enums;

/**
 * @ClassName FlinkSQLTypesEnum
 * @Description 支持的Flink SQL类型
 * @Author ml.c
 * @Date 2020/3/11 3:41 下午
 * @Version 1.0
 */
public enum FlinkSQLTypesEnum {

    /**
     * 实体类嵌套 类型
     */
    ROW("ROW",true),
    VARCHAR("VARCHAR",true),
    /**
     * 数组嵌套类型
     */
    ARRAY("ARRAY",true),
    INT("INT",true),
    BIGINT("BIGINT",true),
    FLOAT("FLOAT",true),
    DOUBLE("DOUBLE",true),
    BOOLEAN("BOOLEAN",true),
    DATE("DATE",true),
    TIME("TIME",true),
    TIMESTAMP("TIMESTAMP",true),
    DECIMAL("DECIMAL",true),
    // null（尚不支持）
    NULL("NULL",false),
    ARRAY_TINYINT("ARRAY[TINYINT]",true);


    private String type;
    private boolean unsupported;

    FlinkSQLTypesEnum(String type,boolean unsupported){
        this.type = type;
        this.unsupported = unsupported;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isUnsupported() {
        return unsupported;
    }

    public void setUnsupported(boolean unsupported) {
        this.unsupported = unsupported;
    }}
