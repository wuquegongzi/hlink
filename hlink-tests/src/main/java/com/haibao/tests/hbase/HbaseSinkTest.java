package com.haibao.tests.hbase;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.java.StreamTableEnvironment;

/*
 * @Author ml.c
 * @Description //TODO
 * @Date 18:27 2020-03-30
 **/
public class HbaseSinkTest {

    public static void main(String[] args) {
        EnvironmentSettings streamSettings = EnvironmentSettings.newInstance().inStreamingMode()
                .useBlinkPlanner().build();


        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment
//                .createRemoteEnvironment("localhost",8081);
                .getExecutionEnvironment();
//        streamEnv.enableCheckpointing(2 * 1000L);
//        streamEnv.getConfig().setLatencyTrackingInterval(LATENCY_TRACK_INTERVAL);
        StreamTableEnvironment streamTableEnv = StreamTableEnvironment.create(streamEnv, streamSettings);

        String kafkaSourceSQL = "create table kafka_topic_src\n" +
                "(\n" +
                "    user_id VARCHAR,\n" +
                "    item_id VARCHAR,\n" +
                "    category_id VARCHAR,\n" +
                "    behavior VARCHAR,\n" +
                "    ts TIMESTAMP" +
                ") with (\n" +
                "'connector.type' = 'kafka', \n" +
                "'connector.version' = 'universal',\n" +
                "'connector.topic' = 'user_behavior',\n" +
                "'connector.properties.0.key' = 'group.id',\n" +
                "'connector.properties.0.value' = 'testGroup2',\n" +
                "'connector.properties.1.key' = 'bootstrap.servers',\n" +
                "'connector.properties.1.value' = 'localhost:9092',\n" +
                "'connector.property-version' = '1',\n" +
                "'connector.startup-mode' = 'earliest-offset',\n" +
                "'format.type' = 'json',\n" +
                "'format.property-version' = '1',\n" +
                "'format.derive-schema' = 'true',\n" +
                "'update-mode' = 'append')";

        String hbaseSinkSQL = "CREATE TABLE hbase_sink_table\n" +
                "(\n" +
                "        rowkey VARCHAR,\n" +
                "        cf row( \n" +
                "         a VARCHAR )\n" +
                ")\n" +
                "with (\n" +
                "'connector.type' = 'hbase',\n" +
                "'connector.version' = '1.4.3',\n" +
                "'connector.table-name' = 'hbase_sink_table',\n" +
                "'connector.zookeeper.quorum' = '10.57.17.160:2181',\n" +
                "'connector.property-version' = '1')";


        String insertSQL = "insert into hbase_sink_table select user_id,row(item_id) from kafka_topic_src";

        streamTableEnv.sqlUpdate(kafkaSourceSQL);
        streamTableEnv.sqlUpdate(hbaseSinkSQL);
        streamTableEnv.sqlUpdate(insertSQL);
        try {
            streamEnv.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
