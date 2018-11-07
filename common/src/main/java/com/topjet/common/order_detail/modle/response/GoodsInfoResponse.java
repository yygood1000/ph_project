package com.topjet.common.order_detail.modle.response;


import com.topjet.common.db.bean.IMUserBean;
import com.topjet.common.order_detail.modle.bean.DriverInfo;
import com.topjet.common.order_detail.modle.bean.FreightInfo;
import com.topjet.common.order_detail.modle.bean.ImGoodsInfo;
import com.topjet.common.order_detail.modle.bean.ImUserInfo;
import com.topjet.common.order_detail.modle.bean.OrderUserInfo;
import com.topjet.common.order_detail.modle.bean.OwnerInfo;
import com.topjet.common.order_detail.modle.bean.RefundInfo;
import com.topjet.common.utils.StringUtils;

/**
 * 货源详情服务端返回数据类
 * 司机，货主
 * Created by tsj004 on 2017/8/30.
 */

public class GoodsInfoResponse {
    // 货主
    private String goods_id;
    private String goods_version;
    private String goods_status = ""; // 货源状态
    private String order_no;
    private String remark;
    private String remark_img_key;
    private String remark_img_url;
    private String other_remark;
    private String goods_push_num;
    private String offer_sum;
    private String looked_sum;
    private String called_sum;
    private String update_time;
    private OrderUserInfo sender_info;
    private OrderUserInfo receiver_info;
    private String distance;	// 距离	是	string	如果没有传司机定位的经纬度，则不会返回
    private String the_total_distance;	// 总距离	是	string

    private String pay_style;	// 支付方式	是	string	1: 货到付款 2: 提付全款 3:提付部分运费 4:回单付运费'
    //司机
    private OwnerInfo owner_info; // 货主信息
    private String goods_no;
    private String pre_goods_id;	//报价ID	是	string
    private String is_offer;	//是否报价	是	string	0：未报价 1：已报价
    private String transport_fee;	//运费/报价	是	string
    private String deposit_fee;	//定金	是	string
    private String pre_goods_version;	//报价version	是	string

    private ImGoodsInfo im_goods_info;   // IM用货源信息
    private ImUserInfo im_user_info; // IM用户信息

    // 退款
    private RefundInfo refund_info;
    // 运费
    private FreightInfo freight;
    // 订单信息
    private OrderInfoResponse order_info;

    /**
     * 获取聊天用的用户信息, 跟司机聊
     */
    public IMUserBean getIMUserInfo(DriverInfo userInfo){
        IMUserBean userBean = new IMUserBean();
        userBean.setUsername(userInfo.getDriver_id());
        userBean.setUserId(userInfo.getDriver_id());
        userBean.setNick(userInfo.getDriver_name());
        userBean.setUserPhone(userInfo.getDriver_mobile());
        userBean.setAvatarKey(userInfo.getDriver_icon_key());
        userBean.setAvatar(userInfo.getDriver_icon_url());

        return userBean;
    }

    /**
     * 获取聊天用的用户信息, 跟货主聊
     */
    public IMUserBean getIMUserInfo(OwnerInfo userInfo, ImUserInfo imUserInfo){
        IMUserBean userBean = new IMUserBean();
        userBean.setUsername(userInfo.getOwner_id());
        userBean.setUserPhone(userInfo.getOwner_mobile());
        userBean.setUserId(userInfo.getOwner_id());

        userBean.setNick(imUserInfo.getUser_name());
        userBean.setAvatarKey(imUserInfo.getIcon_key());
        userBean.setAvatar(imUserInfo.getIcon_url());


        return userBean;
    }

    public ImUserInfo getIm_user_info() {
        return im_user_info;
    }

    public void setIm_user_info(ImUserInfo im_user_info) {
        this.im_user_info = im_user_info;
    }

    public OrderInfoResponse getOrder_info() {
        return order_info;
    }

    public void setOrder_info(OrderInfoResponse order_info) {
        this.order_info = order_info;
    }

    public FreightInfo getFreight() {
        return freight;
    }

    public void setFreight(FreightInfo freight) {
        this.freight = freight;
    }

    public RefundInfo getRefund_info() {
        return refund_info;
    }

    public void setRefund_info(RefundInfo refund_info) {
        this.refund_info = refund_info;
    }

    public ImGoodsInfo getIm_goods_info() {
        return im_goods_info;
    }

    public void setIm_goods_info(ImGoodsInfo im_goods_info) {
        this.im_goods_info = im_goods_info;
    }

    public String getPay_style() {
        return pay_style;
    }

    public void setPay_style(String pay_style) {
        this.pay_style = pay_style;
    }

    public int getGoods_status() {
        return StringUtils.getIntToString(goods_status);
    }

    public void setGoods_status(String goods_status) {
        this.goods_status = goods_status;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getThe_total_distance() {
        return the_total_distance;
    }

    public void setThe_total_distance(String the_total_distance) {
        this.the_total_distance = the_total_distance;
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

    public String getPre_goods_id() {
        return pre_goods_id;
    }

    public void setPre_goods_id(String pre_goods_id) {
        this.pre_goods_id = pre_goods_id;
    }

    public String getIs_offer() {
        return is_offer;
    }

    public void setIs_offer(String is_offer) {
        this.is_offer = is_offer;
    }

    public double getTransport_fee() {
        return StringUtils.getDoubleToString(transport_fee);
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

    public String getPre_goods_version() {
        return pre_goods_version;
    }

    public void setPre_goods_version(String pre_goods_version) {
        this.pre_goods_version = pre_goods_version;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
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

    public String getGoods_push_num() {
        return goods_push_num;
    }

    public void setGoods_push_num(String goods_push_num) {
        this.goods_push_num = goods_push_num;
    }

    public String getOffer_sum() {
        return offer_sum;
    }

    public void setOffer_sum(String offer_sum) {
        this.offer_sum = offer_sum;
    }

    public String getLooked_sum() {
        return looked_sum;
    }

    public void setLooked_sum(String looked_sum) {
        this.looked_sum = looked_sum;
    }

    public String getCalled_sum() {
        return called_sum;
    }

    public void setCalled_sum(String called_sum) {
        this.called_sum = called_sum;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getGoods_no() {
        return goods_no;
    }

    public void setGoods_no(String goods_no) {
        this.goods_no = goods_no;
    }




}
