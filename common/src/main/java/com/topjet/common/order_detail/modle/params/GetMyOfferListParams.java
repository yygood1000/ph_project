package com.topjet.common.order_detail.modle.params;

/**
 * creator: zhulunjun
 * time:    2017/9/11
 * describe: 查看我的报价列表
 */

public class GetMyOfferListParams {
    private String longitude;	// 经度	是	String
    private String latitude; // 纬度	是	String
    private String page;	// 页码	是	String	初始值为1

    public GetMyOfferListParams(String longitude, String latitude, String page) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.page = page;
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
