package com.haibao.tests.http;

import com.haibao.flink.cache.CacheParm;
import com.haibao.flink.cache.CacheTypeEnum;
import com.haibao.flink.connectors.side.http.HttpAsyncLookupTableSource;
import com.haibao.flink.enums.HttpMedimTypeEnum;
import com.haibao.flink.enums.HttpMethodTypeEnum;
import com.haibao.tests.socket.CustomerSocketTextStreamFunction;
import com.haibao.tests.socket.ServersocketSourceTableInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
*  @author: c.zh
*  @description: 维表测试
*  @date: 2020/3/9
**/
public class HttpSideTabelJoinTest {

    public static void main(String[] args) throws Exception {
        // use blink and streammode
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();

        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv, settings);

        RowTypeInfo typeInformation = buildRowTypeInfo();
        CustomerSocketTextStreamFunction sourceFunction = buildCustomerSocketTextStreamFunction(typeInformation);
        String tableName = "user_visit";

        DataStreamSource serversocketSource = streamEnv.addSource(sourceFunction, tableName, typeInformation);
        // 源表
        //serversocketSource.print();
        System.out.println("源表 列：" + String.join(",", typeInformation.getFieldNames()) + ",proctime.proctime");
        tableEnv.registerDataStream(tableName, serversocketSource, String.join(",", typeInformation.getFieldNames()) + ", proctime.proctime");

        CacheParm caffeineCache = CacheParm.Builder.newBuilder()
                .setCacheType(CacheTypeEnum.W_TinyLFU.getCacheType())
                .setInitialCapacity(500)
                .setMaximumSize(10000L)
                .setExpireAfterWrite(500L)
                .build();

        String[] fieldNames = new String[]{"address","company"};
        TypeInformation<?>[]  subTypes = new TypeInformation[]{Types.STRING,Types.STRING};

        // 维表
        HttpAsyncLookupTableSource tableSource = HttpAsyncLookupTableSource.Builder.newBuilder()
                .withFieldNames(new String[]{"a","b"})
                .withFieldTypes(new TypeInformation[]{Types.STRING,Types.ROW_NAMED(fieldNames,subTypes)})
//                .withFieldTypes(new TypeInformation[]{Types.STRING,Types.STRING})
                .withMethodType(HttpMethodTypeEnum.GET)
                .withRequestUrl("http://localhost:8080/test/row_test")
                .withExtraParam(null)
                .withTimeOut(1000)
                .withCharSet("UTF-8")
                .withMedimType(HttpMedimTypeEnum.JSON)
                .withConnectionField(new String[]{"a"})
                .withTableName("sideTable")
                .withCacheParm(caffeineCache)
                .build();

        tableEnv.registerTableSource("sideTable", tableSource);

//        Table table = tableEnv.sqlQuery("select t2.name,t3.address from (" +
//                "select t1.visitCount," +
//                    "t1.name ," +
//                    "t1.proctime," +
//                    "s1.* " +
//                " from user_visit as t1 " +
//                " join sideTable" +
//                " FOR SYSTEM_TIME AS OF t1.proctime s1 on t1.name=s1.a ) " +
//                " as t2,unnest(t2.b) as t3 (address,company) ");

        Table table = tableEnv.sqlQuery("select " +
                " t1.visitCount," +
                " t1.name ," +
                " t1.proctime," +
                " s1.b.address " +
                " from user_visit as t1 " +
                " join sideTable" +
                " FOR SYSTEM_TIME AS OF t1.proctime s1 on t1.name=s1.a ");

        // 查询的结果直接打印
        DataStream<Row> rowDataStream = tableEnv.toAppendStream(table, Row.class);
        rowDataStream.print();

        streamEnv.execute();
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
