package com.yzp.product.api;

import com.yzp.common.IBaseService;
import com.yzp.entity.TProduct;
import com.yzp.product.api.dto.ProductDto;
import com.yzp.product.api.dto.UpProductDto;

public interface IProductService extends IBaseService<TProduct> {

    Long save(ProductDto dto);

    UpProductDto selectById(Long id);

    Integer upDateById(UpProductDto dto);
}
