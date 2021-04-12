package com.haibao.tests.kafka;

import cn.hutool.core.date.DateUtil;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/*
 * @Author ml.c
 * @Description
 * @Date 09:13 2020-04-10
 **/
public class IntervalJoinTest {

    public static void main(String[] args) throws Exception {

        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().useBlinkPlanner().build();
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment
                .createRemoteEnvironment("localhost",8081);
                //.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv, settings);

        // 流1
        // 浏览流 {"user_id": "110","item_id":"1715", "category_id": "1464116","behavior": "pv", "ts":"2018-11-26T01:00:00Z"}
        String kafkaBrowse = ""
                +"CREATE TABLE user_log (\n" +
                "    user_id VARCHAR,\n" +
                "    item_id VARCHAR,\n" +
                "    category_id VARCHAR,\n" +
                "    behavior VARCHAR,\n" +
                "    ts TIMESTAMP\n" +
                ") WITH (\n" +
                "    'connector.type' = 'kafka',\n" +
                "    'connector.version' = 'universal',\n" +
                "    'connector.topic' = 'user_behavior',\n" +
                "    'update-mode' = 'append',\n" +
                "    'connector.properties.0.key' = 'zookeeper.connect',\n" +
                "    'connector.properties.0.value' = 'localhost:2181',\n" +
                "    'connector.properties.1.key' = 'bootstrap.servers',\n" +
                "    'connector.properties.1.value' = 'localhost:9092',\n" +
                "    'connector.properties.2.key' = 'group.id',\n" +
                "    'connector.properties.2.value' = 'testGroup3',\n" +
                "    'connector.startup-mode' = 'earliest-offset',\n" +
                "    'format.type' = 'json',\n" +
                "    'format.fail-on-missing-field' = 'true',\n" +
                "    'format.json-schema' =\n" +
                "    '{\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"user_id\": {\n" +
                "          \"type\": \"string\"\n" +
                "        },\n" +
                "        \"item_id\": {\n" +
                "          \"type\": \"string\"\n" +
                "        },\n" +
                "        \"category_id\": {\n" +
                "          \"type\": \"string\"\n" +
                "        },\n" +
                "        \"behavior\": {\n" +
                "          \"type\": \"string\"\n" +
                "        },\n" +
                "        \"ts\": {\n" +
                "          \"type\": \"string\",\n" +
                "          \"format\": \"date-time\"\n" +
                "        }\n" +
                "      }\n" +
                "    }'\n" +
                ")";
        tableEnv.sqlUpdate(kafkaBrowse);
        Table userLog = tableEnv.scan("user_log");
//        user_log.addColumns("PROCTIME() as proctime");
        tableEnv.toAppendStream(userLog, Row.class).print();

        // 流2
        // 用户基础信息变化流 {"user_id": "110","item_id":"1715", "category_id": "1464116","behavior": "pv2", "ts":"2018-11-26T01:00:00Z","work":{"address":"文一路","company":"阿里"}}
        String kafkaUser = ""
                + "CREATE TABLE user_qt_log (\n" +
                "    user_id VARCHAR,\n" +
                "    item_id VARCHAR,\n" +
                "    category_id VARCHAR,\n" +
                "    behavior VARCHAR,\n" +
                "    work  ROW< address VARCHAR,company  VARCHAR>,\n" +
                "    ts TIMESTAMP\n" +
                ") WITH (\n" +
                "    'connector.type' = 'kafka',\n" +
                "    'connector.version' = 'universal',\n" +
                "    'connector.topic' = 'obj_qt',\n" +
                "    'update-mode' = 'append',\n" +
                "    'connector.properties.0.key' = 'zookeeper.connect',\n" +
                "    'connector.properties.0.value' = 'localhost:2181',\n" +
                "    'connector.properties.1.key' = 'bootstrap.servers',\n" +
                "    'connector.properties.1.value' = 'localhost:9092',\n" +
                "    'connector.properties.2.key' = 'group.id',\n" +
                "    'connector.properties.2.value' = 'obj_qt_group',\n" +
                "    'connector.startup-mode' = 'latest-offset',\n" +
                "    'format.type' = 'json',\n" +
                "    'format.fail-on-missing-field' = 'true',\n" +
                "    'format.derive-schema' = 'true'\n" +
                ")";
        tableEnv.sqlUpdate(kafkaUser);
        tableEnv.toAppendStream(tableEnv.scan("user_qt_log"), Row.class).print();

        // Interval Join
        String execSQL = ""
                +"  SELECT a.user_id,a.item_id,a.category_id,a.behavior,a.ts,b.work.address,b.work.company\n" +
                "  FROM  user_log a, user_qt_log  b\n" +
                "  WHERE\n" +
                "    a.user_id = b.user_id\n" +
                "    AND b.ts BETWEEN a.ts - INTERVAL '30' SECOND AND a.ts";
        tableEnv.toAppendStream(tableEnv.sqlQuery(execSQL), Row.class).print();

        tableEnv.execute(IntervalJoinTest.class.getSimpleName()+ DateUtil.now());
    }

}
