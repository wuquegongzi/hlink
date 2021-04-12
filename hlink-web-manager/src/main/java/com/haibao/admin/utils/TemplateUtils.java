package com.haibao.admin.utils;

import cn.hutool.core.util.StrUtil;
import com.haibao.admin.web.vo.templete.JsonField;
import com.haibao.admin.web.vo.templete.Option;
import com.haibao.admin.web.vo.templete.UnionField;

import java.util.List;
import java.util.Map;

/*
 * @Author ml.c
 * @Description json模版工具类
 * @Date 14:10 2020-03-31
 **/
public class TemplateUtils {

    public static Map jsonTemplateExtract(List<JsonField> jsonFields, Map map) {

        jsonFields.forEach(jsonField -> {
            map.put(jsonField.getFieldName(), StrUtil.isEmpty(jsonField.getFieldValue()) ? jsonField.getDefaultValue() : jsonField.getFieldValue());

            //判断嵌套属性
            List<Option> options = jsonField.getOption();
            if(options != null){
                for (Option o:options) {
                    if(o.isHasUnion()){
                        List<UnionField> unionFields =o.getUnionFields();
                        for (UnionField unionField:unionFields) {
                            map.put(unionField.getFieldName(),StrUtil.isEmpty(unionField.getFieldValue()) ? unionField.getDefaultValue() : unionField.getFieldValue());
                        }
                    }
                }
            }
        });

        return map;
    }
}
