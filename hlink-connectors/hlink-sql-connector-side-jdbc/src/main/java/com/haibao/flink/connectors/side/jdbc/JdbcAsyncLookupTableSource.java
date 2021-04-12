package com.haibao.flink.connectors.side.jdbc;

import com.haibao.flink.cache.CacheParm;
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
 * @ClassName JdbcAsyncLookupTableSource
 * @Description 创建JdbcAsyncLookupTableSource传递AsyncTableFunction
 * @Author ml.c
 * @Date 2020/2/11 11:48 上午
 * @Version 1.0
 */
public class JdbcAsyncLookupTableSource implements LookupableTableSource<Row> {
    private final String[] fieldNames;
    private final String[] connectionField;
    private final TypeInformation<?>[] fieldTypes;
    /**
     * 用于查询的表名，对应数据库，不能是别名
     */
    private final String tableName;
    /**
     * jdbc 客户端连接信息，默认采用Vertx异步连接，c3p0连接池
     */
    private Map configMap;

    private CacheParm cacheParm;

    private List<DsSchemaColumn> dsSchemaColumnList;

    public JdbcAsyncLookupTableSource(Builder builder) {
        this.tableName = builder.tableName;
        this.fieldNames = builder.fieldNames;
        this.fieldTypes = builder.fieldTypes;
        this.connectionField = builder.connectionField;
        this.configMap = builder.map;
        this.cacheParm = builder.cacheParm;
        this.dsSchemaColumnList = builder.dsSchemaColumnList;
    }

    @Override
    public TableFunction<Row> getLookupFunction(String[] lookupKeys) {
        return null;
    }

    //  使用AsyncTableFunction，加载维表数据
    @Override
    public AsyncTableFunction<Row> getAsyncLookupFunction(String[] lookupKeys) {
        return JdbcAsyncLookupFunction.Builder.getBuilder()
                //添加数据库连接配置信息
                .withConfigMap(configMap)
                .withTableName(tableName)
                .withFieldNames(fieldNames)
                .withFieldTypes(fieldTypes)
                .withConnectionField(connectionField)
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
        //jdbc客户端配置信息
        private Map map;
        private String tableName;
        private String[] fieldNames;
        private String[] connectionField;
        private TypeInformation<?>[] fieldTypes;
        private CacheParm cacheParm;

        private List<DsSchemaColumn> dsSchemaColumnList;
        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withConfigMap(Map map){
            this.map = map;
            return this;
        }

        public Builder withTableName(String tableName){
            this.tableName = tableName;
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

        public Builder withConnectionField(String[] connectionField) {
            this.connectionField = connectionField;
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

        public JdbcAsyncLookupTableSource build() {
            return new JdbcAsyncLookupTableSource(this);
        }

    }
}
