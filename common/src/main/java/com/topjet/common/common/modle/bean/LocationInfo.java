package com.topjet.common.common.modle.bean;

/**
 * Created by yy on 2017/12/28.
 * 定位信息
 */

public class LocationInfo {
    private String address1;//定位一级地址
    private String address2;//定位二级地址
    private String address3;//定位三级地址
    private double longitude;//经度
    private double latitude;//纬度
    private String detail;//定为详细地址
    private String city_id;//城市ID

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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
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

    @Override
    public String toString() {
        return "LocationInfo{" +
                "address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", address3='" + address3 + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", detail='" + detail + '\'' +
                ", city_id='" + city_id + '\'' +
                '}';
    }
}
