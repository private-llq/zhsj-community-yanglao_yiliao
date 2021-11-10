package com.zhsj.community.yanglao_yiliao.healthydata.controller;

import com.zhsj.basecommon.enums.ErrorEnum;
import com.zhsj.basecommon.exception.BaseException;
import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实例代码
 * @author lxjr
 * @date 2021/11/10 9:14
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/test")
    public R<Void> test() {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        throw new BaseException(ErrorEnum.SERVER_BUSY);
    }

    @GetMapping("/test1")
    public R<Object> test1() {
        return R.ok(new Object());
    }
}
