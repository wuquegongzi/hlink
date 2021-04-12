package com.haibao.admin.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.haibao.admin.utils.OkHttpUtil;
import com.haibao.admin.web.vo.ClusterVO;
import com.haibao.admin.web.common.enums.Contants;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TCluster;
import com.haibao.admin.web.service.ClusterService;
import com.haibao.admin.web.service.JarService;
import com.haibao.admin.web.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 集群管理层
 */
@Api(tags = "集群管理",position=2)
@Validated
@RestController
@RequestMapping("/cluster")
public class ClusterController extends BaseController{

    @Autowired
    ClusterService clusterService;

    @Autowired
    JarService jarService;

    @Autowired
    JobService jobService;

    /**
     * 集群列表
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "集群列表分页查询")
    @ApiOperationSupport(author = "c.zh",
            ignoreParameters = {"id", "delFlag", "taskManagerCount", "slotTotal", "slotAvailableCount", "slotRunningCount", "jobCompleteCount", "jobCancelCount",
                    "jobFailureCount", "parms","serialVersionUID"})
    Response<IPage<TCluster>> clusterList(ClusterVO clusterVO){

        QueryWrapper<TCluster> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag",Contants.NORMAL);
        Page<TCluster> page = new Page<>(clusterVO.getCurPage(), clusterVO.getPageSize());
        IPage<TCluster> clusters =  clusterService.page(page,queryWrapper);
        return Response.success(clusters);
    }

    /**
     * 集群下拉列表
     * @return
     */
    @GetMapping("/check/list")
    @ApiOperation(value = "集群下拉列表查询(无分页)")
    @ApiOperationSupport(author = "c.zh")
    Response<List<TCluster>> clusterCheckList(ClusterVO clusterVO){

        QueryWrapper<TCluster> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag",Contants.NORMAL);
        List<TCluster> clusters =  clusterService.list(queryWrapper);
        return Response.success(clusters);
    }

    @GetMapping("/info")
    @ApiOperationSupport(author = "c.zh")
    @ApiOperation(value = "单个集群详细信息查询")
    @ApiImplicitParam(name = "clusterId",value = "集群Id",paramType = "query",required = true)
    Response<ClusterVO> clusterInfo(@NotNull Long clusterId){
        return clusterService.info(clusterId);
    }


    /**
     * 集群新增
     * @return
     */
    @PostMapping("/add")
    @ApiOperationSupport(author = "c.zh")
    @ApiOperation(value = "集群添加")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "name",value = "集群名称",paramType = "query",required = true),
                    @ApiImplicitParam(name = "type",value = "集群类型",paramType = "query",required = true),
                    @ApiImplicitParam(name = "address",value = "集群地址",paramType = "query",required = true),
                    @ApiImplicitParam(name = "remark",value = "备注",paramType = "query",required = false)})
    Response clusterAdd(@NotEmpty(message = "集群名称不可为空")
                        @Size(max = 64,message = "集群名称最大长度不可超过64")
                                String name,
                        @NotNull Integer type,
                        @NotEmpty(message = "集群地址不可为空")
                        @Size(max = 100,message = "集群地址最大长度不可超过100")
                                String address,
                        @Size(max = 200,message = "备注长度不可大于200")
                                String remark) {

        QueryWrapper<TCluster> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        queryWrapper.eq("del_flag",Contants.NORMAL);
        int count=clusterService.count(queryWrapper);
        if(count>0){
            return Response.error(0,"集群名称重复");
        }
        TCluster tCluster=new TCluster();
        tCluster.setName(name);
        tCluster.setAddress(address);
        tCluster.setType(type);
        tCluster.setRemark(remark);
        tCluster.setDelFlag(Contants.NORMAL);
        Date date=new Date();
        tCluster.setCreateTime(date);
        tCluster.setModifyTime(date);
        tCluster.setCreateBy(getAccount());

        okhttp3.Response response= null;
        try {
            response = OkHttpUtil.httpAccessibility(address+"/config",null);
        } catch (Exception e) {
            return  Response.error(0,"集群地址不可用");
//            e.printStackTrace();
        }
        if(response==null||!response.isSuccessful()){
            return  Response.error(0,"集群地址不可用");
        }
        if(clusterService.save(tCluster)){
            return  Response.success();
        }else {
            return  Response.error(0,"集群添加失败");
        }
    }

    /**
     * 集群修改
     * @return
     */
    @PutMapping("/update")
    @ApiOperationSupport(author = "c.zh")
    @ApiOperation(value = "集群修改")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "name",value = "集群名称",paramType = "query"),
                    @ApiImplicitParam(name = "address",value = "集群地址",paramType = "query"),
                    @ApiImplicitParam(name = "id",value = "集群编号",paramType = "query",required = true),
                    @ApiImplicitParam(name = "remark",value = "描述",paramType = "query",required = false)})
    Response clusterUpdate(
            @NotEmpty(message = "集群名称不可为空")
            @Size(max = 64,message = "集群名称最大长度不可超过64")
            String name,
            @NotNull(message = "ID缺失")
            Long id,
            @NotEmpty(message = "集群地址不可为空")
            @Size(max = 100,message = "集群地址最大长度不可超过100")
            String address,
            @Size(max = 200,message = "备注长度不可大于200")
                    String remark) {
        QueryWrapper<TCluster> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        queryWrapper.eq("del_flag",Contants.NORMAL);
        queryWrapper.ne("id", id);
        int count=clusterService.count(queryWrapper);
        if(count>0){
            return Response.error(0,"集群名称重复");
        }
        TCluster tCluster=new TCluster();
        tCluster.setName(name);
        tCluster.setAddress(address);
        tCluster.setId(id);
        tCluster.setRemark(remark);
        tCluster.setModifyTime(new Date());
        tCluster.setModifyBy(getAccount());
        okhttp3.Response response= null;
        try {
            response = OkHttpUtil.httpAccessibility(address,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(response==null||!response.isSuccessful()){
            return  Response.error(0,"集群地址不可用");
        }
        if(clusterService.updateById(tCluster)){
            return  Response.success();

        }else {
            return  Response.error(0,"集群更新失败");
        }
    }

    /**
     * 集群删除
     * @return
     */
    @DeleteMapping("/delete")
    @ApiOperationSupport(author = "c.zh")
    @ApiOperation(value = "集群删除")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id",value = "集群编号",paramType = "query",required = true)})
    Response clusterDelete(Long id) {
        TCluster tCluster=new TCluster();
        tCluster.setId(id);
        tCluster.setDelFlag(Contants.DELETE);
        QueryWrapper jarQueryWrapper=new QueryWrapper();
        jarQueryWrapper.eq("cluster_id",id);
        int jarCount =jarService.count(jarQueryWrapper);
        if(jarCount > 0){
            return Response.error(0,"资源包存在该集群的引用，无法删除");
        }
        QueryWrapper jobQueryWrapper=new QueryWrapper();
        jobQueryWrapper.eq("cluster_id",id);
        jobQueryWrapper.eq("del_flag",1);
        int jobCount =jobService.count(jobQueryWrapper);
        if(jobCount > 0){
            return Response.error(0,"作业中存在该集群的引用，无法删除");
        }
        boolean status =clusterService.updateById(tCluster);
        if(status){
            return Response.success();
        }else {
            return Response.error(0,"集群删除失败");
        }
    }

}
