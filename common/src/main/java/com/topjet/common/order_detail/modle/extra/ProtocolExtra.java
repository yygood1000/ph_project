package com.topjet.common.order_detail.modle.extra;

import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.order_detail.modle.params.UpdatePayTypeParams;

/**
 * Created by yy on 2017/9/22.
 * <p>
 * 跳转交易协议页面 参数
 */

public class ProtocolExtra extends BaseExtra {
    public static final int TYPE_CHECK_PROTOCOL = 0;// 查看合同
    public static final int TYPE_CONFIRM_HAS_NOT_DIALOG = 1;// 确认成交，无支付明细弹窗，货主版
    public static final int TYPE_CONFIRM_HAS_DIALOG = 2;// 确认成交，有支付明细弹窗，货主版
    public static final int TYPE_ALTER_PAY_TYPE = 3;// 修改支付方式，货主版
    public static final int TYPE_ACCEPT_ORDER = 4;// 我要承接，司机版
    public static final int TYPE_DELIVER_GOODS = 5;// 发货->跳交易协议，货主版

    private String tag;//跳转页面的标识

    private int pageType;// 进入页面的标识
    private String goods_id;

    // 业务请求使用的字段，根据需求传入的
    private String preGoodsId; // 报价单id
    private String preGoodsVersion; // 报价单version

    private String freight_fee;//运费总金额
//    private String ahead_fee;//提付费金额
//    private String delivery_fee;//到付费金额
//    private String back_fee;//回单付金额

    private UpdatePayTypeParams payTypeParams; // 修改支付方式需要的参数

    private String driver_name;//司机姓名	否	string
    private String driver_mobile;//司机手机号码	是	string
    private String driver_id_card;//司机身份证号	否	string
    private String driver_license_plate_number;// 车牌号	是	string

    private String truck_type;//接单车型	是	String
    private String truck_length;//接单车长	是	String


    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    public UpdatePayTypeParams getPayTypeParams() {
        return payTypeParams;
    }

    public void setPayTypeParams(UpdatePayTypeParams payTypeParams) {
        this.payTypeParams = payTypeParams;
    }

    public void setPreGoodsId(String preGoodsId) {
        this.preGoodsId = preGoodsId;
    }


    public void setPreGoodsVersion(String preGoodsVersion) {
        this.preGoodsVersion = preGoodsVersion;
    }

    public int getPageType() {
        return pageType;
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getFreight_fee() {
        return freight_fee;
    }

    public void setFreight_fee(String freight_fee) {
        this.freight_fee = freight_fee;
    }


    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_mobile() {
        return driver_mobile;
    }

    public void setDriver_mobile(String driver_mobile) {
        this.driver_mobile = driver_mobile;
    }

    public String getDriver_id_card() {
        return driver_id_card;
    }

    public void setDriver_id_card(String driver_id_card) {
        this.driver_id_card = driver_id_card;
    }

    public String getDriver_license_plate_number() {
        return driver_license_plate_number;
    }

    public void setDriver_license_plate_number(String driver_license_plate_number) {
        this.driver_license_plate_number = driver_license_plate_number;
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


    @Override
    public String toString() {
        return "ProtocolExtra{" +
                ", pageType=" + pageType +
                ", goods_id='" + goods_id + '\'' +
                ", preGoodsId='" + preGoodsId + '\'' +
                ", preGoodsVersion='" + preGoodsVersion + '\'' +
                ", freight_fee='" + freight_fee + '\'' +
                ", payTypeParams=" + payTypeParams +
                ", driver_name='" + driver_name + '\'' +
                ", driver_mobile='" + driver_mobile + '\'' +
                ", driver_id_card='" + driver_id_card + '\'' +
                ", driver_license_plate_number='" + driver_license_plate_number + '\'' +
                ", truck_type='" + truck_type + '\'' +
                ", truck_length='" + truck_length + '\'' +
                '}';
    }
}
