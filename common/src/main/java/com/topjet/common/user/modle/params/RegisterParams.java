package com.topjet.common.user.modle.params;

/**
 * Created by yy on 2017.08.16
 * 该接口由560配货平台用户【注册用户】时使用
 */

public class RegisterParams {
    private String mobile;//手机号	是	string
    private String pass_word;//密码	是	string
    private String check_code;//验证码	是	string
    private String msg_push_token;//推送token	否	string
    private String registered_city_id;//注册地城市id	否	string	定位当前的城市id
    private String gps_address_city_id;//GPS地址城市id	是	string	记录操作时GPS信息
    private String gps_longitude;//	GPS经度	是	string	记录操作时GPS信息
    private String gps_latitude;//GPS纬度	是	string	记录操作时GPS信息
    private String gps_detail;//	GPS详细地址	是	string	记录操作时GPS信息
    private String gps_remark = "用户注册";//上传GPS备注	否	string	特定业务场景时需添加备注例如”用户登录”/”报价货源

    public RegisterParams() {
    }

    public RegisterParams(String mobile, String pass_word, String check_code, String
            msg_push_token, String registered_city_id, String gps_address_city_id, String gps_longitude, String
                                  gps_latitude, String gps_detail, String gps_remark) {
        this.mobile = mobile;
        this.pass_word = pass_word;
        this.check_code = check_code;
        this.msg_push_token = msg_push_token;
        this.registered_city_id = registered_city_id;
        this.gps_address_city_id = gps_address_city_id;
        this.gps_longitude = gps_longitude;
        this.gps_latitude = gps_latitude;
        this.gps_detail = gps_detail;
        this.gps_remark = gps_remark;
    }

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

    public String getCheck_code() {
        return check_code;
    }

    public void setCheck_code(String check_code) {
        this.check_code = check_code;
    }

    public String getMsg_push_token() {
        return msg_push_token;
    }

    public void setMsg_push_token(String msg_push_token) {
        this.msg_push_token = msg_push_token;
    }

    public String getRegistered_city_id() {
        return registered_city_id;
    }

    public void setRegistered_city_id(String registered_city_id) {
        this.registered_city_id = registered_city_id;
    }

    public String getGps_address_city_id() {
        return gps_address_city_id;
    }

    public void setGps_address_city_id(String gps_address_city_id) {
        this.gps_address_city_id = gps_address_city_id;
    }

    public String getGps_longitude() {
        return gps_longitude;
    }

    public void setGps_longitude(String gps_longitude) {
        this.gps_longitude = gps_longitude;
    }

    public String getGps_latitude() {
        return gps_latitude;
    }

    public void setGps_latitude(String gps_latitude) {
        this.gps_latitude = gps_latitude;
    }

    public String getGps_detail() {
        return gps_detail;
    }

    public void setGps_detail(String gps_detail) {
        this.gps_detail = gps_detail;
    }

    public String getGps_remark() {
        return gps_remark;
    }

    @Override
    public String toString() {
        return "RegisterParams{" +
                "mobile='" + mobile + '\'' +
                ", pass_word='" + pass_word + '\'' +
                ", check_code='" + check_code + '\'' +
                ", msg_push_token='" + msg_push_token + '\'' +
                ", registered_city_id='" + registered_city_id + '\'' +
                ", gps_address_city_id='" + gps_address_city_id + '\'' +
                ", gps_longitude='" + gps_longitude + '\'' +
                ", gps_latitude='" + gps_latitude + '\'' +
                ", gps_detail='" + gps_detail + '\'' +
                ", gps_remark='" + gps_remark + '\'' +
                '}';
    }
}