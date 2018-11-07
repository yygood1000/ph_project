package com.topjet.crediblenumber.car.modle.params;

/**
 * Created by tsj-004 on 2017/10/16.
 * 车辆id
 */

public class CarIDParams {
    private String driver_truck_id = null;// 司机车辆id
    private String driver_truck_version = null; // 司机车辆version
    private String truck_status = null;// 车辆状态  1 求货,2 休息

    public CarIDParams() {

    }

    public CarIDParams(String id) {
        this.driver_truck_id = id;
    }

    public CarIDParams(String id, String version) {
        this.driver_truck_id = id;
        this.driver_truck_version = version;
    }

    public String getDriver_truck_id() {
        return driver_truck_id;
    }

    public void setDriver_truck_id(String driver_truck_id) {
        this.driver_truck_id = driver_truck_id;
    }

    public String getDriver_truck_version() {
        return driver_truck_version;
    }

    public void setDriver_truck_version(String driver_truck_version) {
        this.driver_truck_version = driver_truck_version;
    }

    public String getTruck_status() {
        return truck_status;
    }

    public void setTruck_status(String truck_status) {
        this.truck_status = truck_status;
    }
}
