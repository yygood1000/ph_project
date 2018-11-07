package com.topjet.common.order_detail.modle.bean;

import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/9/11
 * describe: 查看报价列表实体
 */

public class ShowOfferList {

    private String pre_order_id;    // 报价单id	 
    private String pre_order_version;    // 报价单version	 
    private String goods_id;    // 货源id	 
    private String order_id;    // 订单id	 
    private String transport_fee;    // 报价	 
    private String driver_id;    // 司机id	 
    private String driver_icon_key;    // 司机头像key	 
    private String driver_icon_url;    // 司机头像url	 
    private String driver_comment_level;    // 司机好评度	 
    private String driver_name; // 司机姓名	 
    private String plate_no;    // 司机车牌号	 
    private String driver_truck_type;    // 司机车型	 
    private String driver_truck_length;    // 司机车长	 
    private String driver_mobile;    // 司机手机号	 
    private String driver_id_card;    // 司机身份证号	 
    private String driver_dealorder_count;    // 司机成交笔数	 
    private String driver_gps_detail;    // 司机GPS详细地址	 
    private String pre_order_create_time;    // 报价时间
    private String driver_truck_id; // 车辆id
    private String pay_style;// 支付方式	是	string	1: 货到付款 2: 提付全款 3:提付部分运费 4:回单付运费'

    public String getDriver_truck_id() {
        return driver_truck_id;
    }

    public void setDriver_truck_id(String driver_truck_id) {
        this.driver_truck_id = driver_truck_id;
    }

    private String pre_status;    //报价状态	是	String	1:待货主成交 2: 货主已确认成交 3:司机撤销报价 4:订单已修改

    public String getPre_status() {
        return pre_status;
    }

    public float getDriver_comment_level() {
        return StringUtils.isBlank(driver_comment_level) ? 0 : Float.parseFloat(driver_comment_level);
    }

    public String getPre_order_id() {
        return pre_order_id;
    }


    public String getPre_order_version() {
        return pre_order_version;
    }


    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTransport_fee() {
        return transport_fee;
    }


    public String getDriver_id() {
        return driver_id;
    }


    public String getDriver_icon_key() {
        return driver_icon_key;
    }


    public String getDriver_icon_url() {
        return driver_icon_url;
    }

    public String getDriver_name() {
        return driver_name;
    }


    public String getPlate_no() {
        return plate_no;
    }


    public String getDriver_truck_type() {
        return driver_truck_type;
    }


    public String getDriver_truck_length() {
        return driver_truck_length;
    }


    public String getDriver_mobile() {
        return driver_mobile;
    }


    public String getDriver_dealorder_count() {
        return driver_dealorder_count;
    }

    public String getDriver_gps_detail() {
        return driver_gps_detail;
    }

    public String getDriverIdCard() {
        return driver_id_card;
    }

    public String getPay_style() {
        return pay_style;
    }
}
