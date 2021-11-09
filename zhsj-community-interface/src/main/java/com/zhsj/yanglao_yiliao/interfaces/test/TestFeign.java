package com.zhsj.yanglao_yiliao.interfaces.test;

import com.zhsj.baseweb.annotation.LoginIgnore;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author zzm
 * @version 1.0
 * @Description:
 * @date 2021/11/9 16:31
 */
@FeignClient(name = "community-yiliao-service-service",path = "test")
public interface TestFeign {

    @LoginIgnore
    @PostMapping("/test1")
    void test(@RequestBody String str);
}
