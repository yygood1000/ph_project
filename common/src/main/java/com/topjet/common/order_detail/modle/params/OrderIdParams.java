package com.topjet.common.order_detail.modle.params;

import com.topjet.common.common.modle.bean.GpsInfo;
import com.topjet.common.config.CMemoryData;

/**
 * creator: zhulunjun
 * time:    2017/9/5
 * describe: 需要订单id作为参数的请求
 */

public class OrderIdParams {

    private String order_id;
    private String order_version;    // 订单version	是	String

    // 取消交易
    private String pre_goods_id;     // 报价单id	是	String	:-----
    private String pre_goods_version;    // 报价单version	是	String	:-----

    //确认提货
    private String pickup_code;    //提货码	是	String
    // 确认签收
    private String unload_code;    //卸货码	是	String
    private String pay_password;    // 支付密码(需MD5加密后传递)	是	String	:-----

    public OrderIdParams(String order_id) {
        this.order_id = order_id;
    }

    public OrderIdParams(String order_id, String order_version) {
        this.order_id = order_id;
        this.order_version = order_version;
    }

    public OrderIdParams(String order_id, String order_version, String pay_password) {
        this.order_id = order_id;
        this.order_version = order_version;
        this.pay_password = pay_password;
    }

    private GpsInfo order_gps_info; // 订单GPS信息

    public void setUnload_code(String unload_code) {
        this.unload_code = unload_code;
    }


    public void setPickup_code(String pickup_code) {
        this.pickup_code = pickup_code;
    }


    public void setPre_goods_id(String pre_goods_id) {
        this.pre_goods_id = pre_goods_id;
    }


    public void setPre_goods_version(String pre_goods_version) {
        this.pre_goods_version = pre_goods_version;
    }

    public String getOrder_version() {
        return order_version;
    }


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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
