package com.topjet.common.order_detail.modle.bean;

import com.topjet.common.db.bean.IMUserBean;
import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/9/9
 * describe: 司机订单详情展示信息，在货主详情中
 *            拨打过电话的列表
 *            查看过的列表
 */

public class DriverInfo {
    private String driver_id;    // 司机id	是	string
    private String driver_truck_id;	// 车辆id	是	String   新增
    private String driver_name;    // 司机id	是	string
    private String truck_type;    // 车型	是	string
    private String truck_length;    // 车长	是	string
    private String clinch_a_deal_sum;    // 成交订单总数	是	string
    private String license_plate_number;    // 车牌号	是	string
    private String driver_icon_key;    // 司机头像key	是	string
    private String driver_mobile;    // 司机手机号码	是	string
    private String driver_comment_level;     // 承运好评度

    private String driver_icon_url; 	//司机头像url	是	string
    private String update_time; // 更新时间

    private String driver_longitude;	//  经度	是	string
    private String driver_latitude;	// 纬度	是	string
    private String driver_gps_detailed_address;	// gps详细地址	是	string
    private String driver_gps_update_time;	// gps更新时间	是	string

    public String getDriver_truck_id() {
        return driver_truck_id;
    }

    public void setDriver_truck_id(String driver_truck_id) {
        this.driver_truck_id = driver_truck_id;
    }

    public String getDriver_longitude() {
        return driver_longitude;
    }

    public void setDriver_longitude(String driver_longitude) {
        this.driver_longitude = driver_longitude;
    }

    public String getDriver_latitude() {
        return driver_latitude;
    }

    public void setDriver_latitude(String driver_latitude) {
        this.driver_latitude = driver_latitude;
    }

    public String getDriver_gps_detailed_address() {
        return driver_gps_detailed_address;
    }

    public void setDriver_gps_detailed_address(String driver_gps_detailed_address) {
        this.driver_gps_detailed_address = driver_gps_detailed_address;
    }

    public String getDriver_gps_update_time() {
        return driver_gps_update_time;
    }

    public void setDriver_gps_update_time(String driver_gps_update_time) {
        this.driver_gps_update_time = driver_gps_update_time;
    }

    public boolean getIsNull() {
        return StringUtils.isEmpty(driver_id) ||
                StringUtils.isEmpty(driver_name) ||
                StringUtils.isEmpty(truck_type) ||
                StringUtils.isEmpty(truck_length) ||
                StringUtils.isEmpty(clinch_a_deal_sum) ||
                StringUtils.isEmpty(license_plate_number) ||
                StringUtils.isEmpty(driver_icon_key) ||
                StringUtils.isEmpty(driver_icon_url) ||
                StringUtils.isEmpty(driver_mobile) ||
                StringUtils.isEmpty(driver_comment_level);
    }
    /**
     * 获取聊天用的用户信息, 跟司机聊
     */
    public IMUserBean getIMUserInfo(DriverInfo userInfo){
        IMUserBean userBean = new IMUserBean();
        userBean.setUsername(userInfo.getDriver_id());
        userBean.setUserId(userInfo.getDriver_id());
        userBean.setNick(userInfo.getDriver_name());
        userBean.setUserPhone(userInfo.getDriver_mobile());
        userBean.setAvatarKey(userInfo.getDriver_icon_key());
        userBean.setAvatar(userInfo.getDriver_icon_url());

        return userBean;
    }


    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }


    public String getTruck_type() {
        return truck_type;
    }

    public void setTruck_type(String truck_type) {
        this.truck_type = truck_type;
    }

    public String getTruck_length() {
        return truck_length;
    }

    public void setTruck_length(String truck_length) {
        this.truck_length = truck_length;
    }

    public String getClinch_a_deal_sum() {
        return StringUtils.isEmpty(clinch_a_deal_sum) ? "0" : clinch_a_deal_sum;
    }

    public void setClinch_a_deal_sum(String clinch_a_deal_sum) {
        this.clinch_a_deal_sum = clinch_a_deal_sum;
    }

    public String getLicense_plate_number() {
        return license_plate_number;
    }

    public void setLicense_plate_number(String license_plate_number) {
        this.license_plate_number = license_plate_number;
    }

    public String getDriver_icon_key() {
        return driver_icon_key;
    }

    public void setDriver_icon_key(String driver_icon_key) {
        this.driver_icon_key = driver_icon_key;
    }

    public String getDriver_mobile() {
        return driver_mobile;
    }

    public void setDriver_mobile(String driver_mobile) {
        this.driver_mobile = driver_mobile;
    }

    public float getDriver_comment_level() {
        return StringUtils.isBlank(driver_comment_level) ? 0 : Float.parseFloat(driver_comment_level);
    }

    public void setDriver_comment_level(String driver_comment_level) {
        this.driver_comment_level = driver_comment_level;
    }

    public String getDriver_icon_url() {
        return driver_icon_url;
    }

    public void setDriver_icon_url(String driver_icon_url) {
        this.driver_icon_url = driver_icon_url;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
