package com.topjet.common.common.modle.response;

/**
 * Created by tsj-004 on 2017/7/21.
 * 首页-切换app前获取key   返回类
 */

public class GetSwitchKeyResponse {
    private String switch_key = null;   // 切换的key
    private String load_url = null;     // 安卓的下载地址

    public String getSwitch_key() {
        return switch_key;
    }

    public void setSwitch_key(String switch_key) {
        this.switch_key = switch_key;
    }

    public String getLoad_url() {
        return load_url;
    }

    public void setLoad_url(String load_url) {
        this.load_url = load_url;
    }
}