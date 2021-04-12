package com.haibao.flink.log;

import java.util.HashMap;
import java.util.Map;

/*
 * @Author ml.c
 * @Description 日志结构
 * @Date 11:40 2020-04-15
 **/
public class LogEvent {

    //日志的类型(应用、容器、...)
    private String type;
    //日志的时间戳
    private Long timestamp;
    //日志的级别(debug/info/warn/error)
    private String level;
    //日志内容
    private String message;
    //日志的标识(应用 ID、应用名、容器 ID、机器 IP、集群名、...)
    private Map<String, String> tags = new HashMap<>();

    public LogEvent(String type, Long timestamp, String level, String message, Map<String, String> tags) {
        this.type = type;
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.tags = tags;
    }

    public LogEvent() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }
}
