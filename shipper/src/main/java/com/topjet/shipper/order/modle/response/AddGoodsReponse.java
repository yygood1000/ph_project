package com.topjet.shipper.order.modle.response;

import com.topjet.common.base.model.BaseExtra;

/**
 * Created by tsj-004 on 2017/9/6.
 * 货主发布新货源后服务器返回结果
 */

public class AddGoodsReponse extends BaseExtra {
    // 货源ID
    private String goods_id;
    // 数据版本
    private String goods_version;
    // 订单ID
    private String order_id;
    // 数据版本
    private String order_version;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_version() {
        return goods_version;
    }

    public void setGoods_version(String goods_version) {
        this.goods_version = goods_version;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_version() {
        return order_version;
    }

    public void setOrder_version(String order_version) {
        this.order_version = order_version;
    }
}
