package com.zhsj.community.yanglao_yiliao.yiliao_service.controller.test;

import com.zhsj.baseweb.annotation.LoginIgnore;
import org.springframework.web.bind.annotation.*;

/**
 * @author zzm
 * @version 1.0
 * @Description:
 * @date 2021/11/9 16:22
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @LoginIgnore
    @PostMapping("/test1")
    public void test(@RequestBody String str) {
        System.out.println("==============================" + str);
    }
}
