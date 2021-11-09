package com.zhsj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lxjr
 * @date 2021/11/9 10:01
 */
@SpringBootApplication
@MapperScan("com.zhsj.community.yanglao_yiliao.myself.mapper")
public class YanglaoYiliaoMyselfApplication {
    public static void main(String[] args) {
        SpringApplication.run(YanglaoYiliaoMyselfApplication.class, args);
    }
}
