package com.topjet.common.order_detail.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * Created by yy on 2017/11/7.
 * 跳转我要投诉页面传递参数
 */

public class ComplainExtra extends BaseExtra {
    private String orderId;// 投诉相关订单订单号
    private String version;// 投诉相关订单版本号

    public ComplainExtra(String orderId, String version) {
        this.orderId = orderId;
        this.version = version;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "ComplainExtra{" +
                "orderId='" + orderId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
