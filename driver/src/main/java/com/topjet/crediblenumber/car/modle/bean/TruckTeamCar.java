package com.topjet.crediblenumber.car.modle.bean;

import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.utils.StringUtils;

/**
 * Created by tsj-004 on 2017/10/16.
 *
 * 车辆详情
 */

public class TruckTeamCar extends BaseExtra {
    private String driver_truck_id = null;// 司机车辆id
    private String truck_icon_key = null;// 车头照key
    private String truck_icon_url = null;// 车头照url
    private String truck_license_key = null;    // 车辆行驶证照key
    private String truck_license_url = null;    // 车辆行驶证照url
    private String plate_no1 = null;// 车牌照1级
    private String plate_no2 = null;// 车牌照2级
    private String plate_no3 = null;// 车牌照3级
    private String plate_color = null;// 车牌颜色 1 蓝色 2 黄色
    private String audit_status = null; // 认证状态 0 无需认证 1 未认证 2 已认证 3 认证中 4 认证失败
    private String truck_type_name = null;// 车型name
    private String truck_length_name = null;// 车长name
    private String truck_status = null;// 车辆状态 1 空车 求货中,2 休息
    private String driver_name = null;// 驾驶员姓名
    private String driver_mobile = null;// 驾驶员手机号
    private String truck_type_id = null;        // 车型id
    private String truck_length_id = null;      // 车长id
    private String driver_truck_version = null;     // 司机车辆版本号

    /**
     * 修改和添加车辆时用到
     */
    private String truck_icon_img = null;// 车头照片
    private String truck_license_img = null;// 行驶证照片

    public String getDriver_truck_id() {
        return driver_truck_id;
    }

    public void setDriver_truck_id(String driver_truck_id) {
        this.driver_truck_id = driver_truck_id;
    }

    public String getTruck_icon_key() {
        return truck_icon_key;
    }

    public void setTruck_icon_key(String truck_icon_key) {
        this.truck_icon_key = truck_icon_key;
    }

    public String getTruck_icon_url() {
        return truck_icon_url;
    }

    public void setTruck_icon_url(String truck_icon_url) {
        this.truck_icon_url = truck_icon_url;
    }

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

    public String getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(String audit_status) {
        this.audit_status = audit_status;
    }

    public String getTruck_type_name() {
        return truck_type_name;
    }

    public void setTruck_type_name(String truck_type_name) {
        this.truck_type_name = truck_type_name;
    }

    public String getTruck_length_name() {
        return truck_length_name;
    }

    public void setTruck_length_name(String truck_length_name) {
        this.truck_length_name = truck_length_name;
    }

    public String getTruck_status() {
        return truck_status;
    }

    public void setTruck_status(String truck_status) {
        this.truck_status = truck_status;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_mobile() {
        return driver_mobile;
    }

    public void setDriver_mobile(String driver_mobile) {
        this.driver_mobile = driver_mobile;
    }

    public String getTruck_license_key() {
        return truck_license_key;
    }

    public void setTruck_license_key(String truck_license_key) {
        this.truck_license_key = truck_license_key;
    }

    public String getTruck_license_url() {
        return truck_license_url;
    }

    public void setTruck_license_url(String truck_license_url) {
        this.truck_license_url = truck_license_url;
    }

    public String getTruck_type_id() {
        return truck_type_id;
    }

    public void setTruck_type_id(String truck_type_id) {
        this.truck_type_id = truck_type_id;
    }

    public String getTruck_length_id() {
        return truck_length_id;
    }

    public void setTruck_length_id(String truck_length_id) {
        this.truck_length_id = truck_length_id;
    }

    public String getDriver_truck_version() {
        return driver_truck_version;
    }

    public void setDriver_truck_version(String driver_truck_version) {
        this.driver_truck_version = driver_truck_version;
    }

    public String getTruck_icon_img() {
        return truck_icon_img;
    }

    public void setTruck_icon_img(String truck_icon_img) {
        this.truck_icon_img = truck_icon_img;
    }

    public String getTruck_license_img() {
        return truck_license_img;
    }

    public void setTruck_license_img(String truck_license_img) {
        this.truck_license_img = truck_license_img;
    }

    /**
     * 获取车牌号
     */
    public String getCarNumber() {
        return StringUtils.appendWithQuotationMarks(plate_no1, plate_no2, plate_no3);
    }

    /**
     * 获取车牌号，不要最开头的所在地
     */
    public String getCarNumberNoLocation() {
        return StringUtils.appendWithQuotationMarks(plate_no2, plate_no3);
    }

    /**
     * 获取车型车长
     */
    public String getTypeLength() {
        return StringUtils.appendWithSpace(truck_type_name, truck_length_name);
    }

    /**
     * 获取车牌颜色
     */
    public String getCarNumberColor() {
        // 1 蓝色 2 黄色
        if (plate_color.equals("1")) {
            return "蓝牌";
        } else {
            return "黄牌";
        }
    }

    /**
     * 获取车牌颜色Id
     */
    public String getCarNumberColorId(String plate_color) {
        // 1 蓝色 2 黄色
        if (plate_color.equals("蓝牌")) {
            return "1";
        } else {
            return "2";
        }
    }
}
