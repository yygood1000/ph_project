package com.topjet.common.order_detail.modle.extra;


import com.topjet.common.base.model.BaseExtra;

/**
 * Created by tsj-004 on 2017/9/5.
 */

public class CityAndLocationExtra extends BaseExtra {
    private String cityName;     // 市名
    private String lastName;     // 最后的名字
    private String backwards2Name; // 倒数2个名字
    private String latitude;
    private String longitude;   // 经纬度
    private String address;     // 详细地址
    private String title;       // 地址poi的名称
    private String name;        // 姓名
    private String phone;       // 手机号
    private String cityCode;    //
    private String adCode;      //
    private String pageTitle;   // 标题

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBackwards2Name() {
        return backwards2Name;
    }

    public void setBackwards2Name(String backwards2Name) {
        this.backwards2Name = backwards2Name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    @Override
    public String toString() {
        return "CityAndLocationExtra{" +
                "cityName='" + cityName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", backwards2Name='" + backwards2Name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", adCode='" + adCode + '\'' +
                '}';
    }
}
