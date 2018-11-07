package com.topjet.common.user.modle.params;

/**
 * Created by yy on 2017/11/3.
 * <p>
 * 修改密码相关请求字段
 */

public class PasswordParams {

    private String password_old;// 旧密码
    private String password_new;// 新密码

    public PasswordParams(String password_old) {
        this.password_old = password_old;
    }

    public PasswordParams(String password_old, String password_new) {
        this.password_old = password_old;
        this.password_new = password_new;
    }
}
