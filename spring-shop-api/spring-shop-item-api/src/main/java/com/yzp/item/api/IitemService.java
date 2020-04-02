package com.yzp.item.api;

import com.yzp.common.pojo.ResultBean;
import com.yzp.entity.TProduct;

import java.util.List;

public interface IitemService {
    //生成商品详情
    ResultBean createItemPages(TProduct product);
    //批量生成详情页
    ResultBean batchCreateItemPages(List<TProduct> products);
}
