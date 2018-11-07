package com.topjet.common.user.modle.params;

/**
 * 切换app后登录请求参数
 * Created by tsj-004 on 2017/12/4.
 */

public class SwitchLoginParams {
    private String switch_key = null;//切换用的key
    private String login_address = null;//登录地址
    private String login_city_id = null;//登录城市id
    private String msg_push_token = null;//推送的token

    public String getSwitch_key() {
        return switch_key;
    }

    public void setSwitch_key(String switch_key) {
        this.switch_key = switch_key;
    }

    public String getLogin_address() {
        return login_address;
    }

    public void setLogin_address(String login_address) {
        this.login_address = login_address;
    }

    public String getLogin_city_id() {
        return login_city_id;
    }

    public void setLogin_city_id(String login_city_id) {
        this.login_city_id = login_city_id;
    }

    public String getMsg_push_token() {
        return msg_push_token;
    }

    public void setMsg_push_token(String msg_push_token) {
        this.msg_push_token = msg_push_token;
    }
}