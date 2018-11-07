package com.topjet.crediblenumber.order.modle.params;

/**
 * Created by yy on 2017/8/9.
 * 司机-我的订单列表 请求参数
 */

public class MyOrderParams {

    private String status;//	订单状态	是	string	0:全部 1：新货源 2:待成交 3:待支付 4:承运中
    private String page;//	页数	是	string	初始为1

    public MyOrderParams(String status, String page) {
        this.status = status;
        this.page = page;
    }

    @Override
    public String toString() {
        return "MyOrderParams{" +
                "status='" + status + '\'' +
                ", page='" + page + '\'' +
                '}';
    }
}