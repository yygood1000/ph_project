package com.topjet.common.im.model.params;

/**
 * creator: zhulunjun
 * time:    2017/12/7
 * describe: 环信用户id
 */

public class UserIdParams {
    private String user_id;	// 是	string	环信用户ID

    public UserIdParams(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
