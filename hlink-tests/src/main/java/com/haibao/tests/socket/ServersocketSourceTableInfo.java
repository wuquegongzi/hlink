package com.haibao.tests.socket;

import java.io.Serializable;

/**
 * @ClassName ServersocketSourceTableInfo
 * @Description Serversocket实体类
 * @Author ml.c
 * @Date 2020/2/11 11:50 上午
 * @Version 1.0
 */
public class ServersocketSourceTableInfo implements Serializable {

    public ServersocketSourceTableInfo() { }

    public ServersocketSourceTableInfo(String hostname, int port, String delimiter, long maxNumRetries) {
        this.hostname = hostname;
        this.port = port;
        this.delimiter = delimiter;
        this.maxNumRetries = maxNumRetries;
    }

    private String hostname;

    private int port;

    private String delimiter;

    private long maxNumRetries;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public long getMaxNumRetries() {
        return maxNumRetries;
    }

    public void setMaxNumRetries(long maxNumRetries) {
        this.maxNumRetries = maxNumRetries;
    }

    @Override
    public String toString() {
        return "ServersocketSourceTableInfo{" +
                "hostname='" + hostname + '\'' +
                ", port=" + port +
                ", delimiter='" + delimiter + '\'' +
                ", maxNumRetries=" + maxNumRetries +
                '}';
    }
}
