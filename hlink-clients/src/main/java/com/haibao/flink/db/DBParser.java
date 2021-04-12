package com.haibao.flink.db;

import cn.hutool.core.util.StrUtil;
import com.haibao.flink.model.Ds;
import com.haibao.flink.model.DsSchemaColumn;
import com.haibao.flink.model.Job;
import com.haibao.flink.model.JobDs;
import com.haibao.flink.model.Udf;
import com.haibao.flink.utils.GsonUtils;
import java.util.List;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * @ClassName DBParser
 * @Description 数据库数据解析器
 * @Author ml.c
 * @Date 2020/3/4 6:16 下午
 * @Version 1.0
 */
public class DBParser {
    static final Logger LOG = LoggerFactory.getLogger(DBParser.class);

    private String jobID;
    private String profilesActive;

    public DBParser(String jobID,String profilesActive) {
        this.jobID = jobID;
        this.profilesActive = profilesActive;
    }

    public static void main(String[] args) throws Exception {
        DBParser dbParser = new DBParser("47","test");
        Job job = dbParser.aggregate();
        System.out.println(GsonUtils.gsonString(job));
    }

    public Job aggregate() throws Exception {

        if(StrUtil.isEmpty(jobID)){
            return null;
        }

        Job job = null;

        //参数文件包括作业配置信息、维表相关等配置
        JdbcUtils jdbcUtils = new JdbcUtils();
        Connection con = jdbcUtils.getCon(profilesActive);

        try {

            String confSql = "select * from t_job where id="+jobID;

            String featureSql = "select id,ds_type,ds_id,job_id,\"\" as run_sql from t_job_ds where job_id ="+jobID+" \n" +
                    "union all \n" +
                    "select id,\"sink\" as ds_type,ds_id,job_id,run_sql from t_job_ds_sink  where job_id ="+jobID;

            String dsSql = "select t1.*,t2.json_value from t_ds t1 left join t_ds_json_field t2 on t1.id = t2.ds_id  where t1.id = ? ";

            String dsSchemaColumnSql = "select * from t_ds_schema_column where ds_id = ? ";

            String udfSql = "select * from t_udf where delete_flag = 0";

            QueryRunner runner = new QueryRunner();
            //开启下划线->驼峰转换所用
            BeanProcessor bean = new GenerousBeanProcessor();
            RowProcessor processor = new BasicRowProcessor(bean);

            //获取作业配置详情
            job = runner.query(con,confSql,new BeanHandler<Job>(Job.class,processor));

            if(null == job){
                LOG.error("未获取到任务信息");
                return null;
            }

            //获取作业相关数据源信息
            List<JobDs> jobDsList = runner.query(con,featureSql,new BeanListHandler<JobDs>(JobDs.class,processor));

            jobDsList.forEach(jobDs -> {
                try {
                    Ds ds = runner.query(con,dsSql,new BeanHandler<Ds>(Ds.class,processor),jobDs.getDsId());

                    List<DsSchemaColumn> dsSchemaColumnList = runner.query(con,dsSchemaColumnSql,new BeanListHandler<DsSchemaColumn>(DsSchemaColumn.class,processor),ds.getId());

                    ds.setDsSchemaColumnList(dsSchemaColumnList);
                    jobDs.setDs(ds);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            job.setJobDsList(jobDsList);

            //获取UDF,并且匹配作业特征加工
            List<Udf> udfs = runner.query(con,udfSql,new BeanListHandler<Udf>(Udf.class,processor));
            List<Udf> udfList = udfs.stream().filter(udf -> {
                long count = jobDsList.stream().filter(jobDs -> {
                    if(StrUtil.isNotEmpty(jobDs.getRunSql()) && jobDs.getRunSql().contains(udf.getUdfName())){
                        return true;
                    }
                    return false;
                }).count();

                if(count > 0){
                    return  true;
                }
                return false;
            }).collect(Collectors.toList());

            job.setUdfList(udfList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.close(con);
        }

        return job;
    }


}
