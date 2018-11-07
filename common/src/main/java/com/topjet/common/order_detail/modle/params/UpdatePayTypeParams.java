package com.topjet.common.order_detail.modle.params;

import com.topjet.common.base.model.BaseExtra;

/**
 * creator: zhulunjun
 * time:    2017/11/9
 * describe: 修改支付方式的参数
 */

public class UpdatePayTypeParams extends BaseExtra {
    private String order_id;    // 订单id	是	string
    private String pay_style;    // 支付方式	是	string
    private String ahead_fee;    // 提付费	是	string
    private String ahead_fee_is_managed;    // 提付费是否托管	是	string	0:不托管 1:托管
    private String delivery_fee;    // 到付费	是	string
    private String delivery_fee_is_managed;     // 到付费是否托管	是	string
    private String back_fee;    // 回单付费用	是	string
    private String order_version;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPay_style() {
        return pay_style;
    }

    public void setPay_style(String pay_style) {
        this.pay_style = pay_style;
    }

    public String getAhead_fee() {
        return ahead_fee;
    }

    public void setAhead_fee(String ahead_fee) {
        this.ahead_fee = ahead_fee;
    }

    public String getAhead_fee_is_managed() {
        return ahead_fee_is_managed;
    }

    public void setAhead_fee_is_managed(String ahead_fee_is_managed) {
        this.ahead_fee_is_managed = ahead_fee_is_managed;
    }

    public String getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(String delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public String getDelivery_fee_is_managed() {
        return delivery_fee_is_managed;
    }

    public void setDelivery_fee_is_managed(String delivery_fee_is_managed) {
        this.delivery_fee_is_managed = delivery_fee_is_managed;
    }

    public String getBack_fee() {
        return back_fee;
    }

    public void setBack_fee(String back_fee) {
        this.back_fee = back_fee;
    }

    public String getOrder_version() {
        return order_version;
    }

    public void setOrder_version(String order_version) {
        this.order_version = order_version;
    }
}
