package com.haibao.admin.web.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.haibao.admin.web.vo.UdfVO;
import com.nextbreakpoint.flinkclient.model.JarUploadResponseBody;
import com.haibao.admin.web.common.enums.Contants;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TUdf;
import com.haibao.admin.web.service.UdfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 自定义函数信息定义表 前端控制器
 * </p>
 *
 * @author ml.c
 * @since 2020-02-25
 */
@Api(tags = "自定义函数",position=4)
@Validated
@RestController
@RequestMapping("/ds/udf")
public class UdfController extends BaseController{

    @Autowired
    private UdfService udfService;

    /**
     * UDF 列表
     * @param udsVO
     * @return
     */
    @ApiOperation(value = "自定义函数分页列表")
    @ApiOperationSupport(author = "c.zh",
            ignoreParameters = {"id","udfPath","parms","serialVersionUID"})
    @GetMapping("/list")
    Response<IPage<TUdf>> dsList(UdfVO udsVO){

        QueryWrapper<TUdf> queryWrapper = new QueryWrapper<>();

        if(StrUtil.isNotEmpty(udsVO.getUdfName())){
            queryWrapper.like("udf_name",udsVO.getUdfName());
        }
        queryWrapper.eq("delete_flag", Contants.NORMAL);

        Page<TUdf> page = new Page<>(udsVO.getCurPage(), udsVO.getPageSize());

        IPage<TUdf> list =  udfService.page(page,queryWrapper);

        return Response.success(list);
    }


    @PostMapping("/upload")
    @ApiOperationSupport(author = "c.zh",
            ignoreParameters = {"id", "createBy", "modifyBy", "createTime", "modifyTime","udfPath","jarName","deleteFlag","serialVersionUID"})
    @ApiOperation(value = "自定义函数jar包上传")
    public Response<JarUploadResponseBody> jarsUpload(@NotNull @RequestParam("jarfile") MultipartFile file, @Valid TUdf tUdf){
//        if(bindingResult.hasErrors()){
//            return Response.error(-11,bindingResult.getAllErrors().get(0).getDefaultMessage());
//        }
        QueryWrapper<TUdf> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotEmpty(tUdf.getUdfName())){
            queryWrapper.eq("udf_name",tUdf.getUdfName());
            queryWrapper.ne("delete_flag",1);//已删除的不做匹配
        }
        int count=udfService.count(queryWrapper);
        if(count>0){
            return Response.error(-11,"函数名不可重复，请检查！");
        }
        QueryWrapper<TUdf> queryWrapper1 = new QueryWrapper<>();
        if(StrUtil.isNotEmpty(tUdf.getUdfClass())){
            queryWrapper1.eq("udf_class",tUdf.getUdfClass());
            queryWrapper1.ne("delete_flag",1);//已删除的不做匹配
        }
        if(count>0){
            return Response.error(-11,"类名不可重复，请检查！");
        }

        return udfService.addUdf(file,tUdf);
    }

    @DeleteMapping("/delete")
    @ApiOperationSupport(author = "c.zh")
    @ApiOperation(value = "自定义函数删除")
    @ApiImplicitParam(name = "id",value = "主键id",paramType = "query",dataType = "Long",required = true)
    public Response delteJar(@RequestParam("id")Long id){
        return udfService.deleteUdf(id);
    }

    @PutMapping("/update")
    @ApiOperationSupport(author = "c.zh",
            ignoreParameters = {"createBy", "modifyBy", "createTime", "modifyTime","udfPath","jarName","deleteFlag","udfName","udfClass","udfType","serialVersionUID"})
    @ApiOperation(value = "自定义函数更新")
    public Response updateJar(TUdf tUdf){

        tUdf.setModifyBy(getAccount());
        return udfService.updateUdf(null,tUdf);
    }

}
