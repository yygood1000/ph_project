package com.topjet.common.contact.model;

/**
 * creator: zhulunjun
 * time:    2017/10/17
 * describe: 邀请熟车
 */

public class InviteTruckResponse {
    private String is_top_user;	// 是否是平台用户		0:非该平台用户 1:为平台用户
    public static final String NO_USER = "0";
    public static final String IS_USER = "1";
    private String msg_info;	//提示文字

    public String getIs_top_user() {
        return is_top_user;
    }

    public void setIs_top_user(String is_top_user) {
        this.is_top_user = is_top_user;
    }

    public String getMsg_info() {
        return msg_info;
    }

    public void setMsg_info(String msg_info) {
        this.msg_info = msg_info;
    }
}
