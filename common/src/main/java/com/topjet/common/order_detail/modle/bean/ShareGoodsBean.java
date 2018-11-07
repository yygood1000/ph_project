package com.topjet.common.order_detail.modle.bean;

/**
 * creator: zhulunjun
 * time:    2017/10/24
 * describe: 分享的货源
 */

public class ShareGoodsBean {

    private String goods_id;	// 货源ID	是	string
    private String depart_city;	//出发地文字	是	string
    private String destination_city;	//目的地文字	是	string
    private String load_date;	//提货时间	是	string
    private String the_goods;	//货物	是	string	（货物类型，货物数量、货物单位 拼接的总字符）
    private String tuck_length_type;	//车型车长拼接的总字符串	是	string
    private String is_freight_fee_managed;	//是否托管  1.运费托管 0.运费不托管

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
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

    public String getLoad_date() {
        return load_date;
    }

    public void setLoad_date(String load_date) {
        this.load_date = load_date;
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

    public String getIs_freight_fee_managed() {
        return is_freight_fee_managed;
    }

    public void setIs_freight_fee_managed(String is_freight_fee_managed) {
        this.is_freight_fee_managed = is_freight_fee_managed;
    }
}
