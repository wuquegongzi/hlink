package com.haibao.flink.connectors.side.jdbc;

import com.haibao.flink.cache.AbstractSideCache;
import com.haibao.flink.cache.CacheParm;
import com.haibao.flink.cache.CacheTypeEnum;
import com.haibao.flink.cache.W_TinyLFUSideCache;
import com.haibao.flink.model.DsSchemaColumn;
import com.haibao.flink.utils.GsonUtils;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;
import org.apache.flink.table.functions.AsyncTableFunction;
import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.table.functions.FunctionRequirement;
import org.apache.flink.types.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


/**
 * @ClassName JdbcAsyncLookupFunction
 * @Description 构建JdbcAsyncLookupFunction，使用vertx从mysql异步加载数据
 * @Author ml.c
 * @Date 2020/2/11 11:11 上午
 * @Version 1.0
 */
public class JdbcAsyncLookupFunction extends AsyncTableFunction<Row> {

    private Logger logger = LoggerFactory.getLogger(JdbcAsyncLookupFunction.class);

    private JDBCClient jdbcClient = null;

    private Map configMap;
    private final String tableName;
    private final String[] fieldNames;
    private final String[] connectionField;
    private final TypeInformation<?>[] fieldTypes;

    private CacheParm cacheParm;
    private AbstractSideCache sideCache;

    private List<DsSchemaColumn> dsSchemaColumnList;

    public JdbcAsyncLookupFunction(Builder builder) {
//        setJdbcClientConfig(builder.jsonObjectConfig);
        this.tableName = builder.tableName;
        this.fieldNames = builder.fieldNames;
        this.fieldTypes = builder.fieldTypes;
        this.connectionField = builder.connectionField;
        this.cacheParm = builder.cacheParm;
        this.dsSchemaColumnList = builder.dsSchemaColumnList;
        this.configMap = builder.map;
    }

    public Map getJdbcClientConfig() {
        return configMap;
    }
//    public static void setJdbcClientConfig(JsonObject jdbcClientConfig) {
//        JdbcAsyncLookupFunction.jdbcClientConfig = jdbcClientConfig;
//    }

    /**
     *   根据传递的keys异步查询维表数据
     * @param resultFuture
     * @param keys   源表某些字段的值，通常用来做数据筛选时使用
     */
    public void eval(CompletableFuture<Collection<Row>> resultFuture, Object... keys) throws Exception {

        logger.info("传递的查询参数{}",Json.encode(keys));

        JsonArray inputParams = new JsonArray();
        Arrays.asList(keys).forEach(inputParams::add);
        //从缓存获取
        String cacheKey = tableName.concat("_").concat(StringUtils.join(keys, "-"));

        List<Row> rowList;
        if(openCacheBoolean()) {
            try {
                rowList=(List<Row>)sideCache.getFromCache(cacheKey);
                if(null != rowList){
                    resultFuture.complete(rowList);
                    logger.info("缓存命中,key:{},data:{}",rowList);
                    return;
                }
            } catch (Exception e) {
//                e.printStackTrace();
                sideCache.removeCache(cacheKey);
                logger.error("Cache发序列化异常，key:{},异常信息：{}",cacheKey,e.getMessage());
            }
        }

        try {
            query(inputParams,cacheKey,resultFuture);
        } catch (Exception e) {
            logger.error("jdbc数据库查询异常：{}",e.getMessage());
            resultFuture.complete(Collections.emptyList());
            return;
        }
    }

    /**
     * 异步查询
     * @param inputParams
     * @param cacheKey
     * @param resultFuture
     */
    private void query(JsonArray inputParams, String cacheKey, CompletableFuture<Collection<Row>> resultFuture) {
        jdbcClient.getConnection(conn -> {
            if (conn.failed()) {
                resultFuture.completeExceptionally(conn.cause());
                return;
            }
            final SQLConnection connection = conn.result();
            String sqlCondition = getSelectFromStatement(tableName, fieldNames, connectionField);

            // vertx异步查询
            connection.queryWithParams(sqlCondition, inputParams, rs -> {

                logger.info("jdbc vertx异步查询结果：{}",rs.succeeded());
                if (rs.failed()) {
//                    resultFuture.complete(Collections.emptyList());
                    resultFuture.completeExceptionally(rs.cause());
                    return;
                }
                logger.info("jdbc vertx异步查询数据：{}", GsonUtils.gsonString(rs.result().getResults()));

                int resultSize = rs.result().getResults().size();
                List<Row> rowList;
                if (resultSize > 0) {
                    rowList = Lists.newArrayList();
                    for (JsonArray line : rs.result().getResults()) {
                        Row row = buildRow(line);
                        rowList.add(row);
                    }
                } else {
                    rowList = Collections.emptyList();
                }

                //防止缓存穿透问题，即使为空，也要放入缓存
                if(openCacheBoolean()){
                    sideCache.putCache(cacheKey,rowList);
                }

                resultFuture.complete(rowList);

                // and close the connection
                connection.close(done -> {
                    if (done.failed()) {
                        throw new RuntimeException(done.cause());
                    }
                });

                return;
            });
        });
    }

    private Row buildRow(JsonArray line) {
        Row row = new Row(fieldNames.length);
        for (int i = 0; i < fieldNames.length; i++) {
            row.setField(i, line.getValue(i));
        }
        return row;
    }

    /**
     * 数据返回类型
     * @return
     */
    @Override
    public TypeInformation<Row> getResultType() {
        return  new RowTypeInfo(fieldTypes, fieldNames);
    }

    @Override
    public void open(FunctionContext context) throws Exception {
        initCache();
        initJdbc();
    }

    /**
     * 初始化 jdbc连接池
     */
    private void initJdbc() {

        System.out.println("jdbc配置："+GsonUtils.gsonString(getJdbcClientConfig()));

        JsonObject jsonObject = new JsonObject(getJdbcClientConfig());
        // 使用vertx来实现异步jdbc查询
        System.setProperty("vertx.disableFileCPResolving", "true");

        VertxOptions vo = new VertxOptions();
        vo.setFileResolverCachingEnabled(false);
        vo.setWarningExceptionTime(60000);
        vo.setMaxEventLoopExecuteTime(60000);
        Vertx vertx = Vertx.vertx(vo);
        jdbcClient = JDBCClient.createNonShared(vertx, jsonObject);
    }

    /**
     * 缓存初始化
     */
    private void initCache(){
        AbstractSideCache sideCache = null;

        if(openCacheBoolean()){
            //暂时默认LRU
            if(CacheTypeEnum.W_TinyLFU.getCacheType().equals(cacheParm.getCacheType())){

                sideCache = new W_TinyLFUSideCache(cacheParm);
                this.sideCache = sideCache;

            }else{
                throw new RuntimeException("not support side cache with type:" + cacheParm.getCacheType());
            }
            sideCache.initCache();
        }
    }

    private boolean openCacheBoolean(){
        return !CacheTypeEnum.NONE.getCacheType().equals(cacheParm.getCacheType());
    }


    public static String quoteIdentifier(String identifier) {
        return "`" + identifier + "`";
    }
    //  构建查询维表使用的sql
    public static String getSelectFromStatement(String tableName, String[] selectFields, String[] conditionFields) {
        String fromClause = Arrays.stream(selectFields).map(i -> quoteIdentifier(i)).collect(Collectors.joining(", "));
        String whereClause = Arrays.stream(conditionFields).map(f -> quoteIdentifier(f) + "=? ").collect(Collectors.joining(", "));
        String sqlStatement = "SELECT " + fromClause + " FROM " + quoteIdentifier(tableName) + (conditionFields.length > 0 ? " WHERE " + whereClause : "");

        System.out.println("JDBC维表生成语句:"+sqlStatement);
        return sqlStatement;
    }

    @Override
    public void close() throws Exception {
        super.close();
        if(null != jdbcClient){
            jdbcClient.close();
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public Set<FunctionRequirement> getRequirements() {
        return null;
    }

    @Override
    public boolean isDeterministic() {
        return false;
    }


    /**
     * 属性构建
     */
    public static final class Builder {
        // 查询维表中的字段
        private String[] fieldNames;
        // 查询条件,where中的条件
        private String[] connectionField;
        // 维表数据返回的类型
        private TypeInformation<?>[] fieldTypes;
        //jdbc客户端配置信息
        private Map map;
        //表名
        private String tableName;

        private CacheParm cacheParm;

        private List<DsSchemaColumn> dsSchemaColumnList;

        private Builder() {
        }

        public static Builder getBuilder() {
            return new Builder();
        }

        public Builder withFieldNames(String[] fieldNames) {
            this.fieldNames = fieldNames;
            return this;
        }

        public Builder withConnectionField(String[] connectionField) {
            this.connectionField = connectionField;
            return this;
        }

        public Builder withFieldTypes(TypeInformation[] fieldTypes) {
            this.fieldTypes = fieldTypes;
            return this;
        }

        public Builder withConfigMap(Map map){
            this.map = map;
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

        public JdbcAsyncLookupFunction build() {
            return new JdbcAsyncLookupFunction(this);
        }


    }

}
