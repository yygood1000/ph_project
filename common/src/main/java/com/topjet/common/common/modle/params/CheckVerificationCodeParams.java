package com.topjet.common.common.modle.params;

/**
 * 校验验证码
 * Created by tsj004 on 2017/8/18.
 */

public class CheckVerificationCodeParams {
    private String mobile;
    private String code_type;    // 1.登录 2.注册 3.找回密码
    private String code;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getcode_type() {
        return code_type;
    }

    public void setcode_type(String code_type) {
        this.code_type = code_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
