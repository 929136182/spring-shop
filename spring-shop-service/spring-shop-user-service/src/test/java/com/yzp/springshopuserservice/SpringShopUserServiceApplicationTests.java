package com.yzp.springshopuserservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SpringShopUserServiceApplicationTests {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void contextLoads() {
    }

    @Test
    public void testSecurity(){
        System.out.println("123加密结果" + bCryptPasswordEncoder.encode("123"));
        System.out.println("123加密匹配结果"+bCryptPasswordEncoder.matches("123","$2a$10$UK.utqs/RPrX5flzx6jQ1ucG2QYOhO7LYyq9DyRCIYxrhXpIJ3MPG"));
        System.out.println("123加密匹配结果:"+bCryptPasswordEncoder.matches("123", "$2a$10$sGQ.wT59Kk/J8iAPwnn/tOeik0frrOrz8Jt5.scVZkcGiJRU0Xmz."));

    }

}
