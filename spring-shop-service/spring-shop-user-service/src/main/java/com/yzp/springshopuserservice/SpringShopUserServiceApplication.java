package com.yzp.springshopuserservice;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.yzp.mapper")
public class SpringShopUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringShopUserServiceApplication.class, args);
    }

}
