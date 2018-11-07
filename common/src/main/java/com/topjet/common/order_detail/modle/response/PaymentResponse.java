package com.topjet.common.order_detail.modle.response;

/**
 * creator: zhulunjun
 * time:    2017/9/11
 * describe: 支付响应类
 */

public class PaymentResponse {
    private String total_bill_no;	// 总账单号	是	String
    private String freight_fee;	// 总账单金额	是	String


    public String getTotal_bill_no() {
        return total_bill_no;
    }

    public void setTotal_bill_no(String total_bill_no) {
        this.total_bill_no = total_bill_no;
    }

    public String getFreight_fee() {
        return freight_fee;
    }

    public void setFreight_fee(String freight_fee) {
        this.freight_fee = freight_fee;
    }
}
