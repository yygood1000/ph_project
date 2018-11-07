package com.topjet.common.order_detail.modle.bean;

import com.topjet.common.base.model.BaseExtra;

/**
 * creator: zhulunjun
 * time:    2017/9/6
 * describe: 发货人,收货人 信息类,订单中显示的用户信息
 */

public class OrderUserInfo{
    private String name;	//发货人姓名	是	string
    private String mobile;	//发货人手机号	是	string
    private String city_id;	//出发地地址ID	是	string
    private String city;	//出发地地址	是	string
    private String detailed_address;	//详细出发地	是	string
    private String longitude;	//发货地经度	是	string
    private String latitude;	//发货地纬度	是	string

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetailed_address() {
        return detailed_address;
    }

    public void setDetailed_address(String detailed_address) {
        this.detailed_address = detailed_address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
