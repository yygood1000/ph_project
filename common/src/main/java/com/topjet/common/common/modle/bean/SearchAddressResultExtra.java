package com.topjet.common.common.modle.bean;

import com.topjet.common.base.model.BaseExtra;

/**
 * Created by yy on 2017/10/20.
 * <p>
 * 搜索地址 页面返回地图页面 参数实体类
 */

public class SearchAddressResultExtra extends BaseExtra {
    public double lat;
    public double lng;
    public String adCode;
    public String provinceName;
    public String cityName;
    public String districtName;
    public String fullAddress;// 完整的地址

    @Override
    public String toString() {
        return "SearchAddressResultExtra{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", adCode='" + adCode + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", fullAddress='" + fullAddress + '\'' +
                '}';
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}
