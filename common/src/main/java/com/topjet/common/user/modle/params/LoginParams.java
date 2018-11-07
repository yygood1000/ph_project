package com.topjet.common.user.modle.params;

/**
 * Created by tsj-004 on 2017/8/9.
 * 登录请求参数
 */

public class LoginParams{
    private String mobile;      //手机号
    private String pass_word;   //密码
    private String msg_push_token;      //推送token
    private String login_address;   //登陆时定位的地址
    private String login_city_id;   // 登录时定位的城市ID
    private String login_type;  //登录系统类型 1:密码登录 2:验证码登录
    private String system_type; //APP类型 1 Android司机 2.Android货主

    private String login_longitude; // 经度
    private String login_latitude;// 纬度

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPass_word() {
        return pass_word;
    }

    public void setPass_word(String pass_word) {
        this.pass_word = pass_word;
    }

    public String getMsg_push_token() {
        return msg_push_token;
    }

    public void setMsg_push_token(String msg_push_token) {
        this.msg_push_token = msg_push_token;
    }

    public String getLogin_address() {
        return login_address;
    }

    public void setLogin_address(String login_address) {
        this.login_address = login_address;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getSystem_type() {
        return system_type;
    }

    public void setSystem_type(String system_type) {
        this.system_type = system_type;
    }

    public String getLogin_city_id() {
        return login_city_id;
    }

    public void setLogin_city_id(String login_city_id) {
        this.login_city_id = login_city_id;
    }

    public String getLogin_longitude() {
        return login_longitude;
    }

    public void setLogin_longitude(String login_longitude) {
        this.login_longitude = login_longitude;
    }

    public String getLogin_latitude() {
        return login_latitude;
    }

    public void setLogin_latitude(String login_latitude) {
        this.login_latitude = login_latitude;
    }
}