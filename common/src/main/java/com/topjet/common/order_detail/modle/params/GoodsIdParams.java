package com.topjet.common.order_detail.modle.params;

import com.topjet.common.common.modle.bean.GpsInfo;
import com.topjet.common.config.CMemoryData;

/**
 * creator: zhulunjun
 * time:    2017/9/5
 * describe: 需要货源id作为参数的请求
 */

public class GoodsIdParams {

    private String goods_id;
    private String goods_version;    //货源版本号	是	string

    private String longitude;	// 经度	否	string
    private String latitude; //	纬度	否	string

    // 确认成交
    private String pre_goods_id;     // 报价单id	是	String	:-----
    private String pre_goods_version;    // 报价单version	是	String	:-----

    private String page;

    private UpdatePayTypeParams order_pay_style; // 需要修改支付方式的参数

    private GpsInfo order_gps_info; // 订单GPS信息

    public GoodsIdParams(String goods_id) {
        this.goods_id = goods_id;
    }

    public GoodsIdParams(String goods_id, String goods_version) {
        this.goods_id = goods_id;
        this.goods_version = goods_version;
    }

    public GoodsIdParams(String goods_id, String goods_version, String page) {
        this.goods_id = goods_id;
        this.goods_version = goods_version;
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

    public void setOrder_pay_style(UpdatePayTypeParams order_pay_style) {
        this.order_pay_style = order_pay_style;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setPre_goods_id(String pre_goods_id) {
        this.pre_goods_id = pre_goods_id;
    }

    public void setPre_goods_version(String pre_goods_version) {
        this.pre_goods_version = pre_goods_version;
    }

    public String getGoods_version() {
        return goods_version;
    }

    public void setGoods_version(String goods_version) {
        this.goods_version = goods_version;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    /**
     * 设置接口中的定位信息，直接从内存中获取
     */
    public void setGpsInfo() {
        if (CMemoryData.getLocationInfo() != null) {
            order_gps_info = new GpsInfo();
            this.order_gps_info.setGps_address_city_id(CMemoryData.getLocationInfo().getCity_id());
            this.order_gps_info.setGps_detail(CMemoryData.getLocationInfo().getDetail());
            this.order_gps_info.setGps_latitude(CMemoryData.getLocationInfo().getLatitude() + "");
            this.order_gps_info.setGps_longitude(CMemoryData.getLocationInfo().getLongitude() + "");
            this.order_gps_info.setGps_remark("接单");
        }
    }


}
