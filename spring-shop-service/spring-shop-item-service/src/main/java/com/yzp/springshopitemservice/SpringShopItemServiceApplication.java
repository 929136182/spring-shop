package com.yzp.springshopitemservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.yzp.mapper")
public class SpringShopItemServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringShopItemServiceApplication.class, args);
    }

}
