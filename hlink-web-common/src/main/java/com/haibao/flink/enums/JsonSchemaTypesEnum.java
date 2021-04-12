package com.haibao.flink.enums;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;

/**
 * @ClassName JsonSchemaTypesRnum
 * @Description JSON模式类型到Flink SQL类型的映射
 * @Author ml.c
 * @Date 2020/3/11 3:54 下午
 * @Version 1.0
 */
public enum JsonSchemaTypesEnum {

    OBJECT("object","","",FlinkSQLTypesEnum.ROW, Types.ROW()),
    BOOLEAN("boolean","","",FlinkSQLTypesEnum.BOOLEAN,Types.BOOLEAN),
    ARRAY("array","","",FlinkSQLTypesEnum.ARRAY,Types.LIST(Types.VOID)),
    NUMBER("number","","",FlinkSQLTypesEnum.FLOAT,Types.FLOAT),
    INTEGER("integer","","",FlinkSQLTypesEnum.BIGINT,Types.BIG_INT),
    STRING("string","","",FlinkSQLTypesEnum.VARCHAR,Types.STRING),
    STRING_WITH_DATETIME("string","date-time","",FlinkSQLTypesEnum.TIMESTAMP,Types.LOCAL_DATE_TIME),
    STRING_WITH_DATE("string","date","",FlinkSQLTypesEnum.DATE,Types.LOCAL_DATE),
    STRING_WITH_TIME("string","time","",FlinkSQLTypesEnum.TIME,Types.LOCAL_TIME),
    STRING_WITH_ENCODING_BASE64("string","","base64",FlinkSQLTypesEnum.ARRAY_TINYINT,Types.PRIMITIVE_ARRAY(Types.INT)),
    NULL("null","","",FlinkSQLTypesEnum.NULL,Types.VOID);

    String type;
    String format;
    String encoding;
    FlinkSQLTypesEnum flinkSQLTypesEnum;
    TypeInformation typeInformation;

    JsonSchemaTypesEnum(String type, String format, String encoding, FlinkSQLTypesEnum flinkSQLTypesEnum, TypeInformation typeInformation) {
        this.type = type;
        this.format = format;
        this.encoding = encoding;
        this.flinkSQLTypesEnum = flinkSQLTypesEnum;
        this.typeInformation = typeInformation;
    }

    public static JsonSchemaTypesEnum getByType(String type,String format, String encoding) {
        for (JsonSchemaTypesEnum jsonSchemaType : values()) {
            if (jsonSchemaType.getType().equals(type)
                    && jsonSchemaType.getFormat().equals(format)
                    && jsonSchemaType.getEncoding().equals(encoding)
            ) {
                return jsonSchemaType;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public TypeInformation getTypeInformation() {
        return typeInformation;
    }

    public void setTypeInformation(TypeInformation typeInformation) {
        this.typeInformation = typeInformation;
    }

    public FlinkSQLTypesEnum getFlinkSQLTypesEnum() {
        return flinkSQLTypesEnum;
    }

    public void setFlinkSQLTypesEnum(FlinkSQLTypesEnum flinkSQLTypesEnum) {
        this.flinkSQLTypesEnum = flinkSQLTypesEnum;
    }
}
