package com.sangeng.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Auther:yukemeng Date:2022/4/12-04-12-19:34
 * Description:
 * version:1.0
 */
@SpringBootApplication
@MapperScan("com.sangeng.security.mapper") // 扫描包
public class SecurityApplication {
    public static void main(String[] args) {

        SpringApplication.run(SecurityApplication.class,args);
    }
}
