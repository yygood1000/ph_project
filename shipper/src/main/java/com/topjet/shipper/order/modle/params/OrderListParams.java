package com.topjet.shipper.order.modle.params;

/**
 * Created by yy on 2017/8/9.
 * 货主-我的订单列表 请求参数
 */

public class OrderListParams {

    private String status;//	订单状态	是	string	0:全部 1：新货源 2:待成交 3:待支付 4:承运中  历史订单状态  0 待评价 1 已评价  2 已失效
    private String page;//	页数	是	string	初始为1

    public OrderListParams(String status, String page) {
        this.status = status;
        this.page = page;
    }

    @Override
    public String toString() {
        return "OrderListParams{" +
                "status='" + status + '\'' +
                ", page='" + page + '\'' +
                '}';
    }
}