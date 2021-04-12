package com.haibao.admin.web.controller;

import com.nextbreakpoint.flinkclient.model.ClusterOverviewWithVersion;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TCluster;
import com.haibao.admin.web.service.ClusterService;
import com.haibao.admin.web.service.FlinkApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: c.zh
 * @description: 集群总览前端控制器
 * @date: 2020/2/28
 **/
@Api(tags = "总览",position=1)
@RestController
@RequestMapping("/dashboard")
public class DashboardController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    ClusterService clusterService;

    @Autowired
    FlinkApiService flinkApiService;


    @GetMapping("/clusterInfo")
    @ApiOperation(value = "集群总览接口")
    Response<ClusterOverviewWithVersion> getClusterInfo(String clusterId) {
        TCluster tCluster = clusterService.getById(clusterId);
        if (tCluster == null) {
            return Response.error(0, "集群不存在");
        }

        return flinkApiService.getOverview(tCluster.getAddress());
    }

    /**
     * baoyu
     * @return
     */
//    @Override
//    public Response getManagerConfig() {
//        try {
//            String result=flinkApiComponent.getJobManagerConfig();
//            JSONArray jsonArray= JSONArray.parseArray(result);
//            return Response.success(jsonArray);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return Response.error(CodeEnum.GET_JOB_MANAGER_CONIG_ERROR);
//        }
//    }
}
