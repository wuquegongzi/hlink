# Flink SQL 特征实时加工平台

[idea markdown 插件安装](https://blog.csdn.net/l_lushuang/article/details/89487764)
***
## 代码模块说明

- [hlink-web-manager :  web 接口控制模块]
- [hlink-clients : 用于shell 执行flink sql 作业提交]
- [hlink-web-common : web 公共方法模块]
- [hlink-sql-common : clinets提交公共方法模块]
- [hlink-connections : 外部数据源连接自定义实现]
- [hlink-function : 自定义函数]

## 运行组件 相关版本
1、 flink 版本
- [flink-1.9.2-bin-scala_2.11.tgz](https://www.apache.org/dyn/closer.lua/flink/flink-1.9.2/flink-1.9.2-bin-scala_2.11.tgz)

- [download 页面](https://flink.apache.org/zh/downloads.html#apache-flink-192)

  备注：flink源码编译 
  
  ```shell
  mvn clean install -Dmaven.test.skip=true -Dhadoop.version=2.7.1 -Dmaven.javadoc.skip=true -Dcheckstyle.skip=true -Dlicense.skip=true -Drat.ignoreErrors=true
  
  ```

2、 flink 组件 ： 

​       jar组件已放置在 doc/lib文件夹.
​               需要部署在 {flink-home}/lib下，否则无法支持table功能



3、 mysql版本 ： 8.0+
              数据库测试执行脚本：/doc/db/flink_hlink.sql.bak



4、 kafka版本 ： [kafka_2.11-2.2.0]( https://archive.apache.org/dist/kafka/2.2.0/kafka_2.11-2.2.0.tgz)
      

5、 JDK版本 ：>=jdk 1.8.0

## 常用命令
1、启动zk

    sh zookeeper-server-start.sh ../config/zookeeper.properties &

2、启动kafka

    sh kafka-server-start.sh ../config/server.properties &

3、修改flink配置

     cd flink-1.9.2/conf
     vi flink-conf.yaml

 主要修改 任务元个数：

    taskmanager.numberOfTaskSlots:  10

 指定jar上传路径，放置flink重启资源被删除：

    web.upload.dir: /Users/leon/Documents/items/server/flink-1.9.1/jars/

 可选择配置：

    web.tmpdir: /Users/leon/Documents/items/server/flink-1.9.1/jars/
    io.tmp.dirs: /Users/leon/Documents/items/server/flink-1.9.1/tmp

 其他保持默认即可

4、启动flink 独立集群模式

    bin/start-cluster.sh

5、查看flink管理台

 localhost:8086

6、api在线
        localhost:8086/doc.html 
       admin/123456 

## API
web 接口：Read more [here markdown版](./res/doc/hlink-1.0.0.md)

web 接口： Read more [here html版](./res/doc/hlink-1.0.0.html)

SQL开发以及操作流程：  [markdown](./res/doc/index.md)

API截图：
![xxx](https://user-images.githubusercontent.com/29697202/114652987-a00ecd80-9d19-11eb-9fe5-eef2014220a4.png)

## 原型设计文档

[实时特征加工平台-产品原型-v1.5.xlsx](./res/doc/实时特征加工平台-产品原型-v1.5.xlsx)

