package com.haibao.admin.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.haibao.admin.utils.TDFileUtils;
import com.haibao.admin.web.vo.ResV0;
import com.nextbreakpoint.flinkclient.model.*;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TRes;
import com.haibao.admin.web.service.JarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  @author: ml.c
 *  @Date: 2020/1/6 4:41 下午
 *  @Description: flink jar操作
 */
@Api(tags = "文件管理",position=6)
@RestController
@RequestMapping("/jars")
public class JarController extends BaseController{

    @Autowired
    private JarService jarService;

    @ApiOperationSupport(author = "c.zh",
            ignoreParameters = {"id", "createBy", "modifyBy", "createTime", "modifyTime", "resUname","resName","resUname","resSize","serialVersionUID"})
    @ApiOperation(value = "jar包上传")
    @PostMapping("/upload")
    public Response<JarUploadResponseBody> jarsUpload(@RequestParam("jarfile") MultipartFile file,TRes res){

        res.setCreateBy(getAccount());
        res.setCreateTime(new Date());
        return jarService.jarsUpload(file,res);
    }

    @ApiOperationSupport(author = "c.zh",
            ignoreParameters = {"id","resSize","parms","serialVersionUID"})
    @ApiOperation(value = "jar包列表分页查询")
    @GetMapping("/listPage")
    public Response<IPage<TRes>> jarsList(ResV0 resV0){
        QueryWrapper<TRes> queryWrapper = new QueryWrapper<>();
        if(null != resV0.getClusterId() && resV0.getClusterId() > 0){
            queryWrapper.eq("cluster_id",resV0.getClusterId());
        }

//        if(!isAdmin()){
//            queryWrapper.eq("create_by",getAccount());
//        }

        if(StringUtils.isNotEmpty(resV0.getResName())){
            queryWrapper.like("res_name",resV0.getResName());
        }
        Page<TRes> page = new Page<>(resV0.getCurPage(), resV0.getPageSize());
        IPage<TRes> tResList=jarService.page(page,queryWrapper);

        if(null != tResList){
            List<TRes> list = tResList.getRecords().stream().map(res ->{
                return res.setResSizeStr(TDFileUtils.readableFileSize(res.getResSize()));
            }).collect(Collectors.toList());

            tResList.setRecords(list);
        }

        return Response.success(tResList);
    }

    @ApiOperationSupport(author = "c.zh")
    @ApiOperation(value = "jar包列表获取(无分页)")
    @GetMapping("list")
    public Response<List<TRes>> jars(ResV0 resV0){
        QueryWrapper<TRes> queryWrapper = new QueryWrapper<>();
        if(resV0.getClusterId()!=null){
            queryWrapper.eq("cluster_id",resV0.getClusterId());

        }
//        if(!isAdmin()){
//            queryWrapper.eq("create_by",getAccount());
//        }

        List<TRes>  tResList=jarService.list(queryWrapper);
        return  Response.success(tResList);
    }

    @ApiOperationSupport(author = "c.zh")
    @ApiOperation(value = "jar包删除")
    @ApiImplicitParam(name = "jarid",value = "jar包Id",paramType = "query",dataType = "Long",required = true)
    @DeleteMapping("/removeDO")
    public Response delteJar(@RequestParam("jarid")Long jarid){
        return jarService.deleteJar(jarid);
    }
}
