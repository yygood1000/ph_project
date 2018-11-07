package com.topjet.common.order_detail.modle.response;

import com.topjet.common.db.bean.IMUserBean;
import com.topjet.common.order_detail.modle.bean.DriverInfo;
import com.topjet.common.order_detail.modle.bean.FreightInfo;
import com.topjet.common.order_detail.modle.bean.ImGoodsInfo;
import com.topjet.common.order_detail.modle.bean.OrderUserInfo;
import com.topjet.common.order_detail.modle.bean.OwnerInfo;
import com.topjet.common.order_detail.modle.bean.RefundInfo;
import com.topjet.common.utils.StringUtils;


/**
 * 订单详情服务端返回数据类
 * Created by tsj004 on 2017/8/30.
 */

public class OrderInfoResponse {
    private String order_id;
    private String order_version;
    private String goods_status = ""; // 货源状态
    private String order_no;
    private String order_status;
    private String transport_fee;
    private String remark;
    private String remark_img_key;
    private String remark_img_url;
    private String other_remark;
    private String is_freight_fee_managed; // 是否托管 0 : 否 1 :是
    private String is_assigned; // 是否定向指派 0 : 否 1 :是
    private String pickup_code;
    private String unload_code;
    private OrderUserInfo sender_info;
    private OrderUserInfo receiver_info;

    // 货主
    private String goods_id;    //货源id	是	string
    private String goods_version; // 货源版本
    private DriverInfo driver_info; // 司机信息
    private String update_time;    //订单修改时间	是	string
    private FreightInfo freight;    //运费信息	是	Object	结构见下表
    private String pay_style;	// 支付方式	是	string	1: 货到付款 2: 提付全款 3:提付部分运费 4:回单付运费'
    private String is_familiar_truck;    //	是否熟车 	0:否 1:是

    // 司机
    private String goods_no;
    private OwnerInfo owner_info; // 货主信息

    // 退款
    private RefundInfo refund_info;

    private ImGoodsInfo im_goods_info; // IM用货源信息



    /**
     * 获取聊天用的用户信息, 跟司机聊
     */
    public IMUserBean getIMUserInfo(DriverInfo userInfo){
        return userInfo.getIMUserInfo(userInfo);
    }

    /**
     * 获取聊天用的用户信息, 跟货主聊
     */
    public IMUserBean getIMUserInfo(OwnerInfo userInfo){


        return userInfo.getIMUserInfo(userInfo);
    }
    public boolean getIs_familiar_truck() {
        return StringUtils.isNotBlank(is_familiar_truck) && is_familiar_truck.equals("1");
    }

    public ImGoodsInfo getIm_goods_info() {
        return im_goods_info;
    }

    public void setIm_goods_info(ImGoodsInfo im_goods_info) {
        this.im_goods_info = im_goods_info;
    }

    public void setIs_familiar_truck(String is_familiar_truck) {
        this.is_familiar_truck = is_familiar_truck;
    }

    public String getPay_style() {
        return pay_style;
    }

    public void setPay_style(String pay_style) {
        this.pay_style = pay_style;
    }

    public String getGoods_status() {
        return goods_status;
    }

    public void setGoods_status(String goods_status) {
        this.goods_status = goods_status;
    }

    public String getOrder_version() {
        return order_version;
    }

    public OrderUserInfo getSender_info() {
        return sender_info;
    }

    public void setSender_info(OrderUserInfo sender_info) {
        this.sender_info = sender_info;
    }

    public OrderUserInfo getReceiver_info() {
        return receiver_info;
    }

    public void setReceiver_info(OrderUserInfo receiver_info) {
        this.receiver_info = receiver_info;
    }

    public void setOrder_version(String order_version) {
        this.order_version = order_version;
    }

    public String getGoods_version() {
        return goods_version;
    }

    public void setGoods_version(String goods_version) {
        this.goods_version = goods_version;
    }

    public OwnerInfo getOwner_info() {
        return owner_info;
    }

    public void setOwner_info(OwnerInfo owner_info) {
        this.owner_info = owner_info;
    }

    public DriverInfo getDriver_info() {
        return driver_info;
    }

    public void setDriver_info(DriverInfo driver_info) {
        this.driver_info = driver_info;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getOrder_status() {
        return StringUtils.getIntToString(order_status);
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getTransport_fee() {
        return transport_fee;
    }

    public void setTransport_fee(String transport_fee) {
        this.transport_fee = transport_fee;
    }

    public double getAgency_fee() {
        return freight.getAgency_fee();
    }

    public void setAgency_fee(String agency_fee) {
        freight.setAgency_fee(agency_fee);
    }

    public int getAgency_status() {
        return freight.getAgency_status();

    }

    public void setAgency_status(String agency_status) {
        freight.setAgency_status(agency_status);
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark_img_key() {
        return remark_img_key;
    }

    public void setRemark_img_key(String remark_img_key) {
        this.remark_img_key = remark_img_key;
    }

    public String getRemark_img_url() {
        return remark_img_url;
    }

    public void setRemark_img_url(String remark_img_url) {
        this.remark_img_url = remark_img_url;
    }

    public String getOther_remark() {
        return other_remark;
    }

    public void setOther_remark(String other_remark) {
        this.other_remark = other_remark;
    }

    public String getIs_freight_fee_managed() {
        return is_freight_fee_managed;
    }

    public void setIs_freight_fee_managed(String is_freight_fee_managed) {
        this.is_freight_fee_managed = is_freight_fee_managed;
    }

    public String getIs_assigned() {
        return is_assigned;
    }

    public void setIs_assigned(String is_assigned) {
        this.is_assigned = is_assigned;
    }

    public String getPickup_code() {
        return pickup_code;
    }

    public void setPickup_code(String pickup_code) {
        this.pickup_code = pickup_code;
    }

    public String getUnload_code() {
        return unload_code;
    }

    public void setUnload_code(String unload_code) {
        this.unload_code = unload_code;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }


    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public FreightInfo getFreight() {
        return freight;
    }

    public void setFreight(FreightInfo freight) {
        this.freight = freight;
    }

    public String getGoods_no() {
        return goods_no;
    }

    public void setGoods_no(String goods_no) {
        this.goods_no = goods_no;
    }

    public RefundInfo getRefund_info() {
        return refund_info == null ? new RefundInfo() : refund_info;
    }

    public void setRefund_info(RefundInfo refund_info) {
        this.refund_info = refund_info;
    }
}
