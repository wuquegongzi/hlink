package com.haibao.admin.web.common.enums;

import lombok.Getter;

/**
 * Author: Leslie Kai
 * Date: 2019-06-14
 */
public enum CodeEnum {
	SUCCESS(200, "请求成功"),
	BUSSINESS_ERROR(0, "业务异常"),
	UNKNOW_ERROR(-1, "未知异常"),
	SYSTEM_ERROR(-2, "系统异常"),
	PARAM_ERROR(-3, "参数不正确"),
	DATABASE_ERROR(-4, "数据库异常"),
	ENCRYPT_ERROR(-5, "加解密异常"),
	CACHE_ERROR(-6, "缓存异常"),
	UNAUTHORIZED_ERROR(-7, "未授权"),
	NOT_FOUND_ERROR(-8, "请求不存在"),
	UPLOAD_REMOTE_ERROR(SYSTEM_ERROR.code,"jar包上传异常"),
	UPLOAD_LOCAL_ERROR(SYSTEM_ERROR.code,"JAR包上传异常"),
	GET_JARS_ERROR(SYSTEM_ERROR.code,"JAR包列表获取异常" ),
	DELETE_JARS_ERROR(SYSTEM_ERROR.code,"JAR包删除异常" ),
	UPLOAD_UDF_LOCAL_ERROR(SYSTEM_ERROR.code,"自定义函数JAR包上传异常"),
	DELETE_UDF_LOCAL_ERROR(SYSTEM_ERROR.code,"自定义函数JAR包删除异常"),
	UPDATE_UDF_LOCAL_ERROR(SYSTEM_ERROR.code,"自定义函数JAR包更新异常"),
	UDF_NAME_REPEAT(SYSTEM_ERROR.code,"UDF名字不可重复"),
	JOB_START_ERROR(SYSTEM_ERROR.code,"作业启动异常" ),
	PLEASE_CONFIG_JOB_INFO(BUSSINESS_ERROR.code,"请配置作业信息" ),
	PLEASE_CONFIG_JAR_ID(BUSSINESS_ERROR.code,"请配置作业JAR"),
	JOB_NOT_FOUND(BUSSINESS_ERROR.code,"作业不存在"),
	JOB_IS_RUNNING(BUSSINESS_ERROR.code,"作业正在运行中"),
	JOB_NOT_RUNNINT(BUSSINESS_ERROR.code,"作业非运行状态，不可进行取消操作"),
	JOB_NOT_RUNNING_CANNOT_TRIGGER_SAVEPOINT(BUSSINESS_ERROR.code,"作业非运行状态,不可触发保存点操作"),
	JOB_CANCEL_ERROR(BUSSINESS_ERROR.code,"作业取消失败"),
	GET_JOB_LIST_ERROR(SYSTEM_ERROR.code,"获取作业信息列表异常"),
	GET_JOB_MANAGER_CONIG_ERROR(SYSTEM_ERROR.code,"作业管理获取异常"),
	GET_FLINK_OVERVIEW_ERROR(SYSTEM_ERROR.code,"Flink概要信息获取异常"),
	GET_FLINK_JOB_MANAGER_ERROR(SYSTEM_ERROR.code,"Flink作业管理器信息获取异常"),
	CLUSTER_INFO_IS_NULL(BUSSINESS_ERROR.code,"无法检索到集群信息"),
	GET_SAVE_POINT_STATUS_ERROR(SYSTEM_ERROR.code,"获取作业保存点信息异常"),
	GET_SAVE_POINT_PATH_EMPTY(SYSTEM_ERROR.code,"获取到的作业保存点路径为空"),
	SAVE_POINT_ROOT_PATH_IS_EMPTY(BUSSINESS_ERROR.code,"未检索到保存点路径"),
	SAVE_POINT_DISPOSAL_ERROR(SYSTEM_ERROR.code,"历史保存点路径清理失败"),
	FLINK_JOB_ID_IS_EMPTY(BUSSINESS_ERROR.code,"FLINK作业ID不可为空"),
	;

	@Getter
	private int code;

	@Getter
	private String msg;

	CodeEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
