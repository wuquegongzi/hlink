package com.haibao.admin.config.exception;

import com.haibao.admin.web.common.enums.CodeEnum;
import com.haibao.admin.web.common.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/*
 * @Author ml.c
 * @Description 全局异常捕获
 * @Date 14:47 2020-04-23
 **/
@ControllerAdvice
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {

    /**
     *
     * 功能描述:  捕获 自定义的业务异常，并处理，该异常需要手动抛出
     */
    @ExceptionHandler(CustomException.class)
    public Response handleServiceException(CustomException e) {
        log.error("业务异常：{}", e);
        Response response = new Response();
        response.setSuccess(false);
        response.setCode(e.getCode());
        response.setMsg(e.getMessage());
        return response;
    }


    /**
     *
     * 功能描述:  捕获 HttpMediaTypeNotSupportedException 异常，并处理，该异常由验证框架抛出
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Response handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.error("HttpMediaTypeNotSupportedException : {}",e);
        Response response = new Response();
        response.setSuccess(false);
        response.setCode(3000);
        response.setMsg("HttpMediaTypeNotSupportedException : MediaType 不正确");
        return response;
    }

    /**
     *
     * 功能描述:  捕获 HttpRequestMethodNotSupportedException 异常，并处理，该异常由验证框架抛出
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException : {}",e);
        Response response = new Response();
        response.setSuccess(false);
        response.setCode(3000);
        response.setMsg("HttpRequestMethodNotSupportedException : 不支持的 http method");
        return response;
    }


    /**
     *
     * 功能描述:  捕获 HttpMessageNotReadableException 异常，并处理，该异常由验证框架抛出
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException {}: ",e);
        Response response = new Response();
        response.setSuccess(false);
        response.setCode(3000);
        response.setMsg("HttpMessageNotReadableException : 非法的http请求");
        return response;
    }

    /**
     *
     * 功能描述:  捕获 MissingServletRequestParameterException 异常，并处理，该异常由验证框架抛出
     *
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException : ",e);
        Response response = new Response();
        response.setSuccess(false);
        response.setCode(3000);
        response.setMsg("MissingServletRequestParameterException : 参数缺失");
        return response;
    }

    /**
     *
     * 功能描述:  捕获 ConstraintViolationException 异常，并处理，该异常由验证框架抛出
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public Response handleResourceNotFoundException(ConstraintViolationException e) {
        log.error("ConstraintViolationException : {}",e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage() + "\n");
        }
        Response response = new Response();
        response.setSuccess(false);
        response.setCode(3000);
        response.setMsg("ConstraintViolationException : "+ strBuilder.toString());
        return response;
    }

    /**
     * 功能描述:  捕获 MethodArgumentNotValidException 异常，并处理，该异常由验证框架抛出
     *
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : {}",e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s %s", field, code);

        Response response = new Response();
        response.setSuccess(false);
        response.setCode(3000);
        response.setMsg("MethodArgumentNotValidException : "+ message);

        return response;
    }


    /**
     *
     * 功能描述:  搂底的异常处理，正常来讲不应该走到这里的，如果走到这里需要特别注意
     *
     */
    @ExceptionHandler(value = Exception.class)
    public Response defaultErrorHandler(Exception e) {
        log.error("发生其他异常,详细信息：{}", e);
        Response response = Response.error(CodeEnum.SYSTEM_ERROR,"服务器错误，请稍后重试");
        return response;

    }
}
