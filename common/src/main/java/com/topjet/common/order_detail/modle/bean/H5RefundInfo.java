package com.topjet.common.order_detail.modle.bean;

import com.topjet.common.base.model.BaseExtra;

/**
 * Created by yy on 2017/10/16.
 * <p>
 * H5 退款页面传入的参数实体类
 * 该类由订单详情页面构造后传到退款页面。再由退关页面提供H5 调用获取
 */

public class H5RefundInfo extends BaseExtra {
    private String order_id;//是	订单ID(orderInfo 表中的ID)
    private String order_status;//是	订单状态)
    private String version;//	是	订单详情中的版本
    private String trigger_id;//是	退款发起人ID ,货主或者司机用户表对应的ID
    private FreightInfo freight;//	是	运费信息	Object	订单详情的freight类
    private RefundInfo refund_info;//		是	退款单信息	Object	结构见下表

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setTrigger_id(String trigger_id) {
        this.trigger_id = trigger_id;
    }

    public void setFreight(FreightInfo freight) {
        this.freight = freight;
    }

    public void setRefund_info(RefundInfo refund_info) {
        this.refund_info = refund_info;
    }

    @Override
    public String toString() {
        return "H5RefundInfo{" +
                "order_id='" + order_id + '\'' +
                ", order_status='" + order_status + '\'' +
                ", version='" + version + '\'' +
                ", trigger_id='" + trigger_id + '\'' +
                ", freight=" + freight +
                ", refund_info=" + refund_info +
                '}';
    }
}
