package com.zhsj.community.yanglao_yiliao.common.exception;

import com.zhsj.community.yanglao_yiliao.common.constant.ConstError;

/**
 * 错误枚举
 *
 * @author ling
 * @since 2020-11-11 11:02
 */
public enum JSYError {
	/**
	 * 错误请求
	 */
	BAD_REQUEST(ConstError.BAD_REQUEST, "错误的请求"),
	UNAUTHORIZED(ConstError.UNAUTHORIZED, "未认证"),
	FORBIDDEN(ConstError.FORBIDDEN, "禁止访问"),
	NOT_FOUND(ConstError.NOT_FOUND, "页面丢失了"),
	NOT_SUPPORT_REQUEST_METHOD(ConstError.NOT_SUPPORT_REQUEST_METHOD, "不支持的请求类型"),
	REQUEST_PARAM(ConstError.REQUEST_PARAM, "请求参数错误"),
	INTERNAL(ConstError.INTERNAL, "服务器错误"),
	NOT_IMPLEMENTED(ConstError.NOT_IMPLEMENTED, "未实现"),
	GATEWAY(ConstError.GATEWAY, "网关错误"),
	DUPLICATE_KEY(ConstError.DUPLICATE_KEY, "数据已存在!请检查重复的数据"),
	NO_REAL_NAME_AUTH(ConstError.NO_REAL_NAME_AUTHENTICATION, "用户未实名认证!"),
	OPERATOR_INFORMATION_NOT_OBTAINED(ConstError.BAD_REQUEST, "未获取到操作员信息!"),
	NO_AUTH_HOUSE(ConstError.NO_AUTH_HOUSE, "房屋待认证!"),
	NOT_ENOUGH(ConstError.NOT_ENOUGH,"余额不足！");
	
	private final Integer code;
	private final String message;
	
	public Integer getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	
	JSYError(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
