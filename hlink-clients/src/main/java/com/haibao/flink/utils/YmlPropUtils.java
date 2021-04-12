package com.haibao.flink.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.util.StrUtil;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @ClassName YmlPropUtils
 * @Description 读取application.yml
 * @Author ml.c
 * @Date 2020/3/2 7:56 下午
 * @Version 1.0
 */
public class YmlPropUtils {

    private LinkedHashMap prop;
    private static YmlPropUtils ymlPropUtils;

    private YmlPropUtils() {
       new YmlPropUtils("prod");
    }

    /**
     * 私有构造，禁止直接创建
     */
    private YmlPropUtils(String profilesActive) {
        BootYaml yaml = new BootYaml();
        yaml.setActive("spring.profiles.active");
        yaml.setInclude("spring.profiles.include");
        yaml.setPrefix("application");
        String confFile = "application.yml";
        if ("dev".equals(profilesActive)){
            confFile = "application-dev.yml";
        }else if("test".equals(profilesActive)){
            confFile = "application-test.yml";
        }else if("prod".equals(profilesActive)){
            confFile = "application-prod.yml";
        }
        prop = yaml.loadAs(confFile);
    }

    /**
     * 获取单例
     *
     * @return YmlPropUtils
     */
    public static YmlPropUtils getInstance(String profilesActive) {
        if (ymlPropUtils == null) {
            ymlPropUtils = new YmlPropUtils(profilesActive);
        }
        return ymlPropUtils;
    }

    /**
     * 根据属性名读取值
     * 先去主配置查询，如果查询不到，就去启用配置查询
     *
     * @param name 名称
     */
    public Object getProperty(String name) {
        LinkedHashMap param = prop;
        List<String> split = StrSpliter.split(name, StrUtil.C_DOT, true, true);
        for (int i = 0; i < split.size(); i++) {
            if (i == split.size() - 1) {
                return param.get(split.get(i));
            }
            param = Convert.convert(LinkedHashMap.class, param.get(split.get(i)));
        }
        return null;
    }
}
