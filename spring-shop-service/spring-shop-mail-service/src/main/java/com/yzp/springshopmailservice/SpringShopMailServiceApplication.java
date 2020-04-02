package com.yzp.springshopmailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringShopMailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringShopMailServiceApplication.class, args);
    }

}
