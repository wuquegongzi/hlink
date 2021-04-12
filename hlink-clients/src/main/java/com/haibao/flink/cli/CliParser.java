package com.haibao.flink.cli;

import com.haibao.flink.model.Job;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @ClassName CliParser
 * @Description 文件分离解析器
 * @Author ml.c
 * @Date 2020/3/4 5:54 下午
 * @Version 1.0
 */
public class CliParser {

    private String sqlFilePath;
    private String workSpace;


    public CliParser(CliOptions options) {
        this.sqlFilePath = options.getSqlFilePath();
        this.workSpace = options.getWorkingSpace();
    }

    /**
     * 解析并聚合数据
     * @return
     * @throws Exception
     */
    public Job aggregate() throws Exception {

        List<String> sql = Files.readAllLines(Paths.get(workSpace + "/" + sqlFilePath));

        Job job = new Job();
        job.setFileSQLList(sql);

        return job;
    }



}
