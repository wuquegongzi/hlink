package com.haibao.admin.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haibao.admin.web.vo.ClusterVO;
import com.nextbreakpoint.flinkclient.model.ClusterOverviewWithVersion;
import com.haibao.admin.web.common.enums.CodeEnum;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.mapper.ClusterMapper;
import com.haibao.admin.web.entity.TCluster;
import com.haibao.admin.web.service.ClusterService;
import com.haibao.admin.web.service.FlinkApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-02-15
 */
@Service
public class ClusterServiceImpl extends ServiceImpl<ClusterMapper, TCluster> implements ClusterService {

    @Autowired
    FlinkApiService flinkApiService;

    @Override
    public Response<ClusterVO> info(Long clusterId) {
        if(clusterId==null){
            return Response.error(CodeEnum.PARAM_ERROR);
        }
        TCluster tCluster = this.getById(clusterId);
        if(tCluster==null){
            return Response.error(CodeEnum.CLUSTER_INFO_IS_NULL);
        }
        ClusterVO clusterVO=new ClusterVO();
        BeanUtil.copyProperties(tCluster,clusterVO);

        Response<ClusterOverviewWithVersion> response = flinkApiService.getOverview(tCluster.getAddress());

        if(!response.isSuccess()){
            return Response.error(CodeEnum.GET_FLINK_OVERVIEW_ERROR);
        }

        ClusterOverviewWithVersion clusterOverviewWithVersion=response.getData();
        //总slot
        Integer slotTotal=clusterOverviewWithVersion.getSlotsTotal();
        clusterVO.setSlotTotal(slotTotal);
        //可用slot
        Integer slotAvailableCount=clusterOverviewWithVersion.getSlotsAvailable();
        clusterVO.setSlotAvailableCount(slotAvailableCount);
        //运行中slot
        Integer runningSlot=slotTotal-slotAvailableCount;
        clusterVO.setSlotRunningCount(runningSlot);

        clusterVO.setTaskManagerCount(clusterOverviewWithVersion.getTaskmanagers());
        clusterVO.setJobCompleteCount(clusterOverviewWithVersion.getJobsFinished());
        clusterVO.setJobCancelCount(clusterOverviewWithVersion.getJobsCancelled());
        clusterVO.setJobFailureCount(clusterOverviewWithVersion.getJobsFailed());
        return Response.success(clusterVO);
    }
}
