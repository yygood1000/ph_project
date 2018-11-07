package com.topjet.common.common.modle.bean;

import com.topjet.common.base.model.BaseExtra;

/**
 * Created by yy on 2017/12/28.
 * 接口上传带上GPS信息
 */

public class GpsInfo extends BaseExtra {
    private String gps_address_city_id;//GPS地址城市ID	否	串
    private String gps_longitude;//GGPS经度	否	串
    private String gps_latitude;//GGPS纬度	否	串
    private String gps_detail;//GGPS详细地址	否	串
    private String gps_remark;//GGPS备注	否	串

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

    public void setGps_remark(String gps_remark) {
        this.gps_remark = gps_remark;
    }
}
