package com.haibao.admin.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.haibao.admin.web.vo.JobVO;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TJob;
import com.haibao.admin.web.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by baoyu on 2020/2/5.
 * Describe 作业管理
 */
@Api(tags = "作业管理",position=5)
@RestController
@RequestMapping("/jobs")
public class JobController extends BaseController{

    @Autowired
    private JobService jobService;

    static final int N_THREADS = Runtime.getRuntime().availableProcessors();
//    ExecutorService executorService = Executors.newFixedThreadPool(nThreads*2);
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
        N_THREADS,N_THREADS*2,10, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(N_THREADS*2),
            new ThreadPoolExecutor.DiscardPolicy());

    @GetMapping("/list")
    @ApiOperation(value = "作业列表查询", position =1)
    public Response<IPage<JobVO>> list(JobVO jobQuery){

//        if(!isAdmin()){
//            jobQuery.setCreateBy(getAccount());
//        }

        return jobService.selectJobs(jobQuery);
    }

    @GetMapping("/{jobId}")
    @ApiOperation(value = "作业详情查询", position =2)
    public Response<JobVO> detail(@PathVariable(value = "jobId") Long jobId){

        return jobService.getJobByID(jobId);
    }

    @PostMapping("/addDO")
    @ApiOperation(value = "新建作业", position =3)
    @ApiOperationSupport(author = "baoyu.xie",
            ignoreParameters = {"id", "jobCode", "jobStatus", "jobStatusName",
                    "createBy", "createTime", "modifyTime", "modifyBy",
                    "flinkJobId", "version", "delFlag"})
    public Response<TJob> createJob(@Valid @RequestBody JobVO jobVO){

        jobVO.setCreateBy(getAccount());
        jobVO.setCreateTime(new Date());

        return jobService.createJob(jobVO);
    }

    @DeleteMapping("/removeDO")
    @ApiOperation(value = "删除作业", position =11)
    @ApiImplicitParam(name = "id",value="作业id",paramType="query",required = true)
    public Response deleteJob(@RequestParam(value = "id",required = true) Long id){

        return jobService.deleteJob(id);
    }

    @PostMapping("/updateDO")
    @ApiOperation(value = "作业信息更新", position =4)
    @ApiOperationSupport(author = "baoyu.xie", ignoreParameters = {"jobCode", "jobStatus", "jobStatusName", "createBy", "createTime", "modifyTime", "modifyBy",
            "flinkJobId", "version", "delFlag"})
    public Response updateJob(@Valid @RequestBody JobVO jobVO){

        jobVO.setCreateBy(getAccount());
        jobVO.setCreateTime(new Date());

        return jobService.updateJob(jobVO);
    }

    @PostMapping("/run/{jobId}")
    @ApiOperation(value = "启动作业", position =5)
    public Response run(@PathVariable(value = "jobId",required = true) Long jobId){

        return jobService.run(jobId);
    }

    /**
     * @author ml.c
     * @param jobIds
     * @return
     */
    @PostMapping("/runBatch")
    @ApiOperation(value = "批量启动作业", position =8)
    public Response runBatch(@RequestParam(value = "jobIds",required = true) Long[] jobIds){

        long start = System.currentTimeMillis();
        List<Long> idList = Stream.of(jobIds).collect(Collectors.toList());

        //设置request 子线程共享
        ServletRequestAttributes att = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(att, true);

        Map<Long,Response> resultMap = new HashMap(idList.size());
        CompletableFuture[] cfs = idList.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> jobService.run(id), threadPool)
                        .whenComplete((s, e) -> {
                            System.out.println("启动任务"+s+"完成!result="+s+"，异常 e="+e+","+new Date());
                            resultMap.put(id,s);
                        })
                ).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(cfs).join();

        System.out.println("启动 list="+resultMap+",耗时="+(System.currentTimeMillis()-start));

        Map failMap = resultMap.entrySet().stream().filter(entry -> (
                !entry.getValue().isSuccess()
        )).collect(Collectors.toMap(
                entry1 -> entry1.getKey(),
                entry2 -> entry2.getValue()
        ));

        if(failMap.size() > 0){
             return Response.success(failMap);
        }

        return Response.success();
    }

    @PostMapping("/cancel/{jobId}")
    @ApiOperation(value = "取消作业", position =6)
    public Response cancel(@PathVariable(value = "jobId") Long jobId){

        return jobService.cancel(jobId);
    }

    /**
     * @author ml.c
     * @param jobIds
     * @return
     */
    @PostMapping("/cancelBatch")
    @ApiOperation(value = "批量停止作业", position =9)
    public Response cancelBatch(@RequestParam(value = "jobIds") Long[] jobIds){

        Map resultMap = new LinkedHashMap<>();
        for (int i = 0; i < jobIds.length; i++) {
            Response response =  jobService.cancel(jobIds[i]);
            if(!response.isSuccess()){
               resultMap.put(jobIds[i],response);
            }
        }

        if(resultMap.size() > 0){
            return Response.success(resultMap);
        }

        return Response.success();
    }

    /**
     * @author ml.c
     * @param jobId
     * @return
     */
    @PostMapping("/restart/{jobId}")
    @ApiOperation(value = "重启作业", position =7)
    public Response restart(@PathVariable(value = "jobId") Long jobId){

        return jobService.restart(jobId);
    }

    /**
     * @author ml.c
     * @param jobIds
     * @return
     */
    @PostMapping("/restartBatch")
    @ApiOperation(value = "批量重启作业", position =10)
    public Response restartBatch(@RequestParam(value = "jobIds") Long[] jobIds){

        long start = System.currentTimeMillis();
        List<Long> idList = Stream.of(jobIds).collect(Collectors.toList());

        //设置request 子线程共享
        ServletRequestAttributes att = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(att, true);

        Map<Long,Response> resultMap = new HashMap(idList.size());
        CompletableFuture[] cfs = idList.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> jobService.restart(id), threadPool)
                        .whenComplete((s, e) -> {
                            System.out.println("重启任务"+s+"完成!result="+s+"，异常 e="+e+","+new Date());
                            resultMap.put(id,s);
                        })
                ).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(cfs).join();

        System.out.println("list="+resultMap+",耗时="+(System.currentTimeMillis()-start));

        Map failMap = resultMap.entrySet().stream().filter(entry -> (
                !entry.getValue().isSuccess()
        )).collect(Collectors.toMap(
                entry1 -> entry1.getKey(),
                entry2 -> entry2.getValue()
        ));

        if(failMap.size() > 0){
            return Response.success(failMap);
        }

        return Response.success();
    }

    @GetMapping("/overview/{jobId}")
    @ApiOperation(value = "获取作业视图", position =12)
    public Response overview(@PathVariable(value = "jobId") Long jobId){

        return jobService.overview(jobId);
    }

    @GetMapping("/savepoint/{jobId}")
    @ApiOperation(value = "触发作业保存点",position = 13)
    public Response triggerSavePoint(@PathVariable(value = "jobId") Long jobId){
        return jobService.triggerSavePoint(jobId);
    }

    @GetMapping("/{jobId}/log")
    @ApiOperation(value = "作业日志",position = 14)
    public Response jobLog(@PathVariable(value = "jobId") Long jobId){
        return jobService.getLog(jobId);
    }
}
