package com.topjet.shipper.familiar_car.model.extra;

import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/10/19
 * describe: 传递车辆id
 */

public class TruckExtra extends BaseExtra {

    private String truckId;
    private String longitude;	// 经度	string
    private String latitude;	// 纬度	string
    private String truck_plate;	//车牌号	string
    private String address;// 车辆位置
    private String time; // 位置最后更新时间
    private String mobile;// 司机电话
    private String name;// 司机姓名
    // 是否显示 右上角车辆详情
    // 只有从订单列表司机位置进入需要 显示车辆详情
    private boolean isShowMenu = false;


    public TruckExtra() {
    }


    public TruckExtra(String truckId) {
        this.truckId = truckId;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isShowMenu() {
        return isShowMenu;
    }

    public void setShowMenu(boolean showMenu) {
        isShowMenu = showMenu;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public String getTruckId() {
        return truckId;
    }

    public double getLongitude() {
        return StringUtils.getDoubleToString(longitude);
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return StringUtils.getDoubleToString(latitude);
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getTruck_plate() {
        return truck_plate;
    }

    public void setTruck_plate(String truck_plate) {
        this.truck_plate = truck_plate;
    }
}
