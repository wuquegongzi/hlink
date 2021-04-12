package com.haibao.tests.jdbc;

import com.haibao.flink.cache.CacheParm;
import com.haibao.flink.cache.CacheTypeEnum;
import com.haibao.flink.connectors.side.jdbc.JdbcAsyncLookupTableSource;
import com.haibao.tests.socket.CustomerSocketTextStreamFunction;
import com.haibao.tests.socket.ServersocketSourceTableInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.shaded.guava18.com.google.common.collect.Maps;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SideTabelJoin
 * @Description 维表测试
 * @Author ml.c
 * @Date 2020/2/11 11:51 上午
 * @Version 1.0
 */
public class JdbcSideTabelJoinTest {

    public static void main(String[] args) throws Exception {
        // use blink and streammode
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();

        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment
                .createRemoteEnvironment("localhost",8081);
//                .getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv, settings);

        //{"id":"1", "name":"陈明","visitCount":1000}
//        joinSocket(streamEnv,tableEnv);

        //{"user_id": "1111","item_id":"1715", "category_id": "1464116","behavior": "pv", "ts":"2018-11-26T01:00:00Z","work":[{"address":"文一路","company":"阿里"},{"address":"文二路","company":"腾讯"}]}
        joinKafka(streamEnv,tableEnv);

        streamEnv.execute();

    }

    private static void joinKafka(StreamExecutionEnvironment streamEnv, StreamTableEnvironment tableEnv) {

        tableEnv.sqlUpdate("CREATE TABLE user_array_log (\n" +
                "    user_id VARCHAR,\n" +
                "    item_id VARCHAR,\n" +
                "    category_id VARCHAR,\n" +
                "    behavior VARCHAR,\n" +
                "    work  ARRAY<ROW<address VARCHAR,company VARCHAR>>,\n" +
                "    ts TIMESTAMP\n" +
                ") WITH (\n" +
                "    'connector.type' = 'kafka',\n" +
                "    'connector.version' = 'universal',\n" +
                "    'connector.topic' = 'array_qt',\n" +
                "    'update-mode' = 'append',\n" +
                "    'connector.properties.0.key' = 'zookeeper.connect',\n" +
                "    'connector.properties.0.value' = 'localhost:2181',\n" +
                "    'connector.properties.1.key' = 'bootstrap.servers',\n" +
                "    'connector.properties.1.value' = 'localhost:9092',\n" +
                "    'connector.properties.2.key' = 'group.id',\n" +
                "    'connector.properties.2.value' = 'array_qt_group',\n" +
                "    'connector.startup-mode' = 'latest-offset',\n" +
                "    'format.type' = 'json',\n" +
                "    'format.fail-on-missing-field' = 'true',\n" +
                "    'format.derive-schema' = 'true'\n" +
                ")");

        Map<String,String> mysqlClientConfig = Maps.newHashMap();
        mysqlClientConfig.put("url", "jdbc:mysql://10.58.10.195:3306/flink_hlink_test?charset=utf8");
        mysqlClientConfig.put("driver_class", "com.mysql.cj.jdbc.Driver");
        mysqlClientConfig.put("user", "root");
        mysqlClientConfig.put("password", "123456");

        CacheParm caffeineCache = CacheParm.Builder.newBuilder()
                .setCacheType(CacheTypeEnum.W_TinyLFU.getCacheType())
                .setInitialCapacity(500)
                .setMaximumSize(10000L)
                .setExpireAfterWrite(5000L)
                .build();

        JdbcAsyncLookupTableSource tableSource =JdbcAsyncLookupTableSource.Builder.newBuilder()
                .withConfigMap(mysqlClientConfig)
                .withTableName("side_test")
                .withFieldNames(new String[]{"a", "b", "c", "d"})
                .withFieldTypes(new TypeInformation<?>[]{Types.STRING, Types.LONG, Types.STRING, Types.STRING})
                //join 连接key
                .withConnectionField(new String[]{"a"})
                .withCacheParm(caffeineCache)
                .build();

        tableEnv.registerTableSource("sideTable", tableSource);

        Table table = tableEnv.sqlQuery(
                "select " +
                "t1.user_id," +
                "t1.item_id ," +
                "t1.proctime," +
                "s1.b ," +
                "s1.c ," +
                "s1.d " +
                "from (" +
                        "select user_id,item_id, PROCTIME() as proctime from user_array_log" +
                        ") as t1 join sideTable " +
                " FOR SYSTEM_TIME AS OF t1.proctime s1 on t1.user_id =s1.a ");
        // 查询的结果直接打印
        DataStream<Row> rowDataStream = tableEnv.toAppendStream(table, Row.class);
        rowDataStream.print();
    }

    private static void joinSocket(StreamExecutionEnvironment streamEnv,StreamTableEnvironment tableEnv) {

        RowTypeInfo typeInformation = buildRowTypeInfo();
        CustomerSocketTextStreamFunction sourceFunction = buildCustomerSocketTextStreamFunction(typeInformation);
        String tableName = "user_visit";

        DataStreamSource serversocketSource = streamEnv.addSource(sourceFunction, tableName, typeInformation);
        // 源表
        //serversocketSource.print();
        System.out.println("源表 列："+String.join(",", typeInformation.getFieldNames()) + ",proctime.proctime");
        tableEnv.registerDataStream(tableName, serversocketSource, String.join(",", typeInformation.getFieldNames()) + ", proctime.proctime");

        // 维表

//        String url = config.getString("url");
//        if (url == null) throw new NullPointerException("url cannot be null");
//        String driverClass = config.getString("driver_class");
//        String user = config.getString("user");
//        String password = config.getString("password");
//        Integer maxPoolSize = config.getInteger("max_pool_size");
//        Integer initialPoolSize = config.getInteger("initial_pool_size");
//        Integer minPoolSize = config.getInteger("min_pool_size");
//        Integer maxStatements = config.getInteger("max_statements");
//        Integer maxStatementsPerConnection = config.getInteger("max_statements_per_connection");
//        Integer maxIdleTime = config.getInteger("max_idle_time");
//        Integer acquireRetryAttempts = config.getInteger("acquire_retry_attempts");
//        Integer acquireRetryDelay = config.getInteger("acquire_retry_delay");
//        Boolean breakAfterAcquireFailure = config.getBoolean("break_after_acquire_failure");


        Map mysqlClientConfig = new HashMap(4);
        mysqlClientConfig.put("url", "jdbc:mysql://10.58.10.195:3306/flink_hlink?charset=utf8");
        mysqlClientConfig.put("driver_class", "com.mysql.cj.jdbc.Driver");
        mysqlClientConfig.put("user", "root");
        mysqlClientConfig.put("password", "123456");

        CacheParm caffeineCache = CacheParm.Builder.newBuilder()
                .setCacheType(CacheTypeEnum.W_TinyLFU.getCacheType())
                .setInitialCapacity(500)
                .setMaximumSize(10000L)
                .setExpireAfterWrite(5000L)
                .build();

        JdbcAsyncLookupTableSource tableSource =JdbcAsyncLookupTableSource.Builder.newBuilder()
                .withConfigMap(mysqlClientConfig)
                .withTableName("side_test")
                .withFieldNames(new String[]{"a", "b", "c", "d"})
                .withFieldTypes(new TypeInformation[]{Types.STRING, Types.LONG, Types.STRING, Types.STRING})
                //join 连接key
                .withConnectionField(new String[]{"a"})
                .withCacheParm(caffeineCache)
                .build();

        tableEnv.registerTableSource("sideTable", tableSource);

        Table table = tableEnv.sqlQuery("select " +
                "t1.visitCount," +
                "t1.name ," +
                "t1.proctime," +
                "s1.b ," +
                "s1.c " +
                "from user_visit as t1 join sideTable " +
                " FOR SYSTEM_TIME AS OF t1.proctime s1 on t1.name =s1.a ");
        // 查询的结果直接打印
        DataStream<Row> rowDataStream = tableEnv.toAppendStream(table, Row.class);
        rowDataStream.print();
    }

    private static RowTypeInfo buildRowTypeInfo() {
        TypeInformation[] types = new TypeInformation[]{Types.STRING, Types.STRING, Types.LONG};
        String[] fields = new String[]{"id", "name", "visitCount"};
        return new RowTypeInfo(types, fields);
    }

    private static CustomerSocketTextStreamFunction buildCustomerSocketTextStreamFunction(RowTypeInfo typeInformation) {
        ServersocketSourceTableInfo tableInfo = new ServersocketSourceTableInfo("127.0.0.1", 9900, "\n", 3);
        return new CustomerSocketTextStreamFunction(tableInfo, typeInformation);
    }
}
