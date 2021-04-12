package com.haibao.admin.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TDict;
import com.haibao.admin.web.service.DictService;
import com.haibao.flink.utils.GsonUtils;
import com.haibao.flink.utils.IdUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: c.zh
 * @description: 字典表管理层
 * @date: 2020/2/27
 **/
@Api(tags = "字典",position = 9)
@RestController
@RequestMapping("/dict")
public class DictController extends BaseController{

    @Autowired
    private DictService dictService;

    @Autowired
    Cache<String, Object> dictCache;

    @GetMapping(value = "list")
    @ApiOperationSupport(author = "c.zh")
    @ApiOperation(value = "字典列表 接口")
    Response<Map<String, List<TDict>>> dictList (){

        Map<String, List<TDict>> collect = (Map<String, List<TDict>>) dictCache.getIfPresent("dictList");

        if(null == collect){
            //所有字典数据
            QueryWrapper<TDict> queryWrapper = new QueryWrapper();
            //0-启用 1-停用
            queryWrapper.eq("status","0");
            queryWrapper.orderByAsc("dict_type","dict_sort");

            List<TDict> tDicts=  dictService.list(queryWrapper);
            collect= tDicts.stream().collect(Collectors.groupingBy(TDict::getDictType));

            dictCache.put("dictList",collect);
        }else {
            System.out.println("字典缓存命中..."+ GsonUtils.gsonString(collect));
        }

        return Response.success(collect);
    }

    @GetMapping(value = "list/{dictType}")
    @ApiOperation(value = "根据字典类型获取字典集合 接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", dataType = "String", paramType = "path", required = true)})
    Response<List<TDict>> list (@PathVariable(value = "dictType") String dictType){

        // 先从缓存读取
//        dictCache.getIfPresent(dictType);
        List<TDict> tDicts = (List<TDict>) dictCache.asMap().get(String.valueOf(dictType));

        if (tDicts == null){
            QueryWrapper<TDict> queryWrapper = new QueryWrapper();
            queryWrapper.eq("dict_type",dictType);
            //0-启用 1-停用
            queryWrapper.eq("status","0");
            queryWrapper.orderByAsc("dict_sort");
            tDicts=  dictService.list(queryWrapper);

            dictCache.put(String.valueOf(dictType),tDicts);
        }

        return Response.success(tDicts);
    }

    @GetMapping(value = "getSnowflakeId")
    @ApiOperation(value = "获取虚拟ID")
    String getSnowflakeId(){
        return String.valueOf(IdUtils.getId());
    }

}
