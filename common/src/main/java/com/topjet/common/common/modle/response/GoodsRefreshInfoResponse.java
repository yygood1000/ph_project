package com.topjet.common.common.modle.response;

/**
 * Created by tsj-004 on 2017/12/18.
 * 货主-获取货源刷新信息   返回类
 */

public class GoodsRefreshInfoResponse {
    private String refresh_minute;  //	刷新分钟数	是	string
    private String refresh_count;   //	刷新次数	是	string

    public String getRefresh_minute() {
        return refresh_minute;
    }

    public void setRefresh_minute(String refresh_minute) {
        this.refresh_minute = refresh_minute;
    }

    public String getRefresh_count() {
        return refresh_count;
    }

    public void setRefresh_count(String refresh_count) {
        this.refresh_count = refresh_count;
    }
}