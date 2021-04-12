package com.haibao.flink.enums;

/*
 * @Author ml.c
 * @Description 检查点存储方式
 * @Date 13:11 2020-04-21
 **/
public enum CheckpointStatebackendTypeEnum {

    MEMORY(0,"MemoryStateBackend"),
    FS(1,"FsStateBackend"),
    ROCKSDB(2,"RocksDBStateBackend");

    private Integer code;
    private String type;

    CheckpointStatebackendTypeEnum(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
