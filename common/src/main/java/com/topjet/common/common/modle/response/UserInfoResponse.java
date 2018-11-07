package com.topjet.common.common.modle.response;

import com.topjet.common.common.modle.bean.UserInfo;

/**
 * creator: zhulunjun
 * time:    2017/11/13
 * describe: 诚信查询
 */

public class UserInfoResponse {


    private UserInfo query_integrity_user_info;	// 被查询用户信息	Object


    public UserInfo getQuery_integrity_user_info() {
        return query_integrity_user_info;
    }

    public void setQuery_integrity_user_info(UserInfo query_integrity_user_info) {
        this.query_integrity_user_info = query_integrity_user_info;
    }
}
