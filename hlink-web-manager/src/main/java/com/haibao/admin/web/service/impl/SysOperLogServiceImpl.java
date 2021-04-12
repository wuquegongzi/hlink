package com.haibao.admin.web.service.impl;

import com.haibao.admin.web.entity.SysOperLog;
import com.haibao.admin.web.mapper.SysOperLogMapper;
import com.haibao.admin.web.service.ISysOperLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-04-22
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysOperLogService {

    private final static LinkedBlockingQueue<SysOperLog> QUEUE=new LinkedBlockingQueue<SysOperLog>(100000);

    private Logger LOGGER = LoggerFactory.getLogger(SysOperLogServiceImpl.class);

    @Override
    public void sendLog(SysOperLog sysOperLog) {
        try {
            QUEUE.offer(sysOperLog,5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 消费者模式开启
     */
    @PostConstruct
    public void consumeLog() {

        LOGGER.info("----开启操作日志消费----");
        CompletableFuture.runAsync(() -> {
            while (true) {
                if(!QUEUE.isEmpty()){
                    try{
                       SysOperLog sysOperLog = QUEUE.poll(5,TimeUnit.SECONDS);
                       this.save(sysOperLog);
                    }catch (Exception e){
                        LOGGER.info("操作日志消费异常{}",e.getMessage());
                    }
                }
            }
        });
    }
}
