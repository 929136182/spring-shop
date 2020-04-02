package com.yzp.springshopproductservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.yzp.common.IBaseDao;
import com.yzp.common.IBaseServiceImpl;
import com.yzp.entity.TProductType;
import com.yzp.mapper.TProductMapper;
import com.yzp.mapper.TProductTypeMapper;
import com.yzp.product.api.IProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Service
@Component
public class ProductTypeServiceImpl extends IBaseServiceImpl<TProductType> implements IProductTypeService {
    @Autowired
    private TProductTypeMapper tProductTypeMapper;
    @Override
    public IBaseDao<TProductType> getIbaseDao() {
        return tProductTypeMapper;
    }
}
