package com.yzp.search.api;

import com.yzp.common.pojo.PageResultBean;
import com.yzp.common.pojo.ResultBean;
import com.yzp.entity.TProduct;

import java.util.List;

public interface ISearchService<T> {
        ResultBean<T> synAllData();

        List<TProduct> getProductByKeywords(String productKeywords);

        PageResultBean<TProduct> getProductByKeywordsByPage(String productKeywords, Integer pageIndex, Integer pageSize);

}
