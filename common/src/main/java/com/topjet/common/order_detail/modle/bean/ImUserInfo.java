package com.topjet.common.order_detail.modle.bean;

/**
 * creator: zhulunjun
 * time:    2018/1/4
 * describe: 聊天用户信息 只有货源详情用到
 */

public class ImUserInfo {
    private String user_name;	// 用户真实姓名	是	string
    private String icon_key;	// 用户头像key	是	string
    private String icon_url;	// 用户头像url	是	string

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getIcon_key() {
        return icon_key;
    }

    public void setIcon_key(String icon_key) {
        this.icon_key = icon_key;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }
}
