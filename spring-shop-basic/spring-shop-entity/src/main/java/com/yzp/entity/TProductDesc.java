package com.yzp.entity;

import java.io.Serializable;

public class TProductDesc implements Serializable {
    private Long id;

    private Long productId;

    private String productDesc;

    public TProductDesc(Long id, Long productId, String productDesc) {
        this.id = id;
        this.productId = productId;
        this.productDesc = productDesc;
    }

    public TProductDesc() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc == null ? null : productDesc.trim();
    }
}