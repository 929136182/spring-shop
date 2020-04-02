package com.yzp.springshopcartweb.intercepter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yzp.common.constant.CookieConstant;
import com.yzp.common.pojo.ResultBean;
import com.yzp.entity.TUser;
import com.yzp.user.api.IUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Reference
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (CookieConstant.USER_TOKEN.equals(cookie.getName())){
                    String uuid = cookie.getValue();
                    //拿到用户凭证去检查当前用户的登录状态
                    ResultBean resultBean = userService.checkIsLogin(uuid);
                    if (resultBean.getCode()==0){
                        TUser user = (TUser) resultBean.getData();
                        request.setAttribute("user",user);
                        return true;
                    }
                }
            }
        }
        return true;
    }
}
