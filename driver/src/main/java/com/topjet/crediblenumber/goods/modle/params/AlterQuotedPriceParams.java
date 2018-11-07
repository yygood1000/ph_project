package com.topjet.crediblenumber.goods.modle.params;

/**
 * Created by yy on 2017/9/9.
 * <p>
 * 司机-修改报价
 */

public class AlterQuotedPriceParams {

    private String transport_fee;//报价金额
    private String deposit_fee;//	定金	是
    private String pre_goods_id;//	报价单id
    private String pre_goods_version;//	报价单版本

    public AlterQuotedPriceParams(String transport_fee, String deposit_fee, String pre_goods_id, String
            pre_goods_version) {
        this.transport_fee = transport_fee;
        this.deposit_fee = deposit_fee;
        this.pre_goods_id = pre_goods_id;
        this.pre_goods_version = pre_goods_version;
    }

    @Override
    public String toString() {
        return "AlterQuotedPriceParams{" +
                "transport_fee='" + transport_fee + '\'' +
                ", deposit_fee='" + deposit_fee + '\'' +
                ", pre_goods_id='" + pre_goods_id + '\'' +
                ", pre_goods_version='" + pre_goods_version + '\'' +
                '}';
    }

    public void setPre_goods_version(String pre_goods_version) {
        this.pre_goods_version = pre_goods_version;
    }

    public void setPre_goods_id(String pre_goods_id) {
        this.pre_goods_id = pre_goods_id;
    }

    public void setTransport_fee(String transport_fee) {
        this.transport_fee = transport_fee;
    }

    public void setDeposit_fee(String deposit_fee) {
        this.deposit_fee = deposit_fee;
    }

}
