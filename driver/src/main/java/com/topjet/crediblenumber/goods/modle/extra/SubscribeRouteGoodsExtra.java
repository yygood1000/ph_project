package com.topjet.crediblenumber.goods.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * Created by yy on 2017/9/9.
 * 跳转订阅路线 货源列表页面Extra
 */

public class SubscribeRouteGoodsExtra extends BaseExtra {
    private boolean isAllRoute;// 是否是全部路线
    private String depart;// 起点
    private String destination;// 终点
    private String subscribeLineId;// 订阅路线ID

    @Override
    public String toString() {
        return "SubscribeRouteGoodsExtra{" +
                "isAllRoute=" + isAllRoute +
                ", depart='" + depart + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }

    public SubscribeRouteGoodsExtra(boolean isAllRoute, String depart, String destination) {
        this(isAllRoute, depart, destination, "");
    }

    public SubscribeRouteGoodsExtra(boolean isAllRoute, String depart, String destination, String subscribeLineId) {
        this.isAllRoute = isAllRoute;
        this.depart = depart;
        this.destination = destination;
        this.subscribeLineId = subscribeLineId;
    }

    public boolean isAllRoute() {
        return isAllRoute;
    }

    public String getDepart() {
        return depart;
    }

    public String getDestination() {
        return destination;
    }

    public String getSubscribeLineId() {
        return subscribeLineId;
    }
}
