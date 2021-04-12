package com.haibao.admin.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haibao.admin.utils.FlinkUtils;
import com.haibao.admin.utils.TDFileUtils;
import com.nextbreakpoint.flinkclient.api.ApiException;
import com.nextbreakpoint.flinkclient.api.FlinkApi;
import com.nextbreakpoint.flinkclient.model.JarFileInfo;
import com.nextbreakpoint.flinkclient.model.JarListInfo;
import com.nextbreakpoint.flinkclient.model.JarUploadResponseBody;
import com.haibao.admin.web.common.enums.CodeEnum;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TCluster;
import com.haibao.admin.web.entity.TRes;
import com.haibao.admin.web.mapper.ClusterMapper;
import com.haibao.admin.web.mapper.JobMapper;
import com.haibao.admin.web.mapper.ResMapper;
import com.haibao.admin.web.service.FlinkApiService;
import com.haibao.admin.web.service.JarService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by baoyu on 2020/2/6.
 * Describe
 */
@Service
public class JarServiceImpl extends ServiceImpl<ResMapper, TRes> implements JarService {

    @Autowired
    private ResMapper resMapper;

    @Autowired
    private ClusterMapper clusterMapper;

    @Autowired
    private FlinkApiService flinkApiService;

    @Autowired
    private JobMapper jobMapper;

    @Value("${spring.servlet.multipart.location}")
    private String fromTempDir;

    @Override
    public Response<JarUploadResponseBody> jarsUpload(MultipartFile multfile,TRes res) {
        String fileName = multfile.getOriginalFilename();
        if(!StringUtils.equalsIgnoreCase("jar", TDFileUtils.getFileType(fileName))){
            return Response.error(CodeEnum.UPLOAD_LOCAL_ERROR);
        }
        Long fileSize=multfile.getSize();
        File file=null;
        File dirFile=null;
        try {
            TCluster tCluster=clusterMapper.selectById(res.getClusterId());
            if(tCluster==null){
                return Response.error(CodeEnum.UPLOAD_REMOTE_ERROR);
            }
            //生成一个唯一目录路径，避免文件覆盖
            String dirPath=fromTempDir+File.separator+UUID.randomUUID().toString();
            dirFile=new File(dirPath);
            if(!dirFile.exists()){
                dirFile.mkdir();
            }
            file=new File(dirPath+File.separator+fileName);
            if(file.exists()){
                file.delete();
            }
            //jar包上传，multipartFile转file
            multfile.transferTo(file);
            //通过file提交到flink
            JarUploadResponseBody responseBody = flinkApiService.uploadJar(FlinkUtils.getIPAndPort(tCluster.getAddress()),file);

            if(responseBody.getStatus()== JarUploadResponseBody.StatusEnum.SUCCESS){
                TRes tRes=new TRes();
                tRes.setResName(fileName);
                tRes.setResType(0);
                tRes.setResSize(fileSize);
                tRes.setModifyTime(res.getCreateTime());
                tRes.setCreateTime(res.getCreateTime());
                tRes.setCreateBy(res.getCreateBy());
                tRes.setStatus(1);
                tRes.setClusterId(tCluster.getId());
                int index=responseBody.getFilename().lastIndexOf("/");
                tRes.setResUname(responseBody.getFilename().substring(index+1,responseBody.getFilename().length()));
                int  count=resMapper.insert(tRes);
                if(count>0){
                    return Response.success();
                }else {
                    flinkApiService.deleteJar(FlinkUtils.getIPAndPort(tCluster.getAddress()),tRes.getResUname());
                    return Response.error(CodeEnum.UPLOAD_REMOTE_ERROR);
                }
            }else {
                return Response.error(CodeEnum.UPLOAD_REMOTE_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(CodeEnum.UPLOAD_REMOTE_ERROR);
        }finally {
            if(file!=null){
                file.delete();
            }
            if(dirFile!=null){
                dirFile.delete();
            }
        }
    }

    @Override
    public Response<List<JarFileInfo>> jars() {
        try {
            //flink获取jar列表
            FlinkApi flinkApi=new FlinkApi();
            JarListInfo jarListInfo=flinkApi.listJars();
            List<JarFileInfo> jarFileInfoList = jarListInfo.getFiles();
            return Response.success(jarFileInfoList);
        } catch (ApiException e) {
            e.printStackTrace();
            return Response.error(CodeEnum.GET_JARS_ERROR);
        }

    }

    @Override
    public Response deleteJar(Long jarid) {
        try {
            TRes tRes=resMapper.selectById(jarid);
            if (null==tRes){
                return Response.error(CodeEnum.DELETE_JARS_ERROR);
            }
            TCluster tCluster=clusterMapper.selectById(tRes.getClusterId());
            if(tCluster==null){
                return Response.error(CodeEnum.DELETE_JARS_ERROR);
            }
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("cluster_id",tRes.getClusterId());
            queryWrapper.eq("del_flag",1);
            queryWrapper.eq("use_jar",tRes.getResUname());
            int jobCount =jobMapper.selectCount(queryWrapper);
            if(jobCount > 0){
                return Response.error(-2,"存在作业引用该资源包，无法删除");
            }
            //判断集群中是否还存在，防止远程集群删除异常
            Response<JarListInfo> jarListInfoResponse = null;
            try {
                jarListInfoResponse = flinkApiService.getJarLists(FlinkUtils.getIPAndPort(tCluster.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(null == jarListInfoResponse || !jarListInfoResponse.isSuccess()){
                return Response.error(-2,"集群不可用，请检查！");
            }
            JarListInfo jarListInfo = jarListInfoResponse.getData();
            List<JarFileInfo> jarFileInfos = jarListInfo.getFiles();
            long  jarCount = jarFileInfos.stream().filter(jarFileInfo -> {
                if(jarFileInfo.getId().equals(tRes.getResUname())){
                    return true;
                }
                return false;
            }).count();

            if(jarCount > 0){
                flinkApiService.deleteJar(FlinkUtils.getIPAndPort(tCluster.getAddress()),tRes.getResUname());
            }

            resMapper.deleteById(jarid);

        } catch (ApiException e) {
            e.printStackTrace();
            return Response.error(CodeEnum.DELETE_JARS_ERROR);
        }
        return Response.success();
    }
}
