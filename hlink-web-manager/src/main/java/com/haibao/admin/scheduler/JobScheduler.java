package com.haibao.admin.scheduler;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nextbreakpoint.flinkclient.model.JobDetailsInfo;
import com.nextbreakpoint.flinkclient.model.JobDetailsInfoJobVertexDetailsInfo;
import com.haibao.admin.utils.ApplicationContextUtil;
import com.haibao.admin.web.common.enums.JobStatusEnum;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TCluster;
import com.haibao.admin.web.entity.TJob;
import com.haibao.admin.web.service.ClusterService;
import com.haibao.admin.web.service.FlinkApiService;
import com.haibao.admin.web.service.JobService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName JobScheduler
 * @Description 作业调度器
 * @Author ml.c
 * @Date 2020/3/7 10:12 下午
 * @Version 1.0
 *
 * CRON 表达式 http://www.bejson.com/othertools/cron/
 */
@Component
public class JobScheduler {

    private static final Logger logger = LoggerFactory.getLogger(JobScheduler.class);

    @Autowired
    private JobService jobService;
    @Autowired
    private ClusterService clusterService;

//    @Autowired
//    private FlinkApiService flinkApiService;

    /**
     * 心跳探测，作业状态同步
     */
    @SchedulerLock(name = "schedule_job_synchronization")
    @Scheduled(cron = "0 0/2 * * * ?")
    public void scheduleJobSynchronization(){

       //获取所有非未运行状态的作业
        QueryWrapper queryWrapper = new QueryWrapper();
        List list = new ArrayList(3);
        //未提交
        list.add(JobStatusEnum.UNCOMMITTED.getStatus());
        //启动中
        list.add(JobStatusEnum.STARTING.getStatus());
        //停止中
        list.add(JobStatusEnum.CANCELLING.getStatus());

        queryWrapper.notIn("job_status",list);

        List<TJob> jobLists = jobService.list(queryWrapper);

        if(null == jobLists){
            return;
        }

        Map<Long, String> clusterMap = clusterService.list().stream().collect(Collectors.toMap(TCluster::getId, TCluster::getAddress));
        if(null == clusterMap){
            logger.error("无可用的集群，无法同步作业信息，请检查！");
            return;
        }

        //心跳探测集群内作业健康状态
        for (TJob tJob : jobLists) {

            String address = clusterMap.get(tJob.getClusterId());
            String flinkJobId = tJob.getFlinkJobId();
            int jobStatus = tJob.getJobStatus();
            Date modifyTime = tJob.getModifyTime(); //lock

            if(StrUtil.isEmpty(address) || StrUtil.isEmpty(flinkJobId)){

                logger.error("作业:{},信息不完整，请检查！",tJob.getJobName());
                continue;
            }

            FlinkApiService flinkApiService = (FlinkApiService) ApplicationContextUtil.getBean("flinkApiService");
            Response<JobDetailsInfo> jobDetailsInfoResponse = flinkApiService.getJobDetails(address,flinkJobId);

            //匹配作业状态，同步作业信息
            if(!jobDetailsInfoResponse.isSuccess()){

                //直接失败
                if(JobStatusEnum.CANCELED.getStatus() != jobStatus
                        && JobStatusEnum.FAILED.getStatus() != jobStatus
                        && JobStatusEnum.FINISHED.getStatus() != jobStatus){

                    logger.error("作业：{},远程获取作业信息失败！失败码：{};失败信息：{}",
                            tJob.getJobName(),
                            jobDetailsInfoResponse.getCode(),
                            jobDetailsInfoResponse.getMsg());

                    TJob tJobNew = new TJob();
                    tJobNew.setJobStatus(JobStatusEnum.FAILED.getStatus());

                    UpdateWrapper<TJob> wrapper = new UpdateWrapper<TJob>();
                    wrapper.eq("id", tJob.getId());
                    wrapper.eq("modify_time", modifyTime);

                    boolean is =  jobService.update(tJobNew,wrapper);
                    logger.info("作业：{}|| 更新状态：{}||直接失败更新 结果：{}",tJob.getJobName(),tJobNew.getJobStatus(),is);
                }

                continue;
            }

            JobDetailsInfo jobDetailsInfo = jobDetailsInfoResponse.getData();
            JobDetailsInfo.StateEnum stateEnum = jobDetailsInfo.getState();

            //最终状态  已停止/已失败/已完成
            if(JobDetailsInfo.StateEnum.CANCELED.getValue().equals(stateEnum.getValue())
            || JobDetailsInfo.StateEnum.FAILED.getValue().equals(stateEnum.getValue())
            || JobDetailsInfo.StateEnum.FINISHED.getValue().equals(stateEnum.getValue())
            ){
               //如果作业不是最终状态，则需要更新，需要乐观锁更新
               if(JobStatusEnum.CANCELED.getStatus() != jobStatus
                       && JobStatusEnum.FAILED.getStatus() != jobStatus
                       && JobStatusEnum.FINISHED.getStatus() != jobStatus){

                   TJob tJobNew = new TJob();
                   tJobNew.setJobStatus(JobStatusEnum.getStatusByValue(stateEnum.getValue()));

                   UpdateWrapper<TJob> wrapper = new UpdateWrapper<TJob>();
                   wrapper.eq("id", tJob.getId());
                   wrapper.eq("modify_time", modifyTime);

                   boolean is =  jobService.update(tJobNew,wrapper);
                   logger.info("作业：{}|| 更新状态：{}-{}||结果：{}",tJob.getJobName(),tJobNew.getJobStatus(),stateEnum.getValue(),is);
               }
            }else{
                //比对作业状态是否无需更改
                if(jobStatus != JobStatusEnum.getStatusByValue(stateEnum.getValue())){
                    TJob tJobNew = new TJob();
                    tJobNew.setJobStatus(JobStatusEnum.getStatusByValue(stateEnum.getValue()));

                    UpdateWrapper<TJob> wrapper = new UpdateWrapper<TJob>();
                    wrapper.eq("id", tJob.getId());
                    wrapper.eq("modify_time", modifyTime);

                    boolean is = jobService.update(tJobNew,wrapper);
                    logger.info("作业：{}|| 更新状态：{}-{}||结果：{}",tJob.getJobName(),tJobNew.getJobStatus(),stateEnum.getValue(),is);
                }
             }

            //更新计算单元
            List<JobDetailsInfoJobVertexDetailsInfo>  vertexDetailsInfos = jobDetailsInfo.getVertices();
            if(null == vertexDetailsInfos){
                vertexDetailsInfos = new ArrayList<>(0);
            }
            if(null == tJob.getComputingUnit() || tJob.getComputingUnit() != vertexDetailsInfos.size()){
                TJob tJobNew = new TJob();
                tJobNew.setComputingUnit(vertexDetailsInfos.size());
                UpdateWrapper<TJob> wrapper = new UpdateWrapper<TJob>();
                wrapper.eq("id", tJob.getId());
                wrapper.eq("modify_time", modifyTime);
                jobService.update(tJobNew,wrapper);
            }

            continue;
        }
    }

    /**
     * 中间状态  补偿
     */
    @SchedulerLock(name = "schedule_job_makeup")
    @Scheduled(cron = "0 0/5 * * * ?")
    public void jobStatusMakeup(){

        //获取所有中间状态的作业
        QueryWrapper queryWrapper = new QueryWrapper();
        List list = new ArrayList(3);

        //启动中
        list.add(JobStatusEnum.STARTING.getStatus());
        //停止中
        list.add(JobStatusEnum.CANCELLING.getStatus());

        queryWrapper.in("job_status",list);

        List<TJob> jobLists = jobService.list(queryWrapper);

        if(null == jobLists){
            return;
        }

        jobLists.forEach(tJob -> {

            int jobStatus = tJob.getJobStatus();
            Date modifyTime = tJob.getModifyTime(); //lock

            //查看更新时间，如果超出半小时，自动失败
            if((JobStatusEnum.STARTING.getStatus() == jobStatus || JobStatusEnum.CANCELLING.getStatus() == jobStatus)
                    && DateUtil.between(modifyTime,new Date(), DateUnit.MINUTE) > 30){

                TJob tJobNew = new TJob();
                tJobNew.setJobStatus(JobStatusEnum.FAILED.getStatus());
                tJobNew.setModifyBy("system");
                tJobNew.setModifyTime(new Date());

                UpdateWrapper<TJob> wrapper = new UpdateWrapper<TJob>();
                wrapper.eq("id", tJob.getId());
                wrapper.eq("modify_time", modifyTime);

                boolean is =  jobService.update(tJobNew,wrapper);
                logger.info("作业：{}, 中间状态超过半小时|| 更新状态：{}||直接失败更新 结果：{}",tJob.getJobName(),tJobNew.getJobStatus(),is);
            }

        });

    }

}
