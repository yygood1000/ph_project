package com.topjet.common.order_detail.modle.response;

/**
 * creator: zhulunjun
 * time:    2017/9/11
 * describe: 确认承接 返回订单状态
 */

public class OrderStateResponse {

    private String order_status;	// 订单状态	是

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
