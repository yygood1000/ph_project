package com.topjet.common.common.modle.bean;

/**
 * creator: zhulunjun
 * time:    2017/11/13
 * describe: 诚信查询，司机查货主，显示的订单列表
 */

public class UserInfoGoods {

    private String goods_id;    // 货源ID	String
    private String pre_goods_version;    // 报价version	String
    private String pre_goods_id;    // 报价ID	String
    private String depart_city;    // 出发城市	String
    private String destination_city;    // 目的地城市	String
    private String goods_size;    // 货物数量	String
    private String truck_length_type;    // 车长 车型	String
    private String transport_fee;    // 运费	String
    private String deposit_fee;    // 定金	String
    private String create_time; // 货源发布时间

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getPre_goods_version() {
        return pre_goods_version;
    }

    public void setPre_goods_version(String pre_goods_version) {
        this.pre_goods_version = pre_goods_version;
    }

    public String getPre_goods_id() {
        return pre_goods_id;
    }

    public void setPre_goods_id(String pre_goods_id) {
        this.pre_goods_id = pre_goods_id;
    }

    public String getDepart_city() {
        return depart_city;
    }

    public void setDepart_city(String depart_city) {
        this.depart_city = depart_city;
    }

    public String getDestination_city() {
        return destination_city;
    }

    public void setDestination_city(String destination_city) {
        this.destination_city = destination_city;
    }

    public String getGoods_size() {
        return goods_size;
    }

    public void setGoods_size(String goods_size) {
        this.goods_size = goods_size;
    }

    public String getTruck_length_type() {
        return truck_length_type;
    }

    public void setTruck_length_type(String truck_length_type) {
        this.truck_length_type = truck_length_type;
    }

    public String getTransport_fee() {
        return transport_fee;
    }

    public void setTransport_fee(String transport_fee) {
        this.transport_fee = transport_fee;
    }

    public String getDeposit_fee() {
        return deposit_fee;
    }

    public void setDeposit_fee(String deposit_fee) {
        this.deposit_fee = deposit_fee;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
