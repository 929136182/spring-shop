package com.yzp.common.pojo;

import java.io.Serializable;

public class ResultBean<T> implements Serializable {
    private Integer code;
    private  T data;

    @Override
    public String toString() {
        return "ResultBean{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }

    private String message;

    public ResultBean() {
    }

    public ResultBean(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public ResultBean(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResultBean(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResultBean errResult(String data){
        ResultBean bean = new ResultBean();
        bean.setData(data);
        bean.setCode(1);
        return bean;
    }

    public static ResultBean successResult(String data){
        ResultBean bean = new ResultBean();
        bean.setData(data);
        bean.setCode(0);
        return bean;
    }
}
