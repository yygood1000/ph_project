package com.topjet.common.common.modle.params;

/**
 * creator: zhulunjun
 * time:    2017/10/17
 * describe:
 */

public class TruckParams {
    // 分页
    private String page;

    // 添加 删除熟车
    private String driver_truck_id;    //司机车辆id	是	string
    private String flag; // 添加删除标记	是	string	0:添加 1:删除
    public static final int ADD = 0;
    public static final int DELETE = 1;

    // 邀请熟车
    private String mobile;    // 邀请的司机手机号

    public TruckParams(String truck_id) {
        this.driver_truck_id = truck_id;
    }

    public TruckParams() {
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTruck_id() {
        return driver_truck_id;
    }

    public void setTruck_id(String truck_id) {
        this.driver_truck_id = truck_id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
