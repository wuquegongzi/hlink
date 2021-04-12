package com.haibao.admin.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haibao.admin.web.vo.JobVO;
import com.haibao.admin.web.entity.TJob;
import com.haibao.admin.web.common.result.Response;

/**
 * Created by baoyu on 2020/2/5.
 * Describe
 */
public interface JobService extends IService<TJob> {

    /**
     * 创建作业
     * */
    Response<TJob> createJob(JobVO jobVO);
    /**
     * 查询作业列表
     * @param jobQuery 查询信息
     * @return 分页作业信息列表
     * */
    Response<IPage<JobVO>> selectJobs(JobVO jobQuery);

    /**
     * 删除作业
     * @param jobId 作业Id
     * @return
     * */
    Response deleteJob(Long jobId);
    /**
     * 作业启动
     * @param jobId 作业Id
     * @return
     * */
    Response run(Long jobId);
    /**
     * 取消正在运行的作业
     * @param
     * @return
     * */
    Response cancel(Long jobId);
    /**
     * 获取运行中和已完成的作业列表
     * return
     * */
    Response overview(Long jobId);
    /**
     * 作业更新
     * @param jobVO 更新信息
     * */
    Response updateJob(JobVO jobVO);

    /**
     * 获取明细
     * @param jobId
     * @return
     */
    Response<JobVO> getJobByID(Long jobId);

    Response restart(Long jobId);
    /**
     * 触发作业保存点设置
     * @param jobId
     * @return
     * */
    Response triggerSavePoint(Long jobId);

    Response getLog(Long jobId);
}
