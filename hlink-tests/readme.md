# Flink SQL shell提交模式

当前，Flink 1.9还不直接支持提交DDL在SQL CLI，并且不支持SQL脚本，只能通过Flink run 提交jar的方式实现SQL任务执行。

## 测试执行步骤
1. 修改 `env.sh`
2. 添加 SQL scripts 在 `src/main/resources/` 以 `.sql.txt` 结尾, e.g, `test.sql.txt`
3. 启动所有依赖组件服务，包括 Flink, Kafka, 和 DataBases.
3. 执行 SQL 通过 `./run.sh <sql-file-name>`, e.g. `./run.sh test`
4. job is submitted successfully.返回:

```
Starting execution of program
Job has been submitted with JobID d01b04d7c8f8a90798d6400462718743
```

## 测试
1、编译代码 根目录

    source ~/.bash_profile 
    mvn clean package -Dmaven.test.skip=true

2、在 hlink-tests 目录下运行

    ./source-generator.sh

会自动创建 user_behavior topic，并实时往里灌入数据。

3、在 hlink-tests 目录下运行

    ./run.sh test

提交成功后，可以在 Web UI 中看到拓扑。