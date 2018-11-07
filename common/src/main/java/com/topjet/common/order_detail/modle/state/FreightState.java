package com.topjet.common.order_detail.modle.state;

/**
 * creator: zhulunjun
 * time:    2017/10/16
 * describe: 运费，订单，等金额的支付状态
 */

public class FreightState {

    // 0.不托管 1.未托管 2.未支付 3.已支付  // 2.未支付 钱已经付给平台， 已支付表示钱已付给对方

    /**
     * 不想托管
     */
    public static final int TRUSTEESHIP_NO = 0;

    /**
     * 想托管，但是没有给钱
     */
    public static final int TRUSTEESHIP_WAIT = 1;

    /**
     * 钱给平台了，平台还没有给用户。
     * 这个状态可以申请退款
     */
    public static final int PAY_ALREADY = 2;

    /**
     * 平台已经把钱给用户了
     */
    public static final int PAY_END = 3;

}
