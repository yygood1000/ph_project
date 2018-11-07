package com.topjet.common.user.modle.params;

/**
 * Created by tsj-004 on 2017.08.15
 * 发送验证码请求接口
 */

public class SendCheckCodeParams{
    private String mobile;
    private String sms_type;    // 1.登录 2.注册 3.找回密码

    public static final String LOGIN = "1";
    public static final String REGISTER = "2";
    public static final String FORGET_PASSWORD = "3";

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSms_type() {
        return sms_type;
    }

    public void setSms_type(String sms_type) {
        this.sms_type = sms_type;
    }
}