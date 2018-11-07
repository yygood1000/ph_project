package com.topjet.common.adv.modle.params;

/**
 * Created by yy on 2017/11/13.
 * <p>
 * 跑马灯广告
 */

public class MarqueeParams {
    public static String TYPE_SHIPPER = "1";
    public static String TYPE_DIRVER = "2";
    private String type;//	请求页面类型 	1.货主版，我的订单\历史订单 2.司机版，找货页和附近货源页

    @Override
    public String toString() {
        return "MarqueeParams{" +
                "type='" + type + '\'' +
                '}';
    }

    public MarqueeParams(String type) {
        this.type = type;
    }
}
