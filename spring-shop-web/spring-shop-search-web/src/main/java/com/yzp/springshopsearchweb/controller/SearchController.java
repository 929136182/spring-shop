package com.yzp.springshopsearchweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yzp.common.pojo.PageResultBean;
import com.yzp.common.pojo.ResultBean;
import com.yzp.entity.TProduct;
import com.yzp.search.api.ISearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("search")
public class SearchController {
    @Reference
    private ISearchService searchService;

    @RequestMapping("init_db_data")
    @ResponseBody
    public String synAllData(){
        ResultBean bean = searchService.synAllData();
        return bean.toString();
    }


    @RequestMapping("searchByKeywords")
    public String searchByKeywords(String productKeywords,Model model){
        List<TProduct> productList = searchService.getProductByKeywords(productKeywords);
       model.addAttribute("products",productList);
        return "show_list";

    }

    @RequestMapping("searchByKeywords/{pageIndex}/{pageSize}")
    public String searchByKeywords(String productKeywords,Model model, @PathVariable("pageIndex") Integer pageIndex, @PathVariable("pageSize") Integer pageSize){
        PageResultBean<TProduct> pageResultBean = searchService.getProductByKeywordsByPage(productKeywords,pageIndex,pageSize);
        pageResultBean.setNavigatePages(3);
        model.addAttribute("pageInfo",pageResultBean);
        model.addAttribute("productKeywords",productKeywords);
        return "show_list";

    }
}
