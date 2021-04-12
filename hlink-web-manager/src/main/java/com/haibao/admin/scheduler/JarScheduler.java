package com.haibao.admin.scheduler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nextbreakpoint.flinkclient.model.JarFileInfo;
import com.nextbreakpoint.flinkclient.model.JarListInfo;
import com.haibao.admin.web.common.enums.Contants;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TCluster;
import com.haibao.admin.web.entity.TRes;
import com.haibao.admin.web.service.ClusterService;
import com.haibao.admin.web.service.FlinkApiService;
import com.haibao.admin.web.service.JarService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: c.zh
 * @description: 探测flink jar资源与本地web mysql进行同步
 * @date: 2020/4/10
 **/

@Component
public class JarScheduler {

    private static final Logger logger = LoggerFactory.getLogger(JarScheduler.class);

    @Autowired
    private JarService jarService;

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private FlinkApiService flinkApiService;

    /**
     * jar包同步
     */
    @SchedulerLock(name = "schedule_jar_synchronization")
    @Scheduled(cron = "0 0/5 * * * ?")
    public void scheduleJarSynchronization() {

        //获取所有正常jar包记录
//        QueryWrapper queryWrapper = new QueryWrapper();
//
//        queryWrapper.eq("status",1);
//        List<TRes> jarLists = jarService.list(queryWrapper);

        List<TRes> jarLists = jarService.list();

        //获取所有没有删除的集群
        QueryWrapper clusterQueryWrapper = new QueryWrapper();
        clusterQueryWrapper.eq("del_flag",Contants.NORMAL);
        List<TCluster> clusters=clusterService.list();
        if (null == clusters||clusters.isEmpty()) {
            logger.error("无可用的集群，无法同步作业信息，请检查！");
            return;
        }
        Map<Long, String> clusterMap = clusters.stream().collect(Collectors.toMap(TCluster::getId, TCluster::getAddress));

        Map<Long, List<String>> clusterJarsMap = clusters
                .stream()
                .collect(Collectors.toMap(
                        TCluster::getId,
                        new Function<TCluster, List<String>>() {
                            @Override
                            public List<String> apply(TCluster tCluster) {

                                List<JarFileInfo> list = null;
                                Response<JarListInfo> res2 = null;
                                try {
                                    res2 = flinkApiService.getJarLists(tCluster.getAddress());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (null != res2 && res2.isSuccess()) {
                                    JarListInfo jarListInfo = res2.getData();
                                    if(null != jarListInfo){
                                        list = jarListInfo.getFiles();
                                    }
                                }

                                if(null == list){
                                    return Collections.emptyList();
                                }
                                return list.stream()
                                        .map(jarFileInfo -> jarFileInfo.getId())
                                        .collect(Collectors.toList());
                            }
                        }
                  )
                );

        for (TRes tRes : jarLists) {

            String address = clusterMap.get(tRes.getClusterId());
            String jarId = tRes.getResUname();

            if (StrUtil.isEmpty(address) || StrUtil.isEmpty(jarId)) {
                logger.error(tRes.getResUname() + "-资源信息不完整，请检查！");
                continue;
            }

            List<String> jarFileInfos = clusterJarsMap.get(tRes.getClusterId());
            //集群存在jar包
            if (null != jarFileInfos && jarFileInfos.contains(jarId)) {
                if (1 != tRes.getStatus()){
                    //正常
                    tRes.setStatus(1);
                    jarService.updateById(tRes);
                }
            } else {
                if(0 != tRes.getStatus()){
                    //jar包缺失
                    tRes.setStatus(0);
                    jarService.updateById(tRes);
                }
            }
        }

    }
}
