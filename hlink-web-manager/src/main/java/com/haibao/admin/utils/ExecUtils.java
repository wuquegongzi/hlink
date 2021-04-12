package com.haibao.admin.utils;

import com.haibao.admin.web.common.result.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 执行shell 工具类
 * @author ml.c
 */
public class ExecUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecUtils.class);


    public Response<String> executeFlinkSQLJob(String content) throws IOException, InterruptedException {

        //todo 记录执行动作以及内容，便于追踪
        System.out.println("=============提交Flink SQL 作业=============\n"+content+"\n");

        Response<String> response = executeCommand(content);
        if (response.isSuccess()){

            //过滤获取jobID
            String result = response.getData();
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(result.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
            String line;
            String jobId = null;
            while ( (line = br.readLine()) != null ) {
                System.out.println(line);
                if(!"".equals(line.trim()) && line.contains("Job has been submitted with JobID")){
                    jobId = line.replace("Job has been submitted with JobID","").trim();
                }
            } 

            return Response.success(jobId);
        }

        return response;
    }

    /**
     * command 通用执行方法
     *
     * @param content 脚本的具体内容: 例如：ls -l
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public Response<String> executeCommand(String content) throws IOException, InterruptedException {

        StringBuilder cmdOut = new StringBuilder();
        StringBuilder errOut = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", content);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        InputStream fis = process.getInputStream();
        //用一个读输出流类去读
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line;
        //逐行读取输出到控制台
        while ((line = br.readLine()) != null) {
            cmdOut.append(line).append(System.getProperty("line.separator"));
            LOGGER.info(line);
            if (line.contains("ERROR")) {
                errOut.append(line).append(System.getProperty("line.separator"));
            }
        }

        while (process.isAlive()) {
            Thread.sleep(1000);
        }

        if (process.exitValue() == 0) {
            LOGGER.info("Success:\n {}", cmdOut.toString());
            return Response.success(cmdOut.toString());
        }
        LOGGER.error("Fail:\n {}", cmdOut.toString());
        LOGGER.info("OUT:\n {}", cmdOut.toString());
        LOGGER.info("ERROR:\n {}", errOut.toString());

        return Response.error(process.exitValue(),cmdOut.toString());
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        String content = "FLINK_DIR=/Users/leon/Documents/items/server/flink-1.9.2 \n" +
                "PROJECT_DIR=`pwd`\n" +
                "#-m 10.57.30.38:8081\n" +
                "#-s /Users/leon/Documents/test/savepoint-7f7501-8ac3033ef17b\n" +
                "$FLINK_DIR/bin/flink run -m 10.57.30.38:8081 -d -p 4 /Users/leon/Documents/items/ideaprojects/hlink/sql-submit/target/flink-sql-submit.jar " +
                " -w /Users/leon/Documents/items/ideaprojects/hlink/sql-submit/target/classes -f test.sql";

        ExecUtils execUtils = new ExecUtils();
        Response response = execUtils.executeFlinkSQLJob(content);
        System.out.println(response.isSuccess());
        System.out.println("任务执行结果："+response.getData());
    }
}
