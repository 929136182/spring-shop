package com.yzp.springshopproductservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.yzp.common.IBaseDao;
import com.yzp.common.IBaseServiceImpl;
import com.yzp.entity.TProduct;
import com.yzp.entity.TProductDesc;
import com.yzp.mapper.TProductDescMapper;
import com.yzp.mapper.TProductMapper;
import com.yzp.product.api.IProductService;
import com.yzp.product.api.dto.ProductDto;
import com.yzp.product.api.dto.UpProductDto;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.apache.zookeeper.data.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Service
@Component
public class ProductServiceImpl extends IBaseServiceImpl<TProduct> implements IProductService{
    @Autowired
    private TProductMapper productMapper;
    @Autowired
    private TProductDescMapper productDescMapper;
    @Autowired
    private SolrClient solrClient;
    @Override
    public IBaseDao<TProduct> getIbaseDao() {
        return productMapper;
    }

    @Override
    public Long save(ProductDto dto) {
        TProduct product = dto.gettProduct();
        productMapper.insertSelective(product);
        Long id = dto.gettProduct().getId();

        TProductDesc productDesc = new TProductDesc();
        productDesc.setProductId(id);
        productDesc.setProductDesc(dto.getProductDesc());
        productDescMapper.insertSelective(productDesc);
        return  id;

    }

    @Override
    public UpProductDto selectById(Long id) {
        UpProductDto productDto = new UpProductDto();
        TProductDesc tProductDesc = new TProductDesc();
        tProductDesc.setProductId(id);
        TProductDesc productDesc = productDescMapper.selectById(tProductDesc.getProductId());
        TProduct product = productMapper.selectByPrimaryKey(id);
        productDto.settProduct(product);
        productDto.settProductDesc(productDesc);
        return productDto;

    }


    @Override
    public Integer upDateById(UpProductDto dto) {
        TProduct product = dto.gettProduct();
        int i = productMapper.updateByPrimaryKey(product);
        TProductDesc productDesc = dto.gettProductDesc();
        productDesc.setProductId(product.getId());
        int i1 = productDescMapper.upDateById(productDesc);
        if (i1 == i && i > 0 && i1 > 0){
            return i;
        }
        return i=0;
    }


}

