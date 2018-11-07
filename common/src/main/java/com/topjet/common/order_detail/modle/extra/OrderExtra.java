package com.topjet.common.order_detail.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * creator: zhulunjun
 * time:    2017/11/17
 * describe: 订单详情参数封装
 */

public class OrderExtra extends BaseExtra {

    private String orderId; // 订单id
    private int dataSource; // 是货源，还是订单
    private String showPickUp = "0"; // 是否显示提货码弹窗

    public static final String SHOW = "1"; // 显示

    public static final int ORDER_DETAIL = 1; // 订单详情
    public static final int GOODS_DETAIL = 2; // 货源详情


    public OrderExtra(String orderId, int dataSource) {
        this.orderId = orderId;
        this.dataSource = dataSource;
    }

    public OrderExtra(String orderId, int dataSource, String showPickUp) {
        this.orderId = orderId;
        this.dataSource = dataSource;
        this.showPickUp = showPickUp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getDataSource() {
        return dataSource;
    }

    public void setDataSource(int dataSource) {
        this.dataSource = dataSource;
    }

    public String getShowPickUp() {
        return showPickUp;
    }

    public void setShowPickUp(String showPickUp) {
        this.showPickUp = showPickUp;
    }
}
