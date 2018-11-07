package com.topjet.common.im.model.response;

import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/12/7
 * describe: 用户状态
 */

public class UserStatusResponse {
    private String im_status;	// 是	string	在线状态	0否1是-1错误

    public static final String ON_LINE = "1"; // 在线
    public static final String OFF_LINE = "0"; // 离线
    public static final String ERROR = "-1"; // 错误

    public String getIm_status() {
        if(StringUtils.isEmpty(im_status) || im_status.equals(ERROR)){
            return "";
        } else if(im_status.equals(ON_LINE)){
            return "（在线）";
        } else {
            return "（离线）";
        }
    }

    public void setIm_status(String im_status) {
        this.im_status = im_status;
    }
}
