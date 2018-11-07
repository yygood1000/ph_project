package com.topjet.common.user.modle.response;

import com.topjet.common.utils.StringUtils;

/**
 * Created by tsj-004 on 2017/7/21.
 * 用户信息
 */

public class LoginResponse {
    private String user_id;
    private String user_status; // 用户实名认证状态 ？ 哪几个状态
    private String user_name;
    private String version; // 数据的版本
    private String is_h5_login_one; // 是否属于H5注册第一次登陆	String	1是 2不是

    private String is_congeal;  // 是否冻结	0：未冻结 1：冻结
    private String congeal_remark;  // 冻结备注
    private String congeal_time;    // 冻结时间

    public void setIs_congeal(String is_congeal) {
        this.is_congeal = is_congeal;
    }

    public String getCongeal_remark() {
        return congeal_remark;
    }

    public void setCongeal_remark(String congeal_remark) {
        this.congeal_remark = congeal_remark;
    }

    public String getCongeal_time() {
        return congeal_time;
    }

    public void setCongeal_time(String congeal_time) {
        this.congeal_time = congeal_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean getIs_h5_login_one() {
        return StringUtils.isNotBlank(is_h5_login_one) && is_h5_login_one.equals("1");
    }

    public boolean getIs_congeal() {
        return StringUtils.isNotBlank(is_congeal) && is_congeal.equals("1");
    }

    public void setIs_h5_login_one(String is_h5_login_one) {
        this.is_h5_login_one = is_h5_login_one;
    }
}
