package com.topjet.common.common.modle.bean;

import com.amap.api.maps.model.LatLng;
import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.db.bean.IMUserBean;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.cluster.ClusterItem;

import java.util.ArrayList;

/**
 * creator: zhulunjun
 * time:    2017/10/12
 * describe: 车辆信息
 */

public class TruckInfo extends BaseExtra implements ClusterItem {
    private String driver_truck_id;    //司机车辆id	string
    private String truck_icon_key;    //车头照key	string
    private String truck_icon_url;    //车头照url	string
    private String plate_no1;    //车牌照1级	string
    private String plate_no2;    //车牌照2级	string
    private String plate_no3;    //车牌照3级	string
    private String plate_color;    //车牌颜色	string	1 蓝色 2 黄色
    private String truck_type_name;    //车型name	string
    private String truck_length_name;    //车长name	string
    private String driver_name;    //司机姓名	string
    private String driver_mobile;    //司机手机号	string
    private String driver_order_count;    //车辆成交笔数	string
    private String gps_address;    //gps地址	string
    private String gps_longitude;    //gps精度	string
    private String gps_latitude;    //gps维度	string
    private String gps_update_time;    //gps更新时间
    private String truck_business_line;    //常跑城市
    private String is_familiar_truck;    //	是否熟车 	0:否 1:是

    // 附近车源地图 特有字段
    private String driver_id;    //司机id
    private String longitude;    //经度
    private String latitude;    //	纬度

    // 车辆详情
    private String truck_license_key;    //车辆行驶证照key	string
    private String truck_license_url;    //车辆行驶证照url	string
    private String audit_status;    //认证状态	string	0 无需认证 1 未认证 2 已认证 3 认证中 4 认证失败
    public static final String AUTHENTICATED = "2"; // 已认证
    private String truck_type_id;    //车型id	string
    private String truck_length_id;    //车长id	string
    private String truck_status;    //车辆状态	string	1 空车/求货中,2 休息
    private String driver_truck_version;    //司机

    // 货主车辆详情
    private String truck_hread_image_key;    //车头照key	string
    private String truck_hread_image_url;    //车头照url	string
    private String truck_plate;    //车牌号	string
    private String truck_type;    //车型	string
    private String truck_length;    //车长	string

    private String driver_head_key;    // 司机头像key	string
    private String driver_head_url;    // 司机头像url	string

    /**
     * 获取聊天对象
     */
    public IMUserBean getUserBean(TruckInfo info) {
        IMUserBean userBean = new IMUserBean();
        userBean.setUserPhone(info.getDriver_mobile());
        userBean.setUserId(info.getDriver_id());
        userBean.setUsername(info.getDriver_id());
        userBean.setNick(info.getDriver_name());
        userBean.setAvatarKey(info.getDriver_head_key());
        userBean.setAvatar(info.getDriver_head_url());
        return userBean;
    }

    /**
     * 地图集合实现接口，返回该点经纬度对象
     */
    @Override
    public LatLng getPosition() {
        if (StringUtils.isNotBlank(latitude) || StringUtils.isNotBlank(longitude)) {
            return new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        }
        return new LatLng(0, 0);
    }

    public String getDriver_head_key() {
        return driver_head_key;
    }

    public void setDriver_head_key(String driver_head_key) {
        this.driver_head_key = driver_head_key;
    }

    public String getDriver_head_url() {
        return driver_head_url;
    }

    public void setDriver_head_url(String driver_head_url) {
        this.driver_head_url = driver_head_url;
    }

    public String getTruck_hread_image_key() {
        return truck_hread_image_key;
    }

    public String getTruck_hread_image_url() {
        return truck_hread_image_url;
    }

    public String getTruck_plate() {
        return truck_plate;
    }

    public String getTruck_type() {
        return truck_type;
    }

    public String getTruck_length() {
        return truck_length;
    }

    /**
     * 获取车牌号
     */
    public String getCarNumber() {
        return StringUtils.appendWithQuotationMarks(plate_no1, plate_no2, plate_no3);
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

    public String getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(String audit_status) {
        this.audit_status = audit_status;
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

    public String getTruck_status() {
        return truck_status;
    }

    public void setTruck_status(String truck_status) {
        this.truck_status = truck_status;
    }

    public String getDriver_truck_version() {
        return driver_truck_version;
    }

    public void setDriver_truck_version(String driver_truck_version) {
        this.driver_truck_version = driver_truck_version;
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

    public String getDriver_order_count() {
        return driver_order_count;
    }

    public void setDriver_order_count(String driver_order_count) {
        this.driver_order_count = driver_order_count;
    }

    public String getGps_address() {
        return gps_address;
    }

    public void setGps_address(String gps_address) {
        this.gps_address = gps_address;
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

    public String getGps_update_time() {
        return gps_update_time;
    }

    public void setGps_latitude(String gps_latitude) {
        this.gps_latitude = gps_latitude;
    }

    public String getTruck_business_line() {
        return truck_business_line;
    }

    public void setTruck_business_line(String truck_business_line) {
        this.truck_business_line = truck_business_line;
    }

    public static ArrayList<TruckInfo> testData(int num) {
        ArrayList<TruckInfo> result = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            result.add(new TruckInfo());
        }
        return result;
    }

    public boolean getIsFamiliarTruck() {
        if (StringUtils.isBlank(is_familiar_truck)) {// 熟车列表
            return true;
        }

        return !is_familiar_truck.equals("0");
    }

    public String getPlateNo() {
        return StringUtils.appendWithSpace(getPlate_no1(), getPlate_no2(), getPlate_no3());
    }

    public void setIs_practice_the_car(String is_practice_the_car) {
        this.is_familiar_truck = is_practice_the_car;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
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

    public String getTruck_id() {
        return driver_truck_id;
    }

    public void setTruck_id(String truck_id) {
        this.driver_truck_id = truck_id;
    }

    @Override
    public String toString() {
        return "TruckInfo{" +
                "driver_truck_id='" + driver_truck_id + '\'' +
                ", truck_icon_key='" + truck_icon_key + '\'' +
                ", truck_icon_url='" + truck_icon_url + '\'' +
                ", plate_no1='" + plate_no1 + '\'' +
                ", plate_no2='" + plate_no2 + '\'' +
                ", plate_no3='" + plate_no3 + '\'' +
                ", plate_color='" + plate_color + '\'' +
                ", truck_type_name='" + truck_type_name + '\'' +
                ", truck_length_name='" + truck_length_name + '\'' +
                ", driver_name='" + driver_name + '\'' +
                ", driver_mobile='" + driver_mobile + '\'' +
                ", driver_order_count='" + driver_order_count + '\'' +
                ", gps_address='" + gps_address + '\'' +
                ", gps_longitude='" + gps_longitude + '\'' +
                ", gps_latitude='" + gps_latitude + '\'' +
                ", gps_update_time='" + gps_update_time + '\'' +
                ", truck_business_line='" + truck_business_line + '\'' +
                ", is_practice_the_car='" + is_familiar_truck + '\'' +
                ", driver_id='" + driver_id + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", truck_license_key='" + truck_license_key + '\'' +
                ", truck_license_url='" + truck_license_url + '\'' +
                ", audit_status='" + audit_status + '\'' +
                ", truck_type_id='" + truck_type_id + '\'' +
                ", truck_length_id='" + truck_length_id + '\'' +
                ", truck_status='" + truck_status + '\'' +
                ", driver_truck_version='" + driver_truck_version + '\'' +
                ", truck_hread_image_key='" + truck_hread_image_key + '\'' +
                ", truck_hread_image_url='" + truck_hread_image_url + '\'' +
                ", truck_plate='" + truck_plate + '\'' +
                ", truck_type='" + truck_type + '\'' +
                ", truck_length='" + truck_length + '\'' +
                '}';
    }


}
