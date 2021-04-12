package com.haibao.admin.web.service.impl;

import com.haibao.admin.utils.TDFileUtils;
import com.haibao.admin.web.common.enums.CodeEnum;
import com.haibao.admin.web.common.enums.Contants;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TUdf;
import com.haibao.admin.web.mapper.UdfMapper;
import com.haibao.admin.web.service.UdfService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * 自定义函数信息定义表 服务实现类
 * </p>
 *
 * @author zc
 * @since 2020-02-25
 */
@Service
public class UdfServiceImpl extends ServiceImpl<UdfMapper, TUdf> implements UdfService {

    @Value("${flink.udf.location}")
    private String udfRootPath;

    @Autowired
    private UdfMapper udfMapper;


    @Override
    public Response addUdf(MultipartFile multfile, TUdf tUdf) {
        try {
//            QueryWrapper<TUdf> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("udf_name", tUdf.getUdfName());
//            queryWrapper.ne("delete_flag",1);//已删除的不做匹配
//            Integer count = udfMapper.selectCount(queryWrapper);
//            if (count > 0) {
//                return Response.error(CodeEnum.UDF_NAME_REPEAT);
//            }
            String fileName = multfile.getOriginalFilename();
            if (!StringUtils.equalsIgnoreCase("jar", TDFileUtils.getFileType(fileName))) {
                return Response.error(-11,"请上传jar类型的文件！");
            }
            File udfRootDir = new File(udfRootPath);
            if (!udfRootDir.exists()) {
                return Response.error(-11,"UDF上传目录配置不正确，请检查！");
            }
//            //新建文件夹来解决包名冲突
//            String udfSavePath = udfRootPath + File.separator + UUID.randomUUID().toString();
//
//            File udfSaveDir = new File(udfSavePath);
//            if (!udfSaveDir.exists()) {
//                udfSaveDir.mkdir();
//            }
            File udfFile = new File(udfRootDir, UUID.randomUUID().toString() + fileName);

            multfile.transferTo(udfFile);
            tUdf.setJarName(fileName);
            tUdf.setUdfPath(udfFile.getPath());
            Date date = new Date();
            tUdf.setCreateTime(date);
            tUdf.setModifyTime(date);
            tUdf.setDeleteFlag(Contants.NORMAL);
            udfMapper.insert(tUdf);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.error(CodeEnum.UPLOAD_UDF_LOCAL_ERROR);
        }
        return Response.success();

    }

    @Override
    public Response deleteUdf(Long id) {
        TUdf tUdf = udfMapper.selectById(id);
        if (tUdf == null) {
            return Response.error(CodeEnum.DELETE_UDF_LOCAL_ERROR);
        }
        //todo 校验正在运行的任务中是否使用
        File udfFile = new File(tUdf.getUdfPath());
        if (udfFile.exists()) {
            udfFile.delete();
        }
        tUdf.setDeleteFlag(Contants.DELETE);
        tUdf.setModifyTime(new Date());
        udfMapper.updateById(tUdf);
        return Response.success();
    }

    @Override
    public Response updateUdf(MultipartFile multfile, TUdf tUdf) {
        try {

            TUdf oldUUdf = udfMapper.selectById(tUdf.getId());
            oldUUdf.setUdfDesc(tUdf.getUdfDesc());
            oldUUdf.setModifyBy(tUdf.getModifyBy());
            oldUUdf.setModifyTime(new Date());
            udfMapper.updateById(oldUUdf);
            return Response.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(CodeEnum.UPDATE_UDF_LOCAL_ERROR);
        }
    }
}
