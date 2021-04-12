package com.haibao.flink.model;

import java.io.Serializable;

/**
 * <p>
 * 作业对应的数据源选择结果,源表、维表、目标表 共用 视图实体
 * 分别 对应持久层 :
 * 源表、维表  -> TJobDs
 * 目标表 -> TJobDsSink
 * </p>
 *
 * @author jobob
 * @since 2020-03-05
 */
public class JobDs implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 元数据类型  源表、结果表、维表、视图、自定义函数 
     */
    private String dsType;

    /**
     * 选择的对应类型ID
     */
    private Long dsId;

    private Long jobId;

    private String runSql;

    /**
     * 数据源信息
     */
    private Ds ds;

    public Ds getDs() {
        return ds;
    }

    public void setDs(Ds ds) {
        this.ds = ds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDsType() {
        return dsType;
    }

    public void setDsType(String dsType) {
        this.dsType = dsType;
    }

    public Long getDsId() {
        return dsId;
    }

    public void setDsId(Long dsId) {
        this.dsId = dsId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getRunSql() {
        return runSql;
    }

    public void setRunSql(String runSql) {
        this.runSql = runSql;
    }
}
