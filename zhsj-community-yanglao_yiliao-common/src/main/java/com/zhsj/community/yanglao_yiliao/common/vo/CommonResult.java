package com.zhsj.community.yanglao_yiliao.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhsj.community.yanglao_yiliao.common.constant.ConstError;
import com.zhsj.community.yanglao_yiliao.common.exception.JSYError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 公共返回类
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> implements Serializable {
	private int code;

	@JsonInclude()
	private String message;

	@JsonInclude()
	private T data;
	
	public static <T> CommonResult<T> ok(T data) {
		return new CommonResult<>(0, null, data);
	}
	
	public static <T> CommonResult<T> ok(T data,String msg) {
		return new CommonResult<>(0, msg, data);
	}

	public static <T> CommonResult<T> ok(String msg) {
		return new CommonResult<>(0, msg, null);
	}
	
	public static CommonResult<Boolean> ok() {
		return new CommonResult<>(0, "操作成功", true);
	}


	public static CommonResult<Boolean> error(int code, String message) {
		return new CommonResult<>(code, message, false);
	}

	public static <T> CommonResult<T> error(int code, T data) {
		return new CommonResult<>(code, null, data);
	}

	public static CommonResult<Boolean> error(JSYError error) {
		return error(error.getCode(), error.getMessage());
	}
	
	public static CommonResult<Boolean> error(String message) {
		return error(ConstError.NORMAL, message);
	}


}
