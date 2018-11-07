package com.topjet.common.order_detail.modle.state;

/**
 * creator: zhulunjun
 * time:    2017/10/19
 * describe: 退款状态
 * 1.发起方申请退款 2.发起方取消退款 3.接收方同意退款 4.接收方拒绝退款 5.退款失效
 */

public class RefundState {

    /**
     * 发起方申请退款
     */
    public static final int REFUND_START =	1;
    /**
     * 发起方取消退款
     */
    public static final int REFUND_CANCEL =	2;
    /**
     * 接收方同意退款
     */
    public static final int REFUND_AGREE =	3;
    /**
     * 接收方拒绝退款
     */
    public static final int REFUND_REJECT =	4;
    /**
     * 退款失效
     */
    public static final int REFUND_LOSE =	5;
}
