package com.topjet.common.order_detail.modle.state;

/**
 * creator: zhulunjun
 * time:    2017/9/6
 * describe: 订单状态类
 *
 * 货源状态等于2，判断订单状态
 */

public class OrderState {
    /**
     * 货主取消交易.该状态订单不会出现在我的订单列表中
     */
    public static final int ORDER_CANCEL = 1;
    /**
     * 货主已确认成交
     */
    public static final int ORDER_CONFIRM = 2;
    /**
     * 待支付定金.该状态订单不会出现在我的订单列表中
     */
    public static final int ORDER_UNPAID_DEPOSIT = 3;
    /**
     * 待支付运费
     */
    public static final int ORDER_UNPAID_FREIGHT = 4;
    /**
     *  提货中
     */
    public static final int ORDER_PICK_UP_GOODS = 5;
    /**
     * 承运中
     */
    public static final int ORDER_DELIVERING = 6;
    /**
     * 已承运/待评价
     */
    public static final int ORDER_STATUS_IS_THE_CARRIER = 7;

    /**
     * 货主已评司机未评价
     */
    public static final int ORDER_STATUS_DRIVERUNEVALUATE =	8;
    /**
     * 司机已评货主未评价
     */
    public static final int ORDER_STATUS_OWNERUNEVALUATE =	9;
    /**
     * 双方已评价
     */
    public static final int ORDER_STATUS_EVALUATE =	10;
    /**
     * 订单已退款
     */
    public static final int ORDER_STATUS_REFUND =	11;


}
