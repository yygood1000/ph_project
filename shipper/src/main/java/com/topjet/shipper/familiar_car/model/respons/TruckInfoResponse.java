package com.topjet.shipper.familiar_car.model.respons;

import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.common.order_detail.modle.bean.DriverInfo;
import com.topjet.common.utils.StringUtils;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/10/26
 * describe: 车辆详情
 */

public class TruckInfoResponse {
    private TruckInfo truck_info;	// 车辆信息	Object	结构见下表
    private String detail;	// 最后一次定位的详细地址	string
    private String longitude;	// 经度	string
    private String latitude;	// 纬度	string
    private String gps_update;	// 最后一次定位时间	string
    private String is_familiar_truck; // 是否是熟车	string	0:否 1:是
    private List<String> businesslinecentre_list;	//期望流向	List《String》
    private DriverInfo driver_info;	//司机信息	Object	结构见下表


    public boolean getIs_familiar_truck() {
        return StringUtils.isNotBlank(is_familiar_truck) && is_familiar_truck.equals("1");
    }

    public void setIs_familiar_truck(String is_familiar_truck) {
        this.is_familiar_truck = is_familiar_truck;
    }

    public TruckInfo getTruck_info() {
        return truck_info;
    }

    public void setTruck_info(TruckInfo truck_info) {
        this.truck_info = truck_info;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public String getGps_update() {
        return gps_update;
    }

    public void setGps_update(String gps_update) {
        this.gps_update = gps_update;
    }

    /**
     * 期望流向
     * @return
     */
    public String getBusinesslinecentre_list() {
        StringBuffer result = new StringBuffer();
        for (String s: businesslinecentre_list){
            result.append(s);
            if(!businesslinecentre_list.get(businesslinecentre_list.size()-1).equals(s)) {
                result.append("/");
            }
        }
        return result.toString();
    }

    public void setBusinesslinecentre_list(List<String> businesslinecentre_list) {
        this.businesslinecentre_list = businesslinecentre_list;
    }

    public DriverInfo getDriver_info() {
        return driver_info;
    }

    public void setDriver_info(DriverInfo driver_info) {
        this.driver_info = driver_info;
    }
}
