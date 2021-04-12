package com.haibao.admin.scheduler;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haibao.admin.web.service.ISysOperLogService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*
 * @Author ml.c
 * @Description //TODO
 * @Date 19:35 2020-04-22
 **/
@Component
public class LogScheduler {

    private Logger LOGGER = LoggerFactory.getLogger(LogScheduler.class);

    @Autowired
    ISysOperLogService sysOperLogService;

    /**
     * 保留一周左右的操作日志
     */
    @SchedulerLock(name = "schedule_job_synchronization")
    @Scheduled(cron = "0 0 22 1/1 * ?")
    public void scheduleLogCleanup(){
        LocalDateTime oneweek = DateUtil.toLocalDateTime(DateUtil.lastWeek());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.le("oper_time",oneweek);
        sysOperLogService.remove(queryWrapper);
    }
}
