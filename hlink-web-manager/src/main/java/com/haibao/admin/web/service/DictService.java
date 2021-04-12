package com.haibao.admin.web.service;

import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TDict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author zc
 * @since 2020-02-28
 */
public interface DictService extends IService<TDict> {

     List<TDict> getByDictType(String dictType);

    TDict getByDictTypeAndValue(String dictType,String dictValue);

    Response<Map> getTypeList();
}
