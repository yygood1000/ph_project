package com.topjet.shipper.order.modle.response;


import com.topjet.common.order_detail.modle.bean.MyOrderListItem;

import java.util.ArrayList;

/**
 * Created by yy on 2017/8/30.
 * <p>
 * 货主版 我的订单列表 返回集合
 */

public class MyOrderResponse {
    private ArrayList<MyOrderListItem> owner_my_order_response_list;

    public ArrayList<MyOrderListItem> getList() {
        return owner_my_order_response_list;
    }

    public MyOrderResponse() {
    }

    @Override
    public String toString() {
        return "MyOrderResponse{" +
                "owner_my_order_response_list=" + owner_my_order_response_list +
                '}';
    }
}
