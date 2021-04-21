# Flink 实时特征加工平台 API使用手册



[TOC]



## 平台介绍

Flink 实时特征加工平台，基于[Flink 1.9.2开源版本](https://ci.apache.org/projects/flink/flink-docs-release-1.9/getting-started/index.html)定制。

集群采用standlone模式，扩充Flink SQL作业能力，以kafka为数据源表，可进行双流结合、源与维表关联，最终将数据输出到目标表。

维表目前可支持mysql/oracle、http，并支持复杂嵌套结构。

目标表支持mysql/oracle、kafka、hbase。

## 功能介绍

1. 集群管理
2. 数据源管理
3. 文件管理
4. 作业管理

## 操作流程

![操作流程](./Flink实时特征加工-操作流程.pdf) 

## 开发API

- ### JAR开发



- ### SQL开发



几个场景介绍：

1、kafka 到 mysql

```sql
INSERT INTO pvuv_sink
SELECT
    DATE_FORMAT(ts, 'yyyy-MM-dd HH:00') dt,
    COUNT(*) AS pv,
    COUNT(DISTINCT user_id) AS uv,
    'cli_4' as source,
    '' as res1
FROM user_log
GROUP BY DATE_FORMAT(ts, 'yyyy-MM-dd HH:00');

{
    "user_id":"543462",
    "item_id":"1715",
    "category_id":"1464116",
    "behavior":"pv",
    "ts":"2018-11-26T01:00:00Z"
}
```


2、实体类嵌套

```sql
INSERT INTO pvuv_sink
SELECT
    DATE_FORMAT(ts, 'yyyy-MM-dd HH:00') dt,
    CAST(1 AS BIGINT) AS pv,
    CAST(1 AS BIGINT) AS uv,
    'obj_qt2' as source,
    work.address as res1
FROM user_qt_log;


{
    "user_id":"543462",
    "item_id":"1715",
    "category_id":"1464116",
    "behavior":"pv",
    "ts":"2018-11-26T01:00:00Z",
    "work":{
        "address":"mars",
        "company":"micro"
    }
}
```


3、数组类嵌套 

```sql
INSERT INTO pvuv_sink
SELECT
    DATE_FORMAT(ts, 'yyyy-MM-dd HH:00') dt,
    CAST(1 AS BIGINT) AS pv,
    CAST(1 AS BIGINT) AS uv,
    t.company as source,
    t.address as res1
FROM user_array_log,unnest(work) as t;


{
    "user_id":"543462",
    "item_id":"1715",
    "category_id":"1464116",
    "behavior":"pv",
    "ts":"2018-11-26T01:00:00Z",
    "work":[
        {
            "address":"杭州",
            "company":"阿里"
        },
        {
            "address":"深圳",
            "company":"腾讯"
        }
    ]
}
```

4、双流结合。

- Interval Join: 间隔连接。场景示例: 一个流Join另一个流一段相对时间内的数据。

  在使用时注意以下几点:
  --支持Process Time和Event Time。
  --对超出时间范围的数据会自动清除，避免State过大。
  --支持非等值连接。如>=、>、<=、<、BETWEEN ... AND ... 。

```sql
insert into sink_log
  SELECT a.user_id,a.item_id,a.category_id,a.behavior,a.ts,b.work.address,b.work.company
  FROM  user_log a, user_qt_log  b
  WHERE
    a.user_id = b.user_id
    AND b.ts BETWEEN a.ts - INTERVAL '30' SECOND AND a.ts;
```



- Regular Join:常规Join

  

  在使用时注意以下几点:
  --默认情况下，需要将两个流的输入全部保存在State中。为限制状态无限增长，可通过Query Configuration设置空闲状态保留时长。
  --目前仅支持等值连接。
  --Outer Join(Left Join/Right Join/Full Join)会产生ADD/RETRACT消息，即RetractStream。在输出时注意一下。



-- INNER JOIN

```sql
insert into sink_log
  SELECT user_log.*,user_qt_log.work.address,user_qt_log.work.company
  FROM user_log
       INNER JOIN user_qt_log
        ON user_log.user_id = user_qt_log.user_id;
```



--  LEFT JOIN

```sql
 SELECT * FROM user_log
        LEFT JOIN user_qt_log
        ON user_log.user_id = user_qt_log.user_id;
```

-- RIGHT JOIN

```sql
 SELECT * FROM user_log
        RIGHT JOIN user_qt_log
        ON user_log.user_id = user_qt_log.user_id;
```

-- FULL JOIN

```sql
  SELECT * FROM user_log
        FULL JOIN user_qt_log
        ON user_log.user_id = user_qt_log.user_id;
```



5、维表

```sql
 select t1.user_id,t1.item_id ,t1.proctime,t1.address,t1.company,s1.b ,s1.c ,s1.d 
 from （
      select user_id,item_id,t.address,t.company, PROCTIME() as proctime from user_array_log,unnest(work) as t 
    ) as t1
  join sideTable
  FOR SYSTEM_TIME AS OF t1.proctime s1 on t1.user_id =s1.a
```

