package com.haibao.flink.connectors.side.http;

import com.haibao.flink.cache.CacheParm;
import com.haibao.flink.enums.HttpMedimTypeEnum;
import com.haibao.flink.enums.HttpMethodTypeEnum;
import com.haibao.flink.model.DsSchemaColumn;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.functions.AsyncTableFunction;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.table.sources.LookupableTableSource;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.utils.TypeConversions;
import org.apache.flink.types.Row;

import java.util.List;
import java.util.Map;

/**
*  @author: c.zh
*  @description: 创建HttpAsyncLookupTableSource传递AsyncTableFunction
*  @date: 2020/3/5
**/
public class HttpAsyncLookupTableSource implements LookupableTableSource<Row> {

    private final String[] fieldNames;
    private final TypeInformation[] fieldTypes;
    private String requestUrl;
    private HttpMethodTypeEnum methodType;
    private Map<String,String> extraParam;
    private Integer timeOut;
    private String  charSet;
    private String[] connectionField;
    private HttpMedimTypeEnum medimType;
    private String  tableName;
    private CacheParm cacheParm;
    private List<DsSchemaColumn> dsSchemaColumnList;

    public HttpAsyncLookupTableSource(Builder builder) {
        this.requestUrl = builder.requestUrl;
        this.fieldNames = builder.fieldNames;
        this.fieldTypes = builder.fieldTypes;
        this.methodType = builder.methodType;
        this.extraParam = builder.extraParam;
        this.timeOut = builder.timeOut;
        this.charSet = builder.charSet;
        this.connectionField = builder.connectionField;
        this.medimType = builder.medimType;
        this.tableName = builder.tableName;
        this.cacheParm =builder.cacheParm;
        this.dsSchemaColumnList = builder.dsSchemaColumnList;
    }

    @Override
    public TableFunction<Row> getLookupFunction(String[] lookupKeys) {
        return null;
    }

    /**
    *  @author: c.zh
    *  @description: 使用AsyncTableFunction，加载维表数据
    *  @date: 2020/3/24
    **/
    @Override
    public AsyncTableFunction<Row> getAsyncLookupFunction(String[] lookupKeys) {
        return HttpAsyncLookupFunction.Builder.getBuilder()
                .withFieldNames(fieldNames)
                .withFieldTypes(fieldTypes)
                .withMethodType(methodType)
                .withRequestUrl(requestUrl)
                .withExetParam(extraParam)
                .withTimeOut(timeOut)
                .withCharSet(charSet)
                .withConnectionField(connectionField)
                .withMedimType(medimType)
                .withTableName(tableName)
                .withCacheParm(cacheParm)
                //列层级投影 用于封装嵌套数据
                .withColumnLevelProjection(dsSchemaColumnList)
                .build();
    }

    @Override
    public boolean isAsyncEnabled() {
        return true;
    }

    /**
     * 读取的数据类型
     * @return
     */
    @Override
    public DataType getProducedDataType() {
        // 旧版本的Typeinfo类型转新版本的DataType
        return TypeConversions.fromLegacyInfoToDataType(new RowTypeInfo(fieldTypes, fieldNames));
    }

    @Override
    public TableSchema getTableSchema() {
        return TableSchema.builder()
                .fields(fieldNames, TypeConversions.fromLegacyInfoToDataType(fieldTypes))
                .build();
    }

    public static final class Builder {
        //HTTP 请求接口地址
        private String requestUrl;
        // GET/POST
        private HttpMethodTypeEnum methodType;
        private Map<String,String>   extraParam;
        private String[] fieldNames;
        private TypeInformation[] fieldTypes;
        private Integer timeOut;
        private String  charSet;
        private String[] connectionField;
        private HttpMedimTypeEnum medimType;
        private String  tableName;
        private CacheParm cacheParm;
        private List<DsSchemaColumn> dsSchemaColumnList;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withRequestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }
        public Builder withMethodType(HttpMethodTypeEnum methodType) {
            if(methodType==null){
                methodType=HttpMethodTypeEnum.GET;
            }
            this.methodType = methodType;
            return this;
        }
        public Builder withFieldNames(String[] fieldNames) {
            this.fieldNames = fieldNames;
            return this;
        }
        public Builder withFieldTypes(TypeInformation[] fieldTypes) {
            this.fieldTypes = fieldTypes;
            return this;
        }
        public Builder withExtraParam(Map<String,String> extraParam) {
            this.extraParam = extraParam;
            return this;
        }
        public Builder withTimeOut(Integer timeOut) {
            this.timeOut = timeOut;
            return this;
        }
        public Builder withCharSet(String charSet) {
            this.charSet = charSet;
            return this;
        }
        public Builder withConnectionField(String[] connectionField) {
            this.connectionField = connectionField;
            return this;
        }
        public Builder withMedimType(HttpMedimTypeEnum medimType) {
            if(medimType==null){
                medimType=HttpMedimTypeEnum.JSON;
            }
            this.medimType = medimType;
            return this;
        }
        public Builder withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder withCacheParm(CacheParm cacheParm) {
            this.cacheParm = cacheParm;
            return this;
        }

        public Builder withColumnLevelProjection(List<DsSchemaColumn> dsSchemaColumnList) {
            this.dsSchemaColumnList = dsSchemaColumnList;
            return this;
        }

        public HttpAsyncLookupTableSource build() {
            return new HttpAsyncLookupTableSource(this);
        }

    }
}
