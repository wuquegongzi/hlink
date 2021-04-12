package com.haibao.admin.web.common.result;

import java.lang.annotation.*;

/**
 * Controller默认进行Response包装，对于特殊不需要的，使用该注解可以忽略包装
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Response_IGNORE {

}
