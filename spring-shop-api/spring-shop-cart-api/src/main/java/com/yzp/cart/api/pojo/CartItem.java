package com.yzp.cart.api.pojo;

import java.io.Serializable;
import java.util.Date;

public class CartItem implements Serializable {
    private Long    productId;
    private Integer count;
    private Date updateTime;

    public CartItem(Long productId, Integer count, Date updateTime) {
        this.productId = productId;
        this.count = count;
        this.updateTime = updateTime;
    }

    public CartItem() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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
        return "CartItem{" +
                "productId=" + productId +
                ", count=" + count +
                ", updateTime=" + updateTime +
                '}';
    }
}
