package com.topjet.crediblenumber.user.modle.bean;

import com.topjet.common.order_detail.modle.bean.OwnerInfo;
import com.topjet.common.utils.StringUtils;

/**
 * Created by tsj-004 on 2017/11/13.
 *
 * 通话记录
 */

public class CallLogData {
    private String depart_city = null;//出发地
    private String destination_city = null;//目的地
    private String the_goods = null;//货物
    private String tuck_length_type = null;//车型车长拼接的总字符串
    private String goods_id = null;//货源id
    private String goods_version = null;//货源version
    private String goods_status = null;//货源状态
    private String call_time = null;//通话时长
    private String create_time = null;//拨打电话时间
    private OwnerInfo owner_info = null;//货主信息
    private String is_call;//	是否对此货源拨打过电话	0:没打过 1：打过
    private String is_examine;//	是否查看过货源		0：没看过 1：看过

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

    public String getThe_goods() {
        return the_goods;
    }

    public void setThe_goods(String the_goods) {
        this.the_goods = the_goods;
    }

    public String getTuck_length_type() {
        return tuck_length_type;
    }

    public void setTuck_length_type(String tuck_length_type) {
        this.tuck_length_type = tuck_length_type;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_version() {
        return goods_version;
    }

    public void setGoods_version(String goods_version) {
        this.goods_version = goods_version;
    }

    public String getGoods_status() {
        return goods_status;
    }

    public void setGoods_status(String goods_status) {
        this.goods_status = goods_status;
    }

    public String getCall_time() {
        return call_time;
    }

    public void setCall_time(String call_time) {
        this.call_time = call_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public OwnerInfo getOwner_info() {
        return owner_info;
    }

    public void setOwner_info(OwnerInfo owner_info) {
        this.owner_info = owner_info;
    }

    public void setIs_call(String is_call) {
        this.is_call = is_call;
    }

    public boolean getIs_call() {
        return StringUtils.isNotBlank(is_call) && is_call.equals("1");
    }

    public boolean getIs_examine() {
        return StringUtils.isNotBlank(is_examine) && is_examine.equals("1");
    }

    public void setIs_examine(String is_examine) {
        this.is_examine = is_examine;
    }
}