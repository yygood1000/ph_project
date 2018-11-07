package com.topjet.common.order_detail.modle.bean;

import com.topjet.common.utils.StringUtils;

import java.io.Serializable;

/**
 * creator: zhulunjun
 * time:    2017/9/6
 * describe: 运费信息
 */

public class FreightInfo implements Serializable {
    private String freight_fee;    //运费金额	是	string
    private String freight_fee_status;    //运费状态	0.不托管 1.未托管 2.未支付 3.已支付  // 2.未支付 钱已经付给平台， 已支付表示钱已付给对方
    private String ahead_fee;    //提付费金额	是	string
    private String ahead_fee_status;    //提付费状态	是	string
    private String delivery_fee;    //到付费金额	是	string
    private String delivery_fee_status;    //到付费状态	是	string
    private String back_fee;    //回单付金额	是	string
    private String back_fee_status;    //回单付状态	是	string

    private String agency_fee; // 定金
    private String agency_status; // 定金状态 0.不托管 1.未托管 2.未支付 3.已支付  // 2.未支付 钱已经付给平台

    public double getAgency_fee() {
        return StringUtils.getDoubleToString(agency_fee);
    }

    public void setAgency_fee(String agency_fee) {
        this.agency_fee = agency_fee;
    }

    public int getAgency_status() {
        return StringUtils.isEmpty(agency_status) ? 0 : StringUtils.getIntToString(agency_status);
    }

    public void setAgency_status(String agency_status) {
        this.agency_status = agency_status;
    }

    public double getFreight_fee() {
        return StringUtils.isEmpty(freight_fee) ? 0 : StringUtils.getDoubleToString(freight_fee);
    }

    public void setFreight_fee(String freight_fee) {
        this.freight_fee = freight_fee;
    }

    public int getFreight_fee_status() {
        return StringUtils.getIntToString(freight_fee_status);
    }

    public void setFreight_fee_status(String freight_fee_status) {
        this.freight_fee_status = freight_fee_status;
    }

    public double getAhead_fee() {
        return StringUtils.getDoubleToString(ahead_fee);
    }

    public void setAhead_fee(String ahead_fee) {
        this.ahead_fee = ahead_fee;
    }

    public int getAhead_fee_status() {
        return StringUtils.getIntToString(ahead_fee_status);
    }

    public void setAhead_fee_status(String ahead_fee_status) {
        this.ahead_fee_status = ahead_fee_status;
    }

    public double getDelivery_fee() {
        return StringUtils.getDoubleToString(delivery_fee);
    }

    public void setDelivery_fee(String delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public int getDelivery_fee_status() {
        return StringUtils.getIntToString(delivery_fee_status);
    }

    public void setDelivery_fee_status(String delivery_fee_status) {
        this.delivery_fee_status = delivery_fee_status;
    }

    public double getBack_fee() {
        return StringUtils.getDoubleToString(back_fee);
    }

    public void setBack_fee(String back_fee) {
        this.back_fee = back_fee;
    }

    public int getBack_fee_status() {
        return StringUtils.getIntToString(back_fee_status);
    }

    public void setBack_fee_status(String back_fee_status) {
        this.back_fee_status = back_fee_status;
    }

    @Override
    public String toString() {
        return "FreightInfo{" +
                "freight_fee='" + freight_fee + '\'' +
                ", freight_fee_status='" + freight_fee_status + '\'' +
                ", ahead_fee='" + ahead_fee + '\'' +
                ", ahead_fee_status='" + ahead_fee_status + '\'' +
                ", delivery_fee='" + delivery_fee + '\'' +
                ", delivery_fee_status='" + delivery_fee_status + '\'' +
                ", back_fee='" + back_fee + '\'' +
                ", back_fee_status='" + back_fee_status + '\'' +
                ", agency_fee='" + agency_fee + '\'' +
                ", agency_status='" + agency_status + '\'' +
                '}';
    }
}
