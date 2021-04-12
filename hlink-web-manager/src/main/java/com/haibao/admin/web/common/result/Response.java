package com.haibao.admin.web.common.result;

import com.haibao.admin.web.common.enums.CodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;

import java.io.Serializable;

/**
 * Author: Leslie Kai
 * Date: 2019-06-14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@ApiModel("公共 响应")
public class Response<T> implements Serializable {
	public static final Response SUCCESS = new Response(CodeEnum.SUCCESS);
	public static final Response FAIL = new Response(CodeEnum.BUSSINESS_ERROR);
	public static final Response FORBIDDEN = new Response(CodeEnum.UNAUTHORIZED_ERROR);
	public static final Response NULL = new Response(CodeEnum.NOT_FOUND_ERROR);
	public static final Response EXCEPTION = new Response(CodeEnum.SYSTEM_ERROR);
	public static final Response PARAM_INVALID = new Response(CodeEnum.PARAM_ERROR);

	@ApiModelProperty(value = "请求是否成功")
	private boolean success = true;
	@ApiModelProperty(value = "状态码")
	private int code;
	@ApiModelProperty(value = "状态信息")
	private String msg;
	@ApiModelProperty(value = "数据")
	private T data;
	private static MessageSource messageSource;

	/**
	 * 构造一个返回特定代码的Response对象
	 *
	 * @param code
	 */
	public Response(CodeEnum code) {
		this.setCode(code.getCode());
		this.setMsg(code.getMsg());
	}

	public static <T> Response<T> success(T data) {
		Response<T> response = new Response<T>();
		response.setData(data);
		response.setSuccess(true);
		response.setMsg(CodeEnum.SUCCESS.getMsg());
		response.setCode(CodeEnum.SUCCESS.getCode());
		return response;
	}
	public static <T> Response<T> success() {
		Response<T> response = new Response<T>();
		response.setSuccess(true);
		response.setMsg(CodeEnum.SUCCESS.getMsg());
		response.setCode(CodeEnum.SUCCESS.getCode());
		return response;
	}
	public static <T> Response<T> error(CodeEnum msg) {
		Response<T> response = new Response<T>();
		response.setSuccess(false);
		response.setCode(msg.getCode());
		response.setMsg(msg.getMsg());
		return response;
	}

	public static <T> Response<T> error(CodeEnum codeEnum,String message) {
		Response<T> response = new Response<T>();
		response.setSuccess(false);
		response.setCode(codeEnum.getCode());
		response.setMsg(message);
		return response;
	}

	public static <T> Response<T> error(int code,String message) {
		Response<T> response = new Response<T>();
		response.setSuccess(false);
		response.setCode(code);
		response.setMsg(message);
		return response;
	}
}
