package com.topjet.common.user.modle.params;

/**
 * 修改头像请求数据
 * Created by tsj-004 on 2017/11/9.
 */

public class EditAvatarParams {
    private String user_icon = null;//用户头像

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }
}