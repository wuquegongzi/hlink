package com.haibao.admin.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.haibao.admin.web.entity.SysOperLog;
import com.haibao.admin.web.service.ISysOperLogService;
import com.haibao.flink.utils.GsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/*
 * @Author ml.c
 * @Description 控制层拦截日志
 * @Date 14:02 2020-04-22
 **/
public class OperLogInterceptor extends HandlerInterceptorAdapter {

    private Logger LOGGER = LoggerFactory.getLogger(OperLogInterceptor.class);

    @Autowired
    ISysOperLogService sysOperLogService;

    private SysOperLog sysOperLog = new SysOperLog();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String URL = request.getRequestURL().toString();
        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();
        String ip = ServletUtil.getClientIP(request);
        Map<String, String> parm = ServletUtil.getParamMap(request);

        String operName = "";
        String deptName = "";
       //todo 获取用户信息

        String title ="root";
        String[] uris =requestURI.split("/");
        if(uris.length > 1){
            title = uris[1];
        }

        //（0其它 1新增 2修改 3删除）
        int bussinessType = 0;
        if(ServletUtil.METHOD_POST.equals(httpMethod)){
            bussinessType = 1;
        }else if(ServletUtil.METHOD_PUT.equals(httpMethod)){
            bussinessType = 2;
        }else if(ServletUtil.METHOD_DELETE.equals(httpMethod)){
            bussinessType = 3;
        }

        sysOperLog.setTitle(title);
        sysOperLog.setMethod(requestURI);
        sysOperLog.setOperIp(ip);
        sysOperLog.setBusinessType(bussinessType);
        sysOperLog.setOperUrl(URL);
        sysOperLog.setOperatorType(1); //操作类别（0其它 1后台用户 2手机端用户）
        sysOperLog.setRequestMethod(httpMethod);
        sysOperLog.setOperName(operName);
        sysOperLog.setDeptName(deptName);
        sysOperLog.setOperParam(GsonUtils.gsonString(parm));
        sysOperLog.setOperTime(DateUtil.toLocalDateTime(DateUtil.date()));

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

        //获取Controller返回值
//        Map logBodyMap = (Map) request.getSession().getAttribute("body");
//        sysOperLog.setJsonResult(GsonUtils.gsonString(logBodyMap));

        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        if(null == ex){
            sysOperLog.setStatus(0);
        }else{
            sysOperLog.setErrorMsg(ex.getMessage());
            sysOperLog.setStatus(1);
        }

        sysOperLogService.sendLog(sysOperLog);

        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

}
