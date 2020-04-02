package com.yzp.cart.api.vo;

import com.yzp.entity.TProduct;

import java.io.Serializable;
import java.util.Date;

public class CartItemVo implements Serializable {

    private TProduct product;
    private Integer count;
    private Date updateTime;

    public CartItemVo(TProduct product, Integer count, Date updateTime) {
        this.product = product;
        this.count = count;
        this.updateTime = updateTime;
    }

    public CartItemVo() {
    }

    public TProduct getProduct() {
        return product;
    }

    public void setProduct(TProduct product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CartItemVo{" +
                "product=" + product +
                ", count=" + count +
                ", updateTime=" + updateTime +
                '}';
    }
}
