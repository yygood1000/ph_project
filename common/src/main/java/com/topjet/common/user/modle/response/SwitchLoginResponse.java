package com.topjet.common.user.modle.response;

/**
 * switch登录  返回信息
 * Created by tsj-004 on 2017/12/4.
 */

public class SwitchLoginResponse {
    private String user_id = null;//用户id
    private String user_status = null;//用户实名认证状态
    private String user_name = null;//用户姓名
    private String user_type = null;//用户类型
    private String version = null;//用户数据版本

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

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}