package com.yzp.springshopproductservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan("com.yzp.mapper")
@EnableDubbo
public class SpringShopProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringShopProductServiceApplication.class, args);
    }

}
