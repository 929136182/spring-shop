package com.yzp.springshopindexweb.com.yzp.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.yzp.common.constant.CookieConstant;
import com.yzp.common.pojo.ResultBean;
import com.yzp.entity.TProductType;
import com.yzp.product.api.IProductTypeService;
import com.yzp.user.api.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class IndexController {

    @Reference
    private IUserService userService;
    @Reference
    private IProductTypeService productTypeService;

    @RequestMapping("index")
    public String showIndex(Model model){
        List<TProductType> productTypes = productTypeService.list();
        model.addAttribute("productTypes",productTypes);
        return "index";
    }

    @RequestMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(@CookieValue(name = CookieConstant.USER_TOKEN,required = false)String uuid){
        ResultBean resultBean = userService.checkIsLogin(uuid);
        System.out.println(resultBean);
        return resultBean;
    }
}
