package com.haibao.admin.web.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.haibao.admin.utils.DateUtil;
import com.haibao.admin.web.service.JobLogService;
import com.haibao.admin.web.entity.TJobLog;
import com.haibao.admin.web.mapper.JobLogMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haibao.flink.log.LogEvent;
import com.haibao.flink.utils.GsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  作业日志跟踪服务实现类
 * </p>
 *
 * @author ml.c
 * @since 2020-04-08
 */
@Service
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, TJobLog> implements JobLogService {

    private Logger logger = LoggerFactory.getLogger(JobLogServiceImpl.class);

    private final static LinkedBlockingQueue<TJobLog> QUEUE=new LinkedBlockingQueue<TJobLog>(50000);

    /**
     * 异步保存日志
     * @param jobId
     * @param gsonString
     * @param logType 保存类型
     * @param msgType  0 json  1 string
     */
    @Override
    public void sendLog(Long jobId, String gsonString, String logType, int msgType) {

//        logger.info("作业{}生成日志：\n{}",jobId,gsonString);

        //非空校验
        if (jobId >0 && StrUtil.isNotEmpty(gsonString)){
            TJobLog tJobLog = new TJobLog();
            tJobLog.setJobId(jobId);
            tJobLog.setJobLog(gsonString);
            tJobLog.setLogType(logType);
            tJobLog.setMsgType(msgType);
            try {
                QUEUE.offer(tJobLog,5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 消费者模式开启
     */
    @PostConstruct
    public void consumeLog() {

        logger.info("----开启作业日志消费----");
        CompletableFuture.runAsync(() -> {
            while (true) {
                if(!QUEUE.isEmpty()){
                    try{
                        TJobLog jobLog = QUEUE.poll(5,TimeUnit.SECONDS);
                        StringBuffer sb = new StringBuffer();

                        //重新处理日志信息
                        String newJobLog = jobLog.getJobLog();

                        //字符串
                        if(1 == jobLog.getMsgType()){
                            sb.append("TIME:").append(cn.hutool.core.date.DateUtil.now()).append(" ").append(jobLog.getJobLog()).append("\n");
                        }
                        //json 重新格式化一下
                       else if(0 == jobLog.getMsgType()){

                            LogEvent logEvent = GsonUtils.gsonToBean(newJobLog,LogEvent.class);
                            sb.append(logEvent.getType()).append(" ")
                                    .append(DateUtil.formatDateTime(logEvent.getTimestamp())).append("\n");

                            sb.append("\n");

                            String logMessage = logEvent.getMessage();
                            Map map = GsonUtils.gsonToMaps(logMessage);
                            map.forEach((k,v)->{
                                sb.append(k).append(":").append(v).append("\n");
                            });

                            sb.append("\n");

                            Map tags = logEvent.getTags();
                            tags.forEach((k,v)->{
                                sb.append(k).append(":").append(v).append("\n");
                            });

                            sb.append("\n");
                        }

                        if("append".equals(jobLog.getLogType())){
                            QueryWrapper queryWrapper = new QueryWrapper();
                            queryWrapper.eq("job_id",jobLog.getJobId());
                            TJobLog oldJobLog = this.getOne(queryWrapper);
                            if(null != jobLog){
                                jobLog.setJobLog(oldJobLog.getJobLog().concat("\n").concat(sb.toString()));
                            }
                        }else{
                            jobLog.setJobLog(sb.toString());
                        }

                        UpdateWrapper updateWrapper = new UpdateWrapper();
                        updateWrapper.eq("job_id",jobLog.getJobId());
                        boolean is = this.saveOrUpdate(jobLog,updateWrapper);
                        logger.info("{}作业日志更新结果{}",jobLog.getJobId(),is);
                    }catch (Exception e){
                        logger.info("作业日志消费异常{}",e.getMessage());
                    }
                }
            }
        });
    }

}
