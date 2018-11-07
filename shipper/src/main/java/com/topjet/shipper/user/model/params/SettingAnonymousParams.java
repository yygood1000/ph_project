package com.topjet.shipper.user.model.params;

/**
 * Created by yy on 2017/10/27.
 * 个人中心-货主-匿名设置
 */

public class SettingAnonymousParams {

    private String is_anonymous;

    public SettingAnonymousParams(String is_anonymous) {
        this.is_anonymous = is_anonymous;
    }

    @Override
    public String toString() {
        return "SettingAnonymousParams{" +
                "is_anonymous='" + is_anonymous + '\'' +
                '}';
    }
}
