/*
package com.haibao.flink.sql.core.sink.hbase;

import org.apache.flink.api.common.io.OutputFormat;
import org.apache.flink.configuration.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

*/
/**
 * @author: c.zh
 * @description: 代码方式实现hbase-sink的保留类
 * @date: 2020/3/31
 **//*


public class HBaseOutputFormat extends OutputFormat {

    private Connection conn;
    private BufferedMutator

    @Override
    public void configure(Configuration configuration) {

    }

    @Override
    public void open(int i, int i1) throws IOException {
        org.apache.hadoop.conf.Configuration configuration= HBaseConfiguration.create();
        configuration.set(HConstants.ZOOKEEPER_QUORUM,"10.57.17.160");
        configuration.set(HConstants.ZOOKEEPER_CLIENT_PORT,"2181");
        conn = ConnectionFactory.createConnection(config)

    }

    @Override
    public void writeRecord(Object o) throws IOException {

    }

    @Override
    public void close() throws IOException {

    }
}
*/
