package com.haibao.admin.utils;

import cn.hutool.core.util.RandomUtil;
import com.haibao.admin.web.common.enums.JobTypeEnum;

import java.util.Date;

/**
 * Created by baoyu on 2020/3/2.
 * Describe 辅助类
 */
public class HelpUtils {

    public static String getJobCode(int type){
        String yyyyMMdd=DateUtil.formatDateTimeTwo(new Date());
        if(JobTypeEnum.JAR.getType()==type){
            return "J"+yyyyMMdd+ RandomUtil.randomNumbers(4);
        }else{
            return "S"+yyyyMMdd+RandomUtil.randomNumbers(4);
        }
    }

}
