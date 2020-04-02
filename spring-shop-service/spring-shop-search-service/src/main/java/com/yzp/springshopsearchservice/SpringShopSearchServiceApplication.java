package com.yzp.springshopsearchservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.yzp.mapper")
public class SpringShopSearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringShopSearchServiceApplication.class, args);
    }

}
