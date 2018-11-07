package com.topjet.shipper.user.model.bean;

/**
 * 通过手机号获取常用联系人信息
 * 添加常用联系人时 校验电话号码
 * Created by tsj004 on 2017/8/28.
 */

public class GetUserInfoByMobileParams {

    public GetUserInfoByMobileParams(String mobile) {
        this.mobile = mobile;
    }

    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
