package com.topjet.shipper.order.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * creator: zhulunjun
 * time:    2017/10/19
 * describe: 订单详情传递到 查看报价页面的参数
 */

public class ShowOfferExtra extends BaseExtra {
    private String orderId;
    private String goodsId;
    private String goodsVersion;
    private boolean isAhead; // 是否部分提付费

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsVersion() {
        return goodsVersion;
    }

    public void setGoodsVersion(String goodsVersion) {
        this.goodsVersion = goodsVersion;
    }

    public boolean isAhead() {
        return isAhead;
    }

    public void setAhead(boolean ahead) {
        isAhead = ahead;
    }
}
