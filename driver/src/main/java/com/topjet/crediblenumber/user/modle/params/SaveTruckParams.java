package com.topjet.crediblenumber.user.modle.params;

/**
 * Created by tsj-004 on 2017/8/9.
 * 司机车辆认证请求参数
 */

public class SaveTruckParams {
    private String plate_no1;//	车牌号码第1级  京A123456 京字为第一级号码
    private String plate_no2;//	车牌号码第2级  京A123456 A为第2级号码
    private String plate_no3;//	车牌号码第3级  京A123456 123456字为第3级号码
    private String plate_color;//	车牌颜色 	1 蓝色 2 黄色
    private String truck_typeId;//车型id
    private String truck_lengthId;//	车长id
    private String truck_head_img;//	车头照
    private String driver_license_img;//	行驶证照片

    private String driver_truck_id = null;//车辆信息id
    private String truck_length = null;//车型
    private String truck_type = null;//车长
    private String truck_head_img_key = null;//车头照key
    private String truck_head_img_url = null;//车头照url
    private String driver_license_img_key = null;//行驶证照片key
    private String driver_license_img_url = null;// 行驶证照片url
    private String audit_status = null;// 认证状态 1 未认证 2 已认证 3 认证中 4 认证失败
    private String audit_status_remark = null;//审核备注
    private String version = null;  // 数据版本

    public String getPlate_no1() {
        return plate_no1;
    }

    public void setPlate_no1(String plate_no1) {
        this.plate_no1 = plate_no1;
    }

    public String getPlate_no2() {
        return plate_no2;
    }

    public void setPlate_no2(String plate_no2) {
        this.plate_no2 = plate_no2;
    }

    public String getPlate_no3() {
        return plate_no3;
    }

    public void setPlate_no3(String plate_no3) {
        this.plate_no3 = plate_no3;
    }

    public String getPlate_color() {
        return plate_color;
    }

    public void setPlate_color(String plate_color) {
        this.plate_color = plate_color;
    }

    public String getTruck_typeId() {
        return truck_typeId;
    }

    public void setTruck_typeId(String truck_typeId) {
        this.truck_typeId = truck_typeId;
    }

    public String getTruck_lengthId() {
        return truck_lengthId;
    }

    public void setTruck_lengthId(String truck_lengthId) {
        this.truck_lengthId = truck_lengthId;
    }

    public String getTruck_head_img() {
        return truck_head_img;
    }

    public void setTruck_head_img(String truck_head_img) {
        this.truck_head_img = truck_head_img;
    }

    public String getDriver_license_img() {
        return driver_license_img;
    }

    public void setDriver_license_img(String driver_license_img) {
        this.driver_license_img = driver_license_img;
    }

    public String getDriver_truck_id() {
        return driver_truck_id;
    }

    public void setDriver_truck_id(String driver_truck_id) {
        this.driver_truck_id = driver_truck_id;
    }

    public String getTruck_length() {
        return truck_length;
    }

    public void setTruck_length(String truck_length) {
        this.truck_length = truck_length;
    }

    public String getTruck_type() {
        return truck_type;
    }

    public void setTruck_type(String truck_type) {
        this.truck_type = truck_type;
    }

    public String getTruck_head_img_key() {
        return truck_head_img_key;
    }

    public void setTruck_head_img_key(String truck_head_img_key) {
        this.truck_head_img_key = truck_head_img_key;
    }

    public String getTruck_head_img_url() {
        return truck_head_img_url;
    }

    public void setTruck_head_img_url(String truck_head_img_url) {
        this.truck_head_img_url = truck_head_img_url;
    }

    public String getDriver_license_img_key() {
        return driver_license_img_key;
    }

    public void setDriver_license_img_key(String driver_license_img_key) {
        this.driver_license_img_key = driver_license_img_key;
    }

    public String getDriver_license_img_url() {
        return driver_license_img_url;
    }

    public void setDriver_license_img_url(String driver_license_img_url) {
        this.driver_license_img_url = driver_license_img_url;
    }

    public String getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(String audit_status) {
        this.audit_status = audit_status;
    }

    public String getAudit_status_remark() {
        return audit_status_remark;
    }

    public void setAudit_status_remark(String audit_status_remark) {
        this.audit_status_remark = audit_status_remark;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}