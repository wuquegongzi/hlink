package com.haibao.admin.interceptor;

import java.lang.annotation.*;

/**
 * 权限注解 validate = false 不进行权限校验，validate = true校验权限，默认值是true
 *
 * 此注解可以注解在类上和方法上
 * 如果注解在类上，当validate=false时则不对此类下的所有方法不进行权限校验
 * 如果注解在方法上，当validate=false时则不对此方法进行权限校验
 */
@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    boolean validate() default true;
}