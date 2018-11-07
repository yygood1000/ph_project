package com.topjet.common.base.net.response;

import java.io.Serializable;

/**
 * 服务端返回的内层子数据类
 * Created by tsj004 on 2017/7/31.
 */

public class Error implements Serializable {
    private String business_code;
    private String business_msg;
    private String exception;
    private String message;

    public String getBusiness_code() {
        return business_code;
    }

    public void setBusiness_code(String business_code) {
        this.business_code = business_code;
    }

    public String getBusiness_msg() {
        return business_msg;
    }

    public void setBusiness_msg(String business_msg) {
        this.business_msg = business_msg;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}