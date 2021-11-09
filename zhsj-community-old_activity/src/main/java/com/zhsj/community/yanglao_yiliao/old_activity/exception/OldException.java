package com.zhsj.community.yanglao_yiliao.old_activity.exception;

import com.zhsj.community.yanglao_yiliao.common.constant.ConstError;
import com.zhsj.community.yanglao_yiliao.common.exception.JSYError;
import com.zhsj.community.yanglao_yiliao.common.exception.JSYException;

/**
 * @author ling
 * @since 2020-11-11 14:13
 */
public class OldException extends JSYException {

	public OldException(Integer code, String message) {
		super(code, message);
	}

	public OldException() {
		super();
	}

	public OldException(String message) {
		super(ConstError.NORMAL, message);
	}

	public OldException(JSYError error) {
		super(error);
	}
}
