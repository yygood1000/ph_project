package com.topjet.common.user.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * creator: yy
 * time:    2017/10/13
 * describe:密码
 */

public class PasswordExtra extends BaseExtra {
    private String password;

    public PasswordExtra(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
