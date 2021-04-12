package com.haibao.admin.web.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.google.common.collect.Maps;
import com.haibao.admin.utils.FlinkUtils;
import com.haibao.admin.utils.TDFileUtils;
import com.haibao.admin.utils.TemplateUtils;
import com.haibao.admin.web.entity.TDs;
import com.haibao.admin.web.entity.TDsJsonField;
import com.haibao.admin.web.entity.TDsSchemaColumn;
import com.haibao.admin.web.entity.TJobDs;
import com.haibao.admin.web.entity.TJobDsSink;
import com.haibao.admin.web.vo.DsJsonFieldVO;
import com.haibao.admin.web.vo.DsSchemaColumnVO;
import com.haibao.admin.web.vo.DsVO;
import com.haibao.admin.web.common.enums.CodeEnum;
import com.haibao.admin.web.service.JobDsService;
import com.haibao.admin.web.service.JobDsSinkService;
import com.haibao.admin.web.vo.templete.Option;
import com.haibao.admin.web.vo.templete.UnionField;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.mapper.DsJsonFieldMapper;
import com.haibao.admin.web.mapper.DsMapper;
import com.haibao.admin.web.mapper.DsSchemaColumnMapper;
import com.haibao.admin.web.service.DsService;
import com.haibao.admin.web.vo.templete.JsonField;
import com.haibao.flink.enums.DsTypeEnum;
import com.haibao.flink.utils.GsonUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName DsServiceImpl
 * @Description 数据源 实现
 * @Author ml.c
 * @Date 2020/2/13 9:46 下午
 * @Version 1.0
 */
@Service
public class DsServiceImpl extends ServiceImpl<DsMapper, TDs> implements DsService {

    @Autowired
    DsJsonFieldMapper dsJsonFieldMapper;
    @Autowired
    DsSchemaColumnMapper dsSchemaColumnMapper;

    @Autowired
    JobDsService jobDsService;
    @Autowired
    JobDsSinkService jobDsSinkService;

    @Autowired
    FreeMarkerConfigurer configurer;

    @Autowired
    @Qualifier("flinkKeywordsCache")
    Cache<String, Integer> flinkKeywordsCache;

    /**
     * jsonschema 校验解析
     *
     * @param file
     * @param req
     * @return
     */
    @Override
    public Response<Map> jsonSchemaSubmitAndAnalysis(MultipartFile file, HttpServletRequest req) {

        //校验格式
        boolean isJson = TDFileUtils.checkFileType(file.getOriginalFilename(), ".json");
        if (!isJson) {
            return Response.error(500, "文件类型只能是.json");
        }

        //上传
        Response r = TDFileUtils.uplaodFileToTemp(file, req);
        if (!r.isSuccess()) {
            return r;
        }

        String tempPath = r.getData().toString();

        //读取
        FileReader fileReader = new FileReader(tempPath);
        String jsonSchema = fileReader.readString();

        //校验
        try {
            TDFileUtils.jsonSchemaValidate(jsonSchema);
        } catch (JSONException e) {
            e.printStackTrace();
            return Response.error(500, "json格式有误，请检查！错误信息如下：\n" + e.getMessage());
        } catch (ValidationException e) {
            e.printStackTrace();
            return Response.error(500, "校验失败！错误信息如下：\n" + e.getAllMessages());
        }

        List<DsSchemaColumnVO> dsSchemaColumnVOS = null;
        try {
            dsSchemaColumnVOS = FlinkUtils.jsonSchemaTypesConvert("root",jsonSchema,0,"0",null);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(500, "解析失败！错误信息如下：\n" + e.getMessage());
        }

        //关键字验证 eg:user,如果要使用关键字作为字段名称，确保将它们用反引号（例如`value`，`count`）引起来
        for (DsSchemaColumnVO dsc: dsSchemaColumnVOS) {
            if(null != flinkKeywordsCache.getIfPresent(dsc.getName().toUpperCase())){
                dsc.setRes1(dsc.getName().toUpperCase());  //匹配关键字
            }
        }

        //删除临时文件
        FileUtil.del(tempPath);

        Map map = new HashMap(2);
        map.put("schemaFile",jsonSchema);
        map.put("dsSchemaColumnVOS",dsSchemaColumnVOS);

        return Response.success(map);
    }

    /**
     * 保存
     *
     * @param dsVO
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = {Exception.class, IOException.class, TemplateException.class})
    @Override
    public Long saveDs(DsVO dsVO) throws IOException, TemplateException {

        TDs tDs = new TDs();
        BeanUtils.copyProperties(dsVO, tDs);

        //默认开启DDL,源表和目标表生成DDL语句的时候，开启.
        //暂时第一版本 如果是维表，不开启ddl
        if (dsVO.getDsType().toLowerCase().equals(DsTypeEnum.SIDE.getType())) {
            tDs.setDdlEnable(0);
        } else {
            tDs.setDdlEnable(1);
        }

        baseMapper.insert(tDs);

        TDsJsonField dsJsonField = new TDsJsonField();
        DsJsonFieldVO dsJsonFieldVO = dsVO.getJsonFieldVO();
        if (null != dsJsonFieldVO) {
            //BeanUtils.copyProperties(dsJsonFieldVO,dsJsonField);
            //页面传回的是集合，需要转换成字符串
            dsJsonField.setJsonValue(GsonUtils.gsonString(dsJsonFieldVO.getJsonValue()));
            dsJsonField.setDsId(tDs.getId());

            dsJsonFieldMapper.insert(dsJsonField);
        }

        List<TDsSchemaColumn> tDsSchemaColumns;
        List<DsSchemaColumnVO> dsSchemaColumnVOS = dsVO.getDsSchemaColumnVOS();
        if (null != dsSchemaColumnVOS && dsSchemaColumnVOS.size() > 0) {
            Long finalDsId = tDs.getId();
            tDsSchemaColumns = dsSchemaColumnVOS.stream().map(dsSchemaColumnVO -> {

                TDsSchemaColumn dsSchemaColumn = new TDsSchemaColumn();
                BeanUtils.copyProperties(dsSchemaColumnVO, dsSchemaColumn);

                dsSchemaColumn.setDsId(finalDsId);

                //过滤关键字
                if(null != flinkKeywordsCache.getIfPresent(dsSchemaColumn.getName().toUpperCase())){
                    dsSchemaColumn.setRes1(dsSchemaColumn.getName().toUpperCase());  //匹配关键字
                }

                return dsSchemaColumn;
            }).collect(Collectors.toList());

            //自定义mapper层批量插入
            dsSchemaColumnMapper.insertBatch(tDsSchemaColumns);
        }

        //根据数据源保存结果，拼装DDL语句
        if (1 == tDs.getDdlEnable()) {

            Map map = Maps.newHashMap();
            map.put("tableName", tDs.getTableName());

            //获取列属性
            List<DsSchemaColumnVO> fields = dsVO.getDsSchemaColumnVOS();

            //需要二次剔除非顶级属性 并且 重新拼装顶级字段属性
            fields = FlinkUtils.dsSchemaColumnsConvertDDL(fields,"0");

            map.put("fields", fields);
            //获取个性化连接属性,json模版的定义fieldName 和 ftl文件的替换属性需要一一对应
            List<JsonField> jsonFields = dsVO.getJsonFieldVO().getJsonValue();
            map = TemplateUtils.jsonTemplateExtract(jsonFields,map);

            //根据功能类型和表类型，获取相对应的DDL模版
            String ddlPath = "ddl/" + dsVO.getDsType() + "_" + dsVO.getTableType() + ".ftl";
            ClassPathResource classPathResource = new ClassPathResource("/templates/"+ddlPath);
            if(!classPathResource.exists()){
                throw new RuntimeException("模版文件不存在"+ddlPath);
            };
            //替换模版
            Template template = configurer.getConfiguration().getTemplate(ddlPath);
            String resp = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);

            tDs.setDsDdl(resp);
            //更新DDL
            baseMapper.updateById(tDs);
        }

        return tDs.getId();
    }

    /**
     * 解析并校验非公共化字段 json 参数是否ok
     *
     * @param jsonFields
     * @return
     */
    @Override
    public boolean checkJsonFields(List<JsonField> jsonFields) {

//        List<JsonField> jsonFields= JSON.parseArray(jsonFieldStr,JsonField.class);
        long vcount = jsonFields.stream().filter(jsonField -> {
            //如果是必填，结果不可为空
            if (jsonField.isRequired() && StrUtil.isEmpty(jsonField.getFieldValue())) {
                return true;
            }
            //判断及联项
            List<Option> options = jsonField.getOption();
            if(null == options || options.size()< 1){
                return false;
            }
            for (Option option: options) {
                if(jsonField.getFieldValue().equals(option.getName()) && option.isHasUnion()){
                    List<UnionField> unionFields = option.getUnionFields();
                    for (UnionField unionField : unionFields) {
                        if(unionField.isRequired() && StrUtil.isEmpty(unionField.getFieldValue())){
                            return true;
                        }
                    }
                }
            }

            return false;

        }).count();

        if (vcount > 0) {
            return false;
        }

        return true;
    }

    @Override
    public Response check(DsVO dsVO) {

        Response r = checkTableName(dsVO.getTableName(), dsVO.getId());
        if (!r.isSuccess()) {
            return r;
        }
        //校验存储数据源明细
        if (null == dsVO.getDsSchemaColumnVOS() || dsVO.getDsSchemaColumnVOS().size() < 1) {
            return Response.error(CodeEnum.PARAM_ERROR, "Schema 列投影不可为空！");
        }
        if (null == dsVO.getJsonFieldVO()
                || null == dsVO.getJsonFieldVO().getJsonValue()
                || dsVO.getJsonFieldVO().getJsonValue().size() < 0) {
            return Response.error(CodeEnum.PARAM_ERROR, "外部连接自定义属性不可为空！");
        }

        //解析JsonField数据，并校验
        boolean checkJsonFields = this.checkJsonFields(dsVO.getJsonFieldVO().getJsonValue());
        if (!checkJsonFields) {
            return Response.error(CodeEnum.PARAM_ERROR, "必填字段不可为空！");
        }

        //如果是维表，必须存在连接属性，且level为1，在root之下
        if (dsVO.getDsType().toLowerCase().equals(DsTypeEnum.SIDE.getType())) {
            List<DsSchemaColumnVO> dsSchemaColumnVOS = dsVO.getDsSchemaColumnVOS();
            long joinCount = dsSchemaColumnVOS.stream().filter(dsSchemaColumnVO -> {
                if(null != dsSchemaColumnVO.getLevel() && 1 == dsSchemaColumnVO.getLevel()
                   && null != dsSchemaColumnVO.getJoinKey()  && 1 == dsSchemaColumnVO.getJoinKey()){
                  return true;
                }
                return false;
            }).count();

            if(joinCount < 1){
                return Response.error(CodeEnum.PARAM_ERROR, "维表-列投影root之下级别为1的层属性，必须存在一个连接属性，请选择！");
            }else if(joinCount > 1){
                return Response.error(CodeEnum.PARAM_ERROR, "维表-列投影root之下级别为1的层属性，目前仅支持一个连接属性，请检查！");
            }
        }

        return Response.success();
    }

    /**
     * 获取明细
     *
     * @param dsVO
     * @return
     */
    @Override
    public Response<DsVO> get(DsVO dsVO) {

        if (null == dsVO.getId()) {
            return Response.error(CodeEnum.PARAM_ERROR);
        }

        //获取数据源主体
        TDs ds = this.getById(dsVO.getId());

        if (null == ds) {
            return Response.error(404, "信息不存在");
        }
        //获取数据源的schema列投影
        QueryWrapper<TDsSchemaColumn> queryWrapper = new QueryWrapper();
        queryWrapper.eq("ds_id", dsVO.getId());
        List<TDsSchemaColumn> dsSchemaColumns = dsSchemaColumnMapper.selectList(queryWrapper);

        //获取数据源个性化字段属性值 json
        QueryWrapper<TDsJsonField> queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("ds_id", dsVO.getId());
        TDsJsonField dsJsonField = dsJsonFieldMapper.selectOne(queryWrapper2);

        //组合
        BeanUtils.copyProperties(ds, dsVO);

        if (null != dsSchemaColumns && dsSchemaColumns.size() > 0) {
            List<DsSchemaColumnVO> dsSchemaColumnVOS =
                    dsSchemaColumns.stream().map(
                            s -> {
                                DsSchemaColumnVO dsSchemaColumnVO = new DsSchemaColumnVO();
                                BeanUtils.copyProperties(s, dsSchemaColumnVO);

                                return dsSchemaColumnVO;
                            }

                    ).collect(Collectors.toList());

            dsVO.setDsSchemaColumnVOS(dsSchemaColumnVOS);
        }

        if (null != dsJsonField) {
            DsJsonFieldVO dsJsonFieldVO = new DsJsonFieldVO();

            //数据存储的是json字符串，需要转换为集合
//            BeanUtils.copyProperties(dsJsonField,dsJsonFieldVO);
            List<JsonField> jsonValue = GsonUtils.jsonToList(dsJsonField.getJsonValue(), JsonField.class);
            dsJsonFieldVO.setId(dsJsonField.getId());
            dsJsonFieldVO.setDsId(dsJsonField.getDsId());
            dsJsonFieldVO.setJsonValue(jsonValue);

            dsVO.setJsonFieldVO(dsJsonFieldVO);
        }

        return Response.success(dsVO);
    }

    /**
     * 获取json个性化字段模版
     *
     * @param dsVO
     * @return
     */
    @Override
    public Response getFieldTemplete(DsVO dsVO) {

        if (StrUtil.isEmpty(dsVO.getDsType()) || StrUtil.isEmpty(dsVO.getTableType())) {
            return Response.error(CodeEnum.PARAM_ERROR);
        }

        String templetePath = "templates/json_field/" + dsVO.getDsType().toLowerCase() + "_" + dsVO.getTableType().toLowerCase() + ".json";
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(templetePath);

        String configContent = "";
        try {
            configContent = TDFileUtils.readFile(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return StrUtil.isNotEmpty(configContent) ? Response.success(GsonUtils.gsonToListMaps(configContent)) : Response.error(500, "获取模版失败！");

    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Response delete(Long id) {
        //校验，有没有任务在使用
        QueryWrapper<TJobDs> queryWrapper = new QueryWrapper<TJobDs>();
        queryWrapper.eq("ds_id",id);

        QueryWrapper<TJobDsSink> queryWrapper2 = new QueryWrapper<TJobDsSink>();
        queryWrapper2.eq("ds_id",id);

        if(jobDsService.count(queryWrapper) >0 || jobDsSinkService.count(queryWrapper2) >0){
            return Response.error(423, "当前数据源有作业使用中，不可删除，请确认！");
        }

        boolean result = this.removeById(id);

        return result ? Response.success() : Response.error(500, "删除失败！");
    }

    /**
     * 修改
     *
     * @param dsVO
     * @return
     */
    @Override
    public Response modify(DsVO dsVO) {
        Response r = checkTableName(dsVO.getTableName(), dsVO.getId());
        if (!r.isSuccess()) {
            return r;
        }

        if (dsVO.getId() < 1) {
            return Response.error(CodeEnum.PARAM_ERROR);
        }

        //校验存储数据源明细
        if (null == dsVO.getDsSchemaColumnVOS() || dsVO.getDsSchemaColumnVOS().size() < 1) {
            return Response.error(CodeEnum.PARAM_ERROR, "Schema 列投影不可为空！");
        }
        if (null == dsVO.getJsonFieldVO()
                || null == dsVO.getJsonFieldVO().getJsonValue()
                || dsVO.getJsonFieldVO().getJsonValue().size() < 0) {
            return Response.error(CodeEnum.PARAM_ERROR, "外部连接属性不可为空！");
        }
        //解析JsonField数据，并校验
        boolean checkJsonFields = this.checkJsonFields(dsVO.getJsonFieldVO().getJsonValue());
        if (!checkJsonFields) {
            return Response.error(CodeEnum.PARAM_ERROR, "必填字段不可为空！");
        }

        //修改
        boolean isDO = false;
        try {
            isDO = updateDs(dsVO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDO ? Response.success() : Response.error(CodeEnum.DATABASE_ERROR, "修改失败");
    }

    /**
     * 校验表名是否已经存在
     *
     * @param tbname
     * @return
     */
    @Override
    public Response checkTableName(String tbname, Long dsId) {

        QueryWrapper<TDs> queryWrapper = new QueryWrapper();
        queryWrapper.eq("table_name", tbname);
        if (null != dsId && dsId > 0) {
            queryWrapper.notIn("id", dsId);
        }

        TDs ds = baseMapper.selectOne(queryWrapper);
        if (null != ds) {
            return Response.error(1, "该别名已存在！");
        }
        return Response.success();
    }

    @Transactional(rollbackFor = Exception.class)
    boolean updateDs(DsVO dsVO) throws IOException, TemplateException {

        TDs tDs = new TDs();
        BeanUtils.copyProperties(dsVO, tDs);

        //默认开启DDL,源表和目标表生成DDL语句的时候，开启.
        //暂时第一版本 如果是维表，不开启ddl
        if (dsVO.getDsType().toLowerCase().equals(DsTypeEnum.SIDE.getType())) {
            tDs.setDdlEnable(0);
        } else {
            tDs.setDdlEnable(1);
        }

        if(StrUtil.isEmpty(tDs.getSchemaFile())){
            tDs.setSchemaFile(null);
        }

        baseMapper.updateById(tDs);

        TDsJsonField dsJsonField = new TDsJsonField();
        DsJsonFieldVO dsJsonFieldVO = dsVO.getJsonFieldVO();
        if (null != dsJsonFieldVO) {
            //页面传回的是集合，需要转换成字符串
            dsJsonField.setJsonValue(GsonUtils.gsonString(dsJsonFieldVO.getJsonValue()));

            UpdateWrapper<TDsJsonField> wrapper = new UpdateWrapper<TDsJsonField>();
            wrapper.eq("ds_id", tDs.getId());

            dsJsonFieldMapper.update(dsJsonField, wrapper);
        }

        List<TDsSchemaColumn> tDsSchemaColumns;
        List<DsSchemaColumnVO> dsSchemaColumnVOS = dsVO.getDsSchemaColumnVOS();
        if (null != dsSchemaColumnVOS && dsSchemaColumnVOS.size() > 0) {
            Long finalDsId = tDs.getId();
            tDsSchemaColumns = dsSchemaColumnVOS.stream().map(dsSchemaColumnVO -> {

                TDsSchemaColumn dsSchemaColumn = new TDsSchemaColumn();
                BeanUtils.copyProperties(dsSchemaColumnVO, dsSchemaColumn);

                dsSchemaColumn.setDsId(finalDsId);

                //过滤关键字
                if(null != flinkKeywordsCache.getIfPresent(dsSchemaColumn.getName().toUpperCase())){
                    dsSchemaColumn.setRes1(dsSchemaColumn.getName().toUpperCase());  //匹配关键字
                }

                return dsSchemaColumn;
            }).collect(Collectors.toList());

            //自定义mapper层批量插入
            QueryWrapper<TDsSchemaColumn> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ds_id", tDs.getId());
            dsSchemaColumnMapper.delete(queryWrapper);
            dsSchemaColumnMapper.insertBatch(tDsSchemaColumns);
        }

        //根据数据源保存结果，拼装DDL语句
        if (1 == tDs.getDdlEnable()) {

            Map map = Maps.newHashMap();
            map.put("tableName", tDs.getTableName());

            //获取列属性
            List<DsSchemaColumnVO> fields = dsVO.getDsSchemaColumnVOS();

            //需要二次剔除非顶级属性 并且 重新拼装顶级字段属性
            fields = FlinkUtils.dsSchemaColumnsConvertDDL(fields,"0");

            map.put("fields", fields);
            //获取个性化连接属性,json模版的定义fieldName 和 ftl文件的替换属性需要一一对应
            List<JsonField> jsonFields = dsVO.getJsonFieldVO().getJsonValue();
            map = TemplateUtils.jsonTemplateExtract(jsonFields,map);

            //根据功能类型和表类型，获取相对应的DDL模版
            String ddlPath = "ddl/" + dsVO.getDsType() + "_" + dsVO.getTableType() + ".ftl";
            ClassPathResource classPathResource = new ClassPathResource("/templates/"+ddlPath);
            if(!classPathResource.exists()){
                throw new RuntimeException("模版文件不存在"+ddlPath);
            };
            //替换模版
            Template template = configurer.getConfiguration().getTemplate(ddlPath);
            String resp = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);

            tDs.setDsDdl(resp);
            //更新DDL
            baseMapper.updateById(tDs);
        }

        return true;

    }
}
