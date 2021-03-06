package com.zhsj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lxjr
 * @date 2021/11/9 9:49
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.zhsj.community.yanglao_yiliao.healthydata.mapper")
@EnableAsync
public class HealthDataApplication {
    public static void main(String[] args) {
        SpringApplication.run(HealthDataApplication.class, args);
    }
}
