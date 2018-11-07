package com.topjet.crediblenumber.goods.modle.response;

import com.topjet.crediblenumber.goods.modle.bean.SubscribeRouteListItem;

import java.util.ArrayList;

/**
 * Created by yy on 2017/8/29.
 * <p>
 * 订阅路线 列表数据 返回体
 */

public class SubscribeRouteListResponse {
    private String supply_of_goods_count;
    private ArrayList<SubscribeRouteListItem> list;

    public SubscribeRouteListResponse(String supply_of_goods_count, ArrayList<SubscribeRouteListItem> list) {
        this.supply_of_goods_count = supply_of_goods_count;
        this.list = list;
    }

    public String getSupply_of_goods_count() {
        return supply_of_goods_count;
    }

    public void setSupply_of_goods_count(String supply_of_goods_count) {
        this.supply_of_goods_count = supply_of_goods_count;
    }

    public ArrayList<SubscribeRouteListItem> getList() {
        return list;
    }

    public void setList(ArrayList<SubscribeRouteListItem> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "SubscribeRouteListResponse{" +
                "supply_of_goods_count='" + supply_of_goods_count + '\'' +
                ", list=" + list +
                '}';
    }
}
