package com.haibao.admin.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nextbreakpoint.flinkclient.model.JarFileInfo;
import com.nextbreakpoint.flinkclient.model.JarUploadResponseBody;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TRes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by baoyu on 2020/2/6.
 * Describe jar包操作服务
 */
public interface JarService extends IService<TRes> {

    /**
     * 作业jar上传flink
     * @param file 网络文件
     * @return
     * */
    Response<JarUploadResponseBody> jarsUpload(MultipartFile file,TRes res);
    /**
     *  查询flink中jar列表
     *  @return jar列表
     * */
    @Deprecated
    Response<List<JarFileInfo>> jars();
    /**
     * 删除flink中的指定jar
     * @param jarid id
     * @return
     * */
    Response deleteJar(Long jarid);
}
