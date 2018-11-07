package com.topjet.common.order_detail.modle.bean;

import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/9/11
 * describe: 我的报价列表
 */

public class MyOfferList {
    private String pre_goods_id;    // 报价ID	是	String
    private String goods_id;    // 货源ID	是	String
    private String depart_city;    // 出发城市	是	String
    private String destination_city;    // 目的地城市	是	String
    private String goods_size;    // 货物数量	是	String
    private String truck_length_type;    // 车长 车型	是	String
    private String transport_fee;    // 运费	是	String
    private String deposit_fee;    // 定金	是	String
    private String update_time; //	修改时间	是	String
    private String distance;    // 当前与出发地的距离	是	String	单位是米，请自行转换
    private String the_total_distance;    // 总路程	是	String	单位是米，请自行转换
    private String pre_goods_version;

    private boolean check = false;// 是否选择

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getPre_goods_version() {
        return pre_goods_version;
    }

    public String getPre_goods_id() {
        return pre_goods_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getDepart_city() {
        return depart_city;
    }

    public String getDestination_city() {
        return destination_city;
    }

    public String getGoods_size() {
        return goods_size;
    }

    public String getTruck_length_type() {
        return truck_length_type;
    }

    public String getTransport_fee() {
        return transport_fee;
    }

    public String getDeposit_fee() {
        return deposit_fee;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    private String getDistance() {
        return distance + "公里";
    }

    public String getDisteanceInfo() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(distance)) {
            sb.append("距提货地").append(getDistance()).append(" ");
        }

        if (StringUtils.isNotBlank(the_total_distance)) {
            sb.append("全程").append(getThe_total_distance()).append(" ");
        }

        return sb.toString();
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    private String getThe_total_distance() {
        return the_total_distance + "公里";
    }

}
