package com.topjet.common.user.modle.response;

/**
 * 找回密码返回类
 * Created by tsj004 on 2017/8/17.
 */

public class RetrievePasswordResponse {
    private String code;  //1.成功 2.失败
    private String msg;
    private String status; // 0通过 1密码错误 2超过六次

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
