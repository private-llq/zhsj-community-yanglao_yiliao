package com.zhsj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lxjr
 * @date 2021/11/9 10:01
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.zhsj.community.yanglao_yiliao.myself.mapper")
public class YanglaoYiliaoMyselfApplication {
    public static void main(String[] args) {
        SpringApplication.run(YanglaoYiliaoMyselfApplication.class, args);
    }
}
