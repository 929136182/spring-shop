package com.yzp.product.api.dto;

import com.yzp.entity.TProduct;
import com.yzp.entity.TProductDesc;

import java.io.Serializable;

public class UpProductDto implements Serializable {
    private TProduct tProduct;
    private TProductDesc tProductDesc;

    public UpProductDto(TProduct tProduct, TProductDesc tProductDesc) {
        this.tProduct = tProduct;
        this.tProductDesc = tProductDesc;
    }

    public UpProductDto() {
    }

    public TProduct gettProduct() {
        return tProduct;
    }

    public void settProduct(TProduct tProduct) {
        this.tProduct = tProduct;
    }

    public TProductDesc gettProductDesc() {
        return tProductDesc;
    }

    public void settProductDesc(TProductDesc tProductDesc) {
        this.tProductDesc = tProductDesc;
    }

    @Override
    public String toString() {
        return "UpProductDto{" +
                "tProduct=" + tProduct +
                ", tProductDesc=" + tProductDesc +
                '}';
    }
}
