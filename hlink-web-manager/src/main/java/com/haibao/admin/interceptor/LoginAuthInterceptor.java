package com.haibao.admin.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import com.haibao.flink.utils.GsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 *
 */
public class LoginAuthInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(LoginAuthInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (null != request.getParameter("tokenFilter") && "1".equals(request.getParameter("tokenFilter")) || request.getRequestURI().contains("api/common/cookie")) {
            // 开发阶段允许权限不校验
            return true;
        }
        // 获取UR
        String requestURI = request.getRequestURI();

        //获取语言
        Cookie lang = ServletUtil.getCookie(request,"lang");

        //前端传了lang且为中文
        if (lang != null && "cn".equals(lang.getValue())) {
            LocaleContextHolder.setLocale(new Locale("zh", "CN"));
        } else {
            //不传或其他一律为英文
            LocaleContextHolder.setLocale(new Locale("en", "US"));
        }

        // 获取cookie
        Cookie cookie = ServletUtil.getCookie(request, "_haiabao_token_");
        if (cookie == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "LOGINFIRST");
            return false;
        }

        String token = cookie.getValue();
        if (StringUtils.isBlank(token)) {
            logger.error("用户未登录");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "LOGINFIRST");
            return false;
        }

        String paramsJson = GsonUtils.gsonString(request.getParameterMap());
        String method = request.getMethod();

        logger.info("进入拦截器，requestURI={}，method={}，params={}，token={}", requestURI, method, paramsJson, token);

        if (StringUtils.containsIgnoreCase(requestURI, "exportList")) {
            return true;
        }

        try {

        } catch (Exception e) {
            logger.error(e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "AUTHSERVICEFAIL");
            return false;
        }

//        threadContext.set();

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

    /**
     * 校验注解权限
     *
     * @param handler
     * @return 如果不进行权限校验则直接返回ture
     */
    private boolean checkPermission(Object handler) {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // 获取类级注解
            Permission permission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Permission.class);

            // 判定如果不进行权限校验直接返回ture
            if (permission != null && !permission.validate()) {
                return true;
            }
            // 获取方法级注解
            Permission methodPermission = handlerMethod.getMethod().getAnnotation(Permission.class);
            if (methodPermission != null && !methodPermission.validate()) {
                return true;
            }
        }

        return false;
    }
}
