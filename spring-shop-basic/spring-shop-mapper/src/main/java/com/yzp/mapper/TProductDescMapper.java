package com.yzp.mapper;

import com.yzp.common.IBaseDao;
import com.yzp.entity.TProductDesc;

public interface TProductDescMapper extends IBaseDao<TProductDesc> {
    TProductDesc selectById(Long productId);
    Integer upDateById(TProductDesc productDesc);
}