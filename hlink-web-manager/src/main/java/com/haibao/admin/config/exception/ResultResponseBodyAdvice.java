package com.haibao.admin.config.exception;

import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.common.result.Response_IGNORE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
/*
 * @Author ml.c
 * @Description Controller 统一返回包装
 * @Date 13:33 2020-04-23
 **/
@ControllerAdvice
public class ResultResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultResponseBodyAdvice.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if (methodParameter.hasMethodAnnotation(Response_IGNORE.class)){
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {

        // 此处获取到request 为特殊需要的时候处理使用
//		HttpServletRequest req = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        // 下面处理统一返回结果（统一code、msg、sign 加密等）
        if (selectedConverterType == MappingJackson2HttpMessageConverter.class
                && (selectedContentType.equals(MediaType.APPLICATION_JSON)
                || selectedContentType.equals(MediaType.APPLICATION_JSON_UTF8))) {
            if (body == null) {
                return Response.NULL;
            } else if (body instanceof Response) {
                return body;
            } else {
                // 异常
                if (returnType.getExecutable().getDeclaringClass().isAssignableFrom(BasicErrorController.class)) {
                    Response vo = Response.EXCEPTION;
                    HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
                    if (req.getRequestURL().toString().contains("localhost")
                            || req.getRequestURL().toString().contains("127.0.0.1")) {
                        vo.setData(body);
                    }
                    return vo;
                } else {
                    return body;
                }
            }
        }
        return body;
    }

}
