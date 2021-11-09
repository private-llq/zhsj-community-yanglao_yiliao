package com.zhsj.community.yanglao_yiliao.myself.controller.test;

import com.zhsj.baseweb.annotation.LoginIgnore;
import com.zhsj.yanglao_yiliao.interfaces.test.TestFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zzm
 * @version 1.0
 * @Description:
 * @date 2021/11/9 16:34
 */
@RestController
@RequestMapping("/t")
public class Tcontroller {

    @Autowired
    private TestFeign feign;

    @LoginIgnore
    @PostMapping("/test")
    public void t() {
        feign.test("ffffffffffffffffffffff");
    }
}
