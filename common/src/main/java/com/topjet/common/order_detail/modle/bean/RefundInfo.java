package com.topjet.common.order_detail.modle.bean;

import com.topjet.common.utils.StringUtils;

import java.io.Serializable;

/**
 * Created by yy on 2017/10/16.
 * <p>
 * 退款信息
 */

public class RefundInfo implements Serializable {
    private String refund_id;//退款id	是	string
    private String refund_version;//退款版本号	是	string
    private String is_oneself_refund;//是否自身退款	是	string	0是 1否
    private String cs_refund_status;//	客服退款状态	是	string	1.发起方申请客服介入 2.接收方申请客服介入 3.客服同意退款 4.客服关闭退款
    private String refund_status;//退款单状态	是	string	1.发起方申请退款 2.发起方取消退款 3.接收方同意退款 4.接收方拒绝退款 5.退款失效
    private String refund_money;// 退款金额

    @Override
    public String toString() {
        return "RefundInfo{" +
                "refund_id='" + refund_id + '\'' +
                ", refund_version='" + refund_version + '\'' +
                ", is_oneself_refund='" + is_oneself_refund + '\'' +
                ", cs_refund_status='" + cs_refund_status + '\'' +
                ", refund_status='" + refund_status + '\'' +
                '}';
    }

    public String getRefund_money() {
        return refund_money;
    }

    public String getRefund_id() {
        return refund_id;
    }

    public String getRefund_version() {
        return refund_version;
    }

    public String getIs_oneself_refund() {
        return is_oneself_refund;
    }

    public String getCs_refund_status() {
        return cs_refund_status;
    }

    public int getRefund_status() {
        return StringUtils.getIntToString(refund_status);
    }
}
