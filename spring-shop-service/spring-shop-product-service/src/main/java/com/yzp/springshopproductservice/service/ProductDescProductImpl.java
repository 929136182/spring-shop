package com.yzp.springshopproductservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.yzp.common.IBaseDao;
import com.yzp.common.IBaseServiceImpl;
import com.yzp.entity.TProduct;
import com.yzp.entity.TProductDesc;
import com.yzp.mapper.TProductDescMapper;
import com.yzp.product.api.IProductDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Service
@Component
public class ProductDescProductImpl extends IBaseServiceImpl<TProductDesc> implements IProductDescService {
    @Autowired
    private TProductDescMapper productDescMapper;
    @Override
    public IBaseDao<TProductDesc> getIbaseDao() {
        return productDescMapper;
    }
}
