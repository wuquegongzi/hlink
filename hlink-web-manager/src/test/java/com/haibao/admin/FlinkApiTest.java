package com.haibao.admin;

import com.nextbreakpoint.flinkclient.api.ApiException;
import com.nextbreakpoint.flinkclient.api.FlinkApi;
import com.nextbreakpoint.flinkclient.model.*;
import com.haibao.admin.config.flink.FlinkApiClient;
import com.haibao.flink.utils.GsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName FlinkApiTest
 * @Description
 * @Author ml.c
 * @Date 2020/3/7 2:41 下午
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlinkApiTest {

    @Autowired
    FlinkApiClient flinkApiClient;

    private static String clusterAddress = "http://10.57.30.38:8081";
    private static String jobID = "60a24503f701a22c8e35a09b462167c1";
    private static String taskmanagerID = "8f9ff21f623e6cdf11ed0dbed5d5da3d";


//    @Test
//    public void terminateJobStop() throws ApiException {
//
//        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
//        flinkApi.terminateJob(jobID,"cancel");
//    }

    @Test
    public void getJobAccumulators() throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        JobAccumulatorsInfo jobAccumulatorsInfo = flinkApi.getJobAccumulators(jobID,true);
        System.out.println("JobAccumulatorsInfo:"+ GsonUtils.gsonString(jobAccumulatorsInfo));
    }

    /**
     * 作业配置
     * @throws ApiException
     */
    @Test
    public void getJobConfig() throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        String jobConfig = flinkApi.getJobConfig(jobID);
        System.out.println("jobConfig:"+ jobConfig);
    }

    /**
     * 作业运行详情
     * @throws ApiException
     */
    @Test
    public void getJobDetails() throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        JobDetailsInfo jobDetailsInfo = flinkApi.getJobDetails(jobID);
        System.out.println("JobDetailsInfo:"+ jobDetailsInfo);
    }

    @Test
    public void getJobCheckpoints() throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        CheckpointingStatistics checkpointingStatistics = flinkApi.getJobCheckpoints(jobID);
        System.out.println("CheckpointingStatistics:"+ checkpointingStatistics);
    }

    @Test
    public void getJobExceptions() throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        JobExceptionsInfo jobExceptions = flinkApi.getJobExceptions(jobID);
        System.out.println("JobExceptionsInfo:"+ jobExceptions);
    }

    @Test
    public void getJobPlan() throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        JobPlanInfo obj = flinkApi.getJobPlan(jobID);
        System.out.println("JobPlanInfo:"+ obj);
    }

    @Test
    public void getJobResult() throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        JobExecutionResultResponseBody obj = flinkApi.getJobResult(jobID);
        System.out.println("JobExecutionResultResponseBody:"+ obj);
        System.out.println("JobExecutionResult:"+obj.getJobExecutionResult());
        System.out.println("status"+obj.getStatus());
    }

    @Test
    public void getJobMetrics() throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        Object obj = flinkApi.getJobMetrics(jobID,"");
        System.out.println("JobMetrics:"+ GsonUtils.gsonString(obj));
    }


    @Test
    public void getJobs() throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        JobIdsWithStatusOverview obj = flinkApi.getJobs();
        System.out.println("JobIdsWithStatusOverview:"+ obj);
    }

    @Test
    public void getTaskManagerDetails() throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        TaskManagerDetailsInfo obj = flinkApi.getTaskManagerDetails(taskmanagerID);
        System.out.println("TaskManagerDetailsInfo:"+ obj);
    }

    @Test
    public void getTaskManagersOverview() throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        TaskManagersInfo obj = flinkApi.getTaskManagersOverview();
        System.out.println("TaskManagersInfo:"+ obj);
    }

    /**
     *
     *
     * {id=Status.JVM.GarbageCollector.PS_MarkSweep.Time},
     * {id=Status.JVM.Memory.Mapped.TotalCapacity},
     * {id=taskSlotsAvailable},
     * {id=taskSlotsTotal},
     * {id=Status.JVM.Memory.Mapped.MemoryUsed},
     * {id=Status.JVM.CPU.Time},
     * {id=Status.JVM.Threads.Count},
     * {id=Status.JVM.Memory.Heap.Committed},
     * {id=Status.JVM.GarbageCollector.PS_MarkSweep.Count},
     * {id=Status.JVM.GarbageCollector.PS_Scavenge.Time},
     * {id=Status.JVM.Memory.Direct.Count},
     * {id=Status.JVM.GarbageCollector.PS_Scavenge.Count},
     * {id=Status.JVM.Memory.NonHeap.Max},
     * {id=numRegisteredTaskManagers},
     * {id=Status.JVM.Memory.NonHeap.Committed},
     * {id=Status.JVM.Memory.NonHeap.Used},
     * {id=Status.JVM.Memory.Direct.MemoryUsed},
     * {id=Status.JVM.Memory.Direct.TotalCapacity},
     * {id=numRunningJobs},
     * {id=Status.JVM.ClassLoader.ClassesLoaded},
     * {id=Status.JVM.Memory.Mapped.Count},
     * {id=Status.JVM.CPU.Load},
     * {id=Status.JVM.Memory.Heap.Used},
     * {id=Status.JVM.Memory.Heap.Max},
     * {id=Status.JVM.ClassLoader.ClassesUnloaded}
     *
     * @throws ApiException
     */
    @Test
    public void getJobManagerMetrics() throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        Object obj = flinkApi.getJobManagerMetrics(
                "Status.JVM.GarbageCollector.PS_MarkSweep.Time," +
                        "Status.JVM.Memory.Mapped.TotalCapacity," +
                "taskSlotsTotal," +
                "taskSlotsAvailable," +
                        "Status.JVM.Memory.Mapped.MemoryUsed,"+
                "Status.JVM.Memory.Heap.Used," +
                        "Status.JVM.Memory.Heap.Max"
        );
        System.out.println("Object:"+ GsonUtils.gsonString(obj));
    }

    @Test
    public void getJobsOverview() throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        Object obj = flinkApi.getJobsOverview();
        System.out.println("Object:"+ obj);
    }

    public void getJobsMetrics() throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        Object obj = flinkApi.getJobMetrics(jobID,null);
        System.out.println("Object:"+ obj);
    }




}
