package com.topjet.common.order_detail.modle.bean;

/**
 * creator: zhulunjun
 * time:    2017/12/11
 * describe: 发送订单消息的封装类
 */

public class ImGoodsInfo {

    private String sender_city;	// 出发地城市	是	string
    private String receiver_city;	// 目的地城市	是	string
    private String quantity_type;	// 货物类型	是	string
    private String quantity_max;	// 货物数量	是	string
    private String truck_type;	// 车型	是	string
    private String truck_length;	// 车长	是	string

    public String getSender_city() {
        return sender_city;
    }

    public void setSender_city(String sender_city) {
        this.sender_city = sender_city;
    }

    public String getReceiver_city() {
        return receiver_city;
    }

    public void setReceiver_city(String receiver_city) {
        this.receiver_city = receiver_city;
    }

    public String getQuantity_type() {
        return quantity_type;
    }

    public void setQuantity_type(String quantity_type) {
        this.quantity_type = quantity_type;
    }

    public String getQuantity_max() {
        return quantity_max;
    }

    public void setQuantity_max(String quantity_max) {
        this.quantity_max = quantity_max;
    }

    public String getTruck_type() {
        return truck_type;
    }

    public void setTruck_type(String truck_type) {
        this.truck_type = truck_type;
    }

    public String getTruck_length() {
        return truck_length;
    }

    public void setTruck_length(String truck_length) {
        this.truck_length = truck_length;
    }
}
