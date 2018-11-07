package com.topjet.common.user.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * creator: zhulunjun
 * time:    2017/10/13
 * describe:手机号
 */

public class PhoneExtra extends BaseExtra {
    private String phone;

    public PhoneExtra(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
