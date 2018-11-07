package com.topjet.common.common.modle.params;

/**
 * Created by tsj-004 on 2017/11/10.
 *
 * 定时上传定位信息 请求参数
 */

public class FixedCycleLocationParams {
    private String address1 = null;//定位一级地址
    private String address2 = null;//定位二级地址
    private String address3 = null;//定位三级地址
    private String longitude = null;//经度
    private String latitude = null;//纬度
    private String detail = null;//定为详细地址
    private String city_id = null;//城市ID

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }
}
