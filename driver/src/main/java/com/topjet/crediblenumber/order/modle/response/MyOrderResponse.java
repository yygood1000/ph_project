package com.topjet.crediblenumber.order.modle.response;


import com.topjet.common.order_detail.modle.bean.MyOrderListItem;

import java.util.ArrayList;

/**
 * Created by yy on 2017/8/30.
 * <p>
 * 司机版 我的订单列表 返回集合
 */

public class MyOrderResponse {
    private ArrayList<MyOrderListItem> list;

    public ArrayList<MyOrderListItem> getList() {
        return list;

    }

    public MyOrderResponse() {
    }

    @Override
    public String toString() {
        return "MyOrderResponse{" +
                "list=" + list +
                '}';
    }
}
