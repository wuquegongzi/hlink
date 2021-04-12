package com.haibao.admin.config.log;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.haibao.admin.web.entity.SysOperLog;
import com.haibao.admin.web.service.ISysOperLogService;
import com.haibao.flink.utils.GsonUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/*
 * @Author ml.c
 * @Description 全局统一日志切面
 * @Date 13:48 2020-04-15
 **/
@Aspect
@Component
public class WebLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebLogAspect.class);

    @Autowired
    ISysOperLogService sysOperLogService;

    private SysOperLog sysOperLog = new SysOperLog();


    @Pointcut("execution(public * com.haibao.admin.web.controller..*.*(..))")
    public void webLog() {

    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

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

    }


    /**
     * 在切点之后织入
     * @throws Throwable
     */
    @After("webLog()")
    public void doAfter() throws Throwable {
//        LOGGER.info("=========================================== End ===========================================");
//        LOGGER.info("");
    }

//    @AfterReturning(returning="ret",pointcut="webLog()")
//    public void doAfterReturning(Object ret)throws Throwable {
//        //处理完请求，返回内容
//        System.out.println("切面日志记录 doAfter begin ===");
//        System.out.println("RESPONSE："+ret);
//        System.out.println("切面日志记录 doAfter end ===");
//    }

    /**
     * 环绕
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();

        String jsonResult = GsonUtils.gsonString(result);
        jsonResult = jsonResult.length() > 2000 ? jsonResult.substring(0,1995)+"...": jsonResult;

        sysOperLog.setStatus(1);
        sysOperLog.setJsonResult(jsonResult);

        sysOperLogService.sendLog(sysOperLog);

        return result;
    }

}
