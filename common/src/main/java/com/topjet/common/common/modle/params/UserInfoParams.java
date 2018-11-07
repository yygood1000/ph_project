package com.topjet.common.common.modle.params;

/**
 * creator: zhulunjun
 * time:    2017/11/13
 * describe: 用户信息
 */

public class UserInfoParams {
    private String user_id;	// 货主id	是	String
    private String page;	// 页数

    public UserInfoParams(String user_id, String page) {
        this.user_id = user_id;
        this.page = page;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
