package com.haibao.admin.web.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.haibao.admin.web.vo.DsVO;
import com.haibao.admin.web.common.enums.CodeEnum;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TDs;
import com.haibao.admin.web.service.DictService;
import com.haibao.admin.web.service.DsService;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @author: ml.c
 * @Date: 2020/1/6 4:41 下午
 * @Description: Flink数据源操作
 */
@Api(tags = "数据源管理", description = "数据源管理接口", position = 3)
@Validated
@RestController
@RequestMapping("/ds")
public class DsController extends BaseController {

    @Autowired
    private DsService dsService;

    @Autowired
    private DictService dictService;

    @Autowired
    Cache<String, Object> dsCache;


    /**
     * ds列表
     *
     * @return
     */
    @ApiOperationSupport(author = "ml.c",
            ignoreParameters = {"id", "ddlEnable", "dsDdl", "schemaFile", "createBy", "createTime", "modifyTime", "modifyBy",
                    "dsSchemaColumnVOS[0]", "jsonFieldVO", "jsonFieldVO.id", "jsonFieldVO.dsId"})
    @ApiOperation(value = "列表查询", position = 1, notes = "数据源列表", produces = "application/json")
    @ApiImplicitParam(name = "dsType", value = "数据源类型,见字典ds_type", paramType = "query", required = true, dataType = "string")
    @GetMapping("/list")
    Response<IPage<TDs>> list(DsVO dsVO) {

        QueryWrapper<TDs> queryWrapper = new QueryWrapper<>();
        if (null != dsVO.getDsType()) {
            queryWrapper.eq("ds_type", dsVO.getDsType());
        }
        if (StrUtil.isNotEmpty(dsVO.getDsName())) {
            queryWrapper.like("ds_name", dsVO.getDsName());
        }
        if (StrUtil.isNotEmpty(dsVO.getTableName())) {
            queryWrapper.like("table_name", dsVO.getTableName());
        }

        //默认列表查询 排除
        queryWrapper.select(TDs.class, ds -> !"ds_ddl".equals(ds.getColumn())
                && !"schema_file".equals(ds.getColumn()));

        Page<TDs> page = new Page<>(dsVO.getCurPage(), dsVO.getPageSize());

        IPage<TDs> dsList = dsService.page(page, queryWrapper);

        return Response.success(dsList);
    }

    /**
     * 数据源明细
     *
     * @param dsVO
     * @return
     */
    @ApiOperationSupport(author = "ml.c",
            ignoreParameters = {"ddlEnable", "dsDdl", "createBy", "createTime", "modifyTime", "modifyBy",
                    "dsSchemaColumnVOS[0]", "jsonFieldVO", "jsonFieldVO.id", "jsonFieldVO.dsId"})
    @ApiOperation(value = "数据源明细", position = 2, notes = "数据源明细", produces = "application/json")
    @ApiImplicitParam(name = "id", value = "主键", paramType = "query", required = true, dataType = "long")
    @GetMapping("/get")
    Response<DsVO> get(DsVO dsVO) {

        return dsService.get(dsVO);
    }

    /**
     * 根据功能类型和表类型 获取不同的属性字段,页面调用补充界面属性
     *
     * @return
     */
    @ApiOperationSupport(author = "ml.c",
            ignoreParameters = {"ddlEnable", "dsDdl", "schemaFile", "createBy", "createTime", "modifyTime", "modifyBy",
                    "dsSchemaColumnVOS[0]", "jsonFieldVO", "jsonFieldVO.id", "jsonFieldVO.dsId"})
    @ApiOperation(value = "获取自定义字段模版", position = 3, notes = "根据功能类型和表类型 获取不同的属性字段,页面调用补充界面属性", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dsType", value = "功能类型", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "tableType", value = "表类型", paramType = "query", required = true, dataType = "string")
    })
    @GetMapping("/getFieldTemplete")
    Response getFieldTemplete(DsVO dsVO) {
        return dsService.getFieldTemplete(dsVO);
    }

    /**
     * 上传schema文件
     *
     * @param file
     * @param req
     * @return
     */
    @ApiOperationSupport(author = "ml.c")
    @ApiOperation(value = "上传schema文件", position = 4, notes = "提交并解析schema")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "file", value = "文件流对象,接收数组格式", required = true, dataType = "__File"),
                    @ApiImplicitParam(name = "schemaType", value = "schema类型 0-json 1-arvo,详情见字典schema_type", required = true, defaultValue = "0", dataType = "String"),
                    @ApiImplicitParam(name = "structureType", value = "结构类型，单一结构- 0 或者 多层嵌套结构- 1", required = true, defaultValue = "0", dataType = "String")}
    )
    @PostMapping("/schemaAnalysisDO")
    Response<Map> schemaAnalysisDO(@NotNull @RequestParam("file") MultipartFile file,
                                   @NotNull @RequestParam("schemaType") String schemaType,
                                   @RequestParam("structureType") String structureType,
                                   HttpServletRequest req) {

        if ("0".equals(schemaType)) {
            return dsService.jsonSchemaSubmitAndAnalysis(file, req);
        } else {
            //arvo todo
            return null;
        }
    }

    /**
     * @return
     */
    @ApiOperationSupport(author = "ml.c")
    @ApiOperation(value = "获取支持的数据类型", position = 5,
            notes = "列投影，获取支持的数据类型,flinkSQLTypes：Flink SQL支持的数据类型，basicTypes：基础数据类型")
    @GetMapping("/getTypeList")
    Response<Map> getTypeList() {

        return dictService.getTypeList();
    }

    /**
     * 数据源添加表的时候，需要校验表的别名是否已经在集群内存在
     *
     * @return
     */
    @ApiOperationSupport(author = "ml.c")
    @ApiOperation(value = "校验表的别名", position = 6, notes = "校验表的别名是否已经存在")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "tableName", value = "别名", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "dsId", value = "如果是修改，需要传入本身ID，用于排除自身", required = false, defaultValue = "0", dataType = "Long")}
    )
    @GetMapping("/checkTableName")
    Response checkTableName(@NotEmpty(message = "别名不可为空") String tableName, Long dsId) {
        return dsService.checkTableName(tableName, dsId);
    }

    /**
     * 添加数据源
     *
     * @return
     */
    @ApiOperationSupport(author = "ml.c",
            ignoreParameters = {"ddlEnable", "dsDdl", "createBy", "createTime", "modifyTime", "modifyBy",
                    "dsSchemaColumnVOS[0]", "jsonFieldVO", "jsonFieldVO.id", "jsonFieldVO.dsId"})
    @ApiOperation(value = "新增动作", position = 7, notes = "添加数据源")
    @PostMapping("/addDO")
    Response addDO(@RequestBody @Valid DsVO dsVO) {

        Response response = dsService.check(dsVO);

        if(!response.isSuccess()){
            return response;
        }

        dsVO.setCreateBy(getAccount());
        dsVO.setCreateTime(new Date());

        //新增重写，需要一个事务
        long id = 0;
        try {
            id = dsService.saveDs(dsVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(CodeEnum.DATABASE_ERROR, "保存失败！");
        }

        return Response.success(id);
    }

    /**
     * 修改数据源
     *
     * @param dsVO
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    @ApiOperationSupport(author = "ml.c", order = 8)
    @ApiOperation(value = "修改动作", notes = "修改数据源")
    @PutMapping("/modifyDO")
    Response updateDO(@RequestBody @Valid DsVO dsVO) {

        Response response = dsService.check(dsVO);
        if(!response.isSuccess()){
            return response;
        }

        dsVO.setModifyBy(getAccount());
        dsVO.setModifyTime(new Date());

        return dsService.modify(dsVO);
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperationSupport(author = "ml.c", order = 9)
    @ApiOperation(value = "删除动作", notes = "删除数据源")
    @DeleteMapping("removeDO")
    Response delete(Long id) {

        return dsService.delete(id);
    }

}
