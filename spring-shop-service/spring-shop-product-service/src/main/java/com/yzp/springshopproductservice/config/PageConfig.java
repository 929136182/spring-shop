package com.yzp.springshopproductservice.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class PageConfig {
    @Bean
    public PageHelper getPageHelper(){
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("dialect","mysql");
        p.setProperty("reasonable","true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}
