package com.zhsj.community.yanglao_yiliao.common.utils;

import com.zhsj.basecommon.enums.ErrorEnum;
import com.zhsj.basecommon.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author lilin
 * @since  2020/11/13 18:04
 */
@Slf4j
public class ValidatorUtils {
    private static final Validator VALIDATOR;

    static {
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws BaseException 校验不通过，则抛异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws BaseException {
        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for (ConstraintViolation<Object> constraint : constraintViolations) {
                msg.append(constraint.getMessage()).append(",");
            }
            throw new BaseException(ErrorEnum.COMMON_QUANTITY_LIMIT,msg.substring(0, msg.length() - 1));
        }
    }


}