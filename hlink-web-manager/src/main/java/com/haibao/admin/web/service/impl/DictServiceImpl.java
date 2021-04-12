package com.haibao.admin.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TDict;
import com.haibao.admin.web.mapper.DictMapper;
import com.haibao.admin.web.service.DictService;
import com.haibao.flink.enums.FlinkSQLTypesEnum;
import org.apache.flink.api.common.typeinfo.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author zc
 * @since 2020-02-28
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, TDict> implements DictService {

    @Autowired
    Cache<String, Object> dictCache;

    @Override
    public List<TDict> getByDictType(String dictType) {
        QueryWrapper<TDict> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("dict_type",dictType);
        return list(queryWrapper);
    }

    @Override
    public TDict getByDictTypeAndValue(String dictType,String dictValue) {
        QueryWrapper<TDict> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("dict_type",dictType);
        queryWrapper.eq("dict_value",dictValue);
        return getOne(queryWrapper);
    }

    @Override
    public Response<Map> getTypeList() {
        Map map  = (Map) dictCache.asMap().get("ds_typeList");

        if(null != map){
            return Response.success(map);
        }

        map = new HashMap(2);
        List<String> flinkSQLTypes= new ArrayList<String>();
        for(FlinkSQLTypesEnum typeEnum : FlinkSQLTypesEnum.values()){
            if(typeEnum.isUnsupported()){
                flinkSQLTypes.add(typeEnum.getType());
            }
        }

        List<String> basicTypes= new ArrayList<String>();
        basicTypes.add(Types.STRING.toString());
        basicTypes.add(Types.INT.toString());
        //自定义对象 即:行
        basicTypes.add("Row");
        //自定义数组类
        basicTypes.add("List");
        basicTypes.add(Types.CHAR.toString());
        basicTypes.add(Types.BOOLEAN.toString());
        basicTypes.add(Types.DOUBLE.toString());
        basicTypes.add(Types.FLOAT.toString());
        basicTypes.add(Types.LONG.toString());
        basicTypes.add(Types.BIG_DEC.toString());
        basicTypes.add(Types.BIG_INT.toString());
        basicTypes.add(Types.BYTE.toString());
        basicTypes.add(Types.SHORT.toString());
        basicTypes.add(Types.LOCAL_DATE.toString());
        basicTypes.add(Types.LOCAL_TIME.toString());
        basicTypes.add(Types.LOCAL_DATE_TIME.toString());
        basicTypes.add(Types.SQL_DATE.toString());
        basicTypes.add(Types.SQL_TIME.toString());
        basicTypes.add(Types.SQL_TIMESTAMP.toString());
        basicTypes.add(Types.INSTANT.toString());
        basicTypes.add(Types.VOID.toString());

        map.put("flinkSQLTypes",flinkSQLTypes);
        map.put("basicTypes",basicTypes);

        dictCache.put("ds_typeList",map);
        return Response.success(map);
    }
}
