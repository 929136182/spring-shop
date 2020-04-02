package com.yzp.entity;

import java.io.Serializable;
import java.util.Date;

public class TProduct implements Serializable {
    private Long id;

    private String name;

    private String image;

    private Long price;

    private Long salePrice;

    private String salePoint;

    private Long typeId;

    private String typeName;

    private Boolean flag;

    private Date createTime;

    private Date updateTime;

    private Long createUser;

    private Long updateUser;

    public TProduct(Long id, String name, String image, Long price, Long salePrice, String salePoint, Long typeId, String typeName, Boolean flag, Date createTime, Date updateTime, Long createUser, Long updateUser) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.salePrice = salePrice;
        this.salePoint = salePoint;
        this.typeId = typeId;
        this.typeName = typeName;
        this.flag = flag;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.createUser = createUser;
        this.updateUser = updateUser;
    }

    public TProduct() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Long salePrice) {
        this.salePrice = salePrice;
    }

    public String getSalePoint() {
        return salePoint;
    }

    public void setSalePoint(String salePoint) {
        this.salePoint = salePoint == null ? null : salePoint.trim();
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    @Override
    public String toString() {
        return "TProduct{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", salePrice=" + salePrice +
                ", salePoint='" + salePoint + '\'' +
                ", typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", flag=" + flag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                '}';
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }
}