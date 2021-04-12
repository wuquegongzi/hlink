package com.haibao.admin.web.controller;

import com.haibao.admin.utils.TDFileUtils;
import com.haibao.admin.web.common.result.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

/*
 * @Author ml.c
 * @Description 辅助文档接口
 * @Date 16:28 2020-04-17
 **/
@Api(tags = "辅助文档接口",position = 18)
@RestController
@RequestMapping("/doc")
public class DocController extends BaseController{

    @GetMapping(value = "get/{key}")
    @ApiOperation(value = "根据字典类型获取字典集合 接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "文档key", dataType = "String", paramType = "path", required = true)})
    Response<String> get (@PathVariable(value = "key") String key) {

        String templetePath = "templates/doc/" +key + "_doc.txt";
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(templetePath);

        String html = "";
        try {
            html = TDFileUtils.readFile(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.success(html);
    }
}
