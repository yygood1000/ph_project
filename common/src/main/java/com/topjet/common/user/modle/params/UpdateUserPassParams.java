package com.topjet.common.user.modle.params;

/**
 * 重置密码
 * Created by tsj004 on 2017/8/15.
 */

public class UpdateUserPassParams{
    private String mobile;
    private String user_pass;

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public void setPwd(String user_pass) {
        this.user_pass = user_pass;
    }
}
