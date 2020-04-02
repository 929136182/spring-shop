package com.yzp.springshopcartweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yzp.cart.api.ICartService;
import com.yzp.common.constant.CookieConstant;
import com.yzp.common.constant.RedisConstant;
import com.yzp.common.pojo.ResultBean;
import com.yzp.entity.TUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@RequestMapping("cart")
public class CartController {

    @Reference
    private ICartService cartService;

    @RequestMapping("addToCart/{productId}/{count}")
    @ResponseBody
    public ResultBean addToCart(@PathVariable("productId")Long productId,
                                @PathVariable("count")Integer count,
                                @CookieValue(name = CookieConstant.USER_CART_TOKEN,
                                required = false)String uuid,
                                HttpServletResponse response,
                                HttpServletRequest request){
        //已登录
        TUser user = (TUser) request.getAttribute("user");
        if (user != null){
            //拼接已登录key
            //拼接redis中的key
            String uKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(user.getId()).toString();
            ResultBean resultBean = cartService.addToCart(uKey,productId,count);
            resetCookie(uuid,response);
            return resultBean;

        }
        if (uuid == null || uuid.equals("")){
            //用户第一次添加购物车为其生成uuid
            uuid = UUID.randomUUID().toString();

        }
        String unKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(uuid).toString();
        ResultBean resultBean = cartService.addToCart(unKey,productId,count);
        resetCookie(uuid,response);
        return resultBean;
    }

    private void resetCookie(String uuid, HttpServletResponse response) {
        Cookie cookie = new Cookie(CookieConstant.USER_CART_TOKEN,uuid);
        cookie.setPath("/");
        cookie.setMaxAge(30*24*60*60);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    @RequestMapping("getCart")
    @ResponseBody
    public ResultBean getCart(@CookieValue(value = CookieConstant.USER_CART_TOKEN,required = false)String uuid,
                              HttpServletResponse response,
                              HttpServletRequest request){
        //已登录
        TUser user = (TUser) request.getAttribute("user");
        if (user != null){
            //拼接已登录key
            //拼接redis中的key
            String uKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(user.getId()).toString();
            ResultBean resultBean = cartService.getCart(uKey);
            resetCookie(uuid,response);
            return resultBean;
        }
        if (uuid == null || uuid.equals("")){
            return new ResultBean(1,null,"用户不存在购物车");
        }
        String unKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(uuid).toString();
        ResultBean resultBean = cartService.getCart(unKey);
        if (resultBean.getCode()==1){
            return new ResultBean(1,null,"用户不存在购物车");

        }
        resetCookie(uuid,response);
        return resultBean;
    }

    @RequestMapping("getCartVo")
    @ResponseBody
    public ResultBean  getCartVO(@CookieValue(value = CookieConstant.USER_CART_TOKEN,required = false)String uuid,
                                HttpServletResponse response,HttpServletRequest request,Model model){
        TUser user = (TUser) request.getAttribute("user");
        if (user != null){
            //拼接已登录key
            //拼接redis中的key
            String uKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(user.getId()).toString();
            ResultBean resultBean = cartService.getCartVO(uKey);
            resetCookie(uuid,response);
            model.addAttribute("resultBean",resultBean);
            return resultBean;
        }
        if (uuid == null || uuid.equals("")){
             ResultBean resultBean = new ResultBean(1,null,"用户不存在购物车");
             model.addAttribute("resultBean",resultBean);
            return new ResultBean(1,null,"用户不存在购物车");

        }
        String unKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(uuid).toString();
        //ResultBean resultBean = cartService.getCart(unKey);
        ResultBean resultBean = cartService.getCartVO(unKey);
        if (resultBean.getCode()==1){
            ResultBean resultBean1 =  new ResultBean(1,null,"用户不存在购物车");
            model.addAttribute("resultBean",resultBean1);
            return new ResultBean(1,null,"用户不存在购物车");
        }
        resetCookie(uuid,response);
        model.addAttribute("resultBean",resultBean);
        return resultBean;
    }

    @RequestMapping("updateCart/{productId}/{count}")
    @ResponseBody
    public ResultBean updateCart(@PathVariable("productId")Long productId,@PathVariable("count")Integer count,
                                 @CookieValue(name = CookieConstant.USER_CART_TOKEN,required = false)String uuid,
                                 HttpServletResponse response,HttpServletRequest request){
        //已登录
        TUser user = (TUser) request.getAttribute("user");
        if (user != null){
            //拼接已登录key
            //拼接redis中的key
            String uKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(user.getId()).toString();
            ResultBean resultBean = cartService.resetCount(uKey,productId,count);
            resetCookie(uuid,response);
            return resultBean;
        }
        if (uuid == null || uuid.equals("")){
            return  new ResultBean(1,null,"用户不存在购物车");

        }
        String unKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(uuid).toString();
        ResultBean resultBean = cartService.resetCount(unKey,productId,count);
        resetCookie(uuid,response);
        return resultBean;
    }

    @RequestMapping("remove/{productId}")
    @ResponseBody
    public ResultBean remove(@PathVariable("productId")Long productId,
                             @CookieValue(name = CookieConstant.USER_CART_TOKEN,required = false)String uuid,
                             HttpServletResponse response,HttpServletRequest request){
        //已登录
       TUser user = (TUser) request.getAttribute("user");
        if (user != null){
            //拼接已登录key
            //拼接redis中的key
            String uKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(user.getId()).toString();
            ResultBean resultBean = cartService.remove(uKey,productId);
            resetCookie(uuid,response);
            return resultBean;
        }
        System.out.println(uuid.toString());
        if (uuid==null || uuid.equals("")){
            return new ResultBean(1,null,"用户不存在购物车");
        }
        String unKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(uuid).toString();
        ResultBean resultBean = cartService.remove(unKey, productId);
        resetCookie(uuid,response);
        return resultBean;
    }

    @RequestMapping("merge")
    @ResponseBody
    public ResultBean merge(@CookieValue(name = CookieConstant.USER_CART_TOKEN,
    required = false)String uuid,HttpServletResponse response,
                            HttpServletRequest request){
        if (uuid == null || uuid.equals("")){
            return new ResultBean(1,null,"不存在未登录购物车，不需要合并");
        }
        TUser user = (TUser) request.getAttribute("user");
        String loginKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(user.getId()).toString();
        String noLoginKey = new StringBuilder(RedisConstant.USER_CART_TOKEN_PRE).append(uuid).toString();
        ResultBean resultBean = cartService.merge(noLoginKey,loginKey);
        resetCookie(uuid,response);
        return resultBean;

    }
}
