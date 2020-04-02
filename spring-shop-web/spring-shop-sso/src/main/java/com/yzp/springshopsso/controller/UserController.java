package com.yzp.springshopsso.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import com.yzp.common.constant.CookieConstant;
import com.yzp.common.constant.RedisConstant;
import com.yzp.common.pojo.ResultBean;
import com.yzp.common.utils.HttpClientUtils;
import com.yzp.entity.TUser;
import com.yzp.user.api.IUserService;
import org.apache.zookeeper.data.Id;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("user")
public class UserController {


    @Autowired
    private RedisTemplate redisTemplate;
    @Reference
    private IUserService userService;

    @RequestMapping("index")
    public String login1() {
/*        TUser user = new TUser();
        user.setUsername("123");
        TUser user1 = userService.checkIsExists(user);*/
        return "login";
    }

    @RequestMapping("login")
    public String login(TUser user, HttpServletResponse response,
                        @CookieValue(name = CookieConstant.USER_CART_TOKEN,
                                required = false)String cartToken) {
        System.out.println(user.toString());
        TUser currentUser = userService.checkIsExists(user);
        System.out.println(currentUser);
        if (currentUser == null) {
            return "login";
        }
        //生成uuid

        String uuid = UUID.randomUUID().toString();
        //保存到redis中
        String userKey = new StringBuilder(RedisConstant.USER_TOKEN_PRE).append(uuid).toString();
        currentUser.setPassword(null);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(userKey,currentUser);
        //设置有效期
        redisTemplate.expire(userKey,3, TimeUnit.MINUTES);
        //生成cookie
        Cookie cookie = new Cookie(CookieConstant.USER_TOKEN,uuid);
        cookie.setPath("/");
        //表示cookie不能用客户端脚本拿到
        //只能后端程序拿到
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        //合并购物车
        //模拟发送浏览器请求 httpClient Cookie:user_cart_token=sasaa;user_token=12212112
        Map<String,String> params = new HashMap<>();
        StringBuilder cookieValue = new StringBuilder(CookieConstant.USER_CART_TOKEN)
                .append("=").append(cartToken).append(";")
                .append(CookieConstant.USER_TOKEN)
                .append("=")
                .append(uuid);
        params.put("Cookie",cookieValue.toString());
        HttpClientUtils.doGetWithHeaders("http://localhost:9096/cart/merge",params);
        return "redirect:http://localhost:9092/index";
    }

    @RequestMapping("logout")
    @ResponseBody
    public ResultBean logout(@CookieValue(value = CookieConstant.USER_TOKEN,required = false)
                             String uuid,HttpServletResponse response){
        if (uuid==null){
            return new ResultBean(1,null,"当前用户未登录");

        }
        //删除redis中的数据
        String key = new StringBuilder(RedisConstant.USER_TOKEN_PRE).append(uuid).toString();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.delete(key);
        //删除cookie
        Cookie cookie = new Cookie(CookieConstant.USER_TOKEN,uuid);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return  new ResultBean(0,null,"退出成功");
    }

    @RequestMapping("isLogin")
    @ResponseBody
    public ResultBean checkIsLogin(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies==null){
            return  new ResultBean(1,"","当前用户未登录");
        }
        //找到cookie
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(CookieConstant.USER_TOKEN)){
                String uuid = cookie.getValue();
                //去redis查询
                redisTemplate.setKeySerializer(new StringRedisSerializer());
                String key = new StringBuilder(RedisConstant.USER_TOKEN_PRE).append(uuid).toString();
                TUser user = (TUser) redisTemplate.opsForValue().get(key);
                if (user!=null){
                    //重新刷新redis有效期
                    redisTemplate.expire(key,30,TimeUnit.MINUTES);
                    return new ResultBean(0,user,"当前用户已登录");
                }
            }
        }
        return new ResultBean(1,null,"当前用户未登录");
    }


}