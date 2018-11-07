package com.topjet.common.order_detail.modle.state;

/**
 * creator: zhulunjun
 * time:    2017/10/18
 * describe: 货源状态
 *
 * GOODS_STATUS_NEW	1	1:新建货源
 GOODS_STATUS_CREATE_ORDER	2	2:生成订单
 GOODS_STATUS_CANCEL	3	3: 货主撤销
 GOODS_STATUS_DRIVER_ABANDON	4	4: 司机放弃订单
 GOODS_STATUS_TIMEOUT	5	5: 货源过期
 GOODS_STATUS_REFUND	6	6: 订单退款
 */

public class GoodsState {

    /**
     * 新建货源
     */
    public static final int GOODS_STATUS_NEW = 1;
    /**
     * 生成订单
     */
    public static final int GOODS_STATUS_CREATE_ORDER = 2;
    /**
     * 货主撤销
     */
    public static final int GOODS_STATUS_CANCEL = 3;
    /**
     * 司机放弃订单
     */
    public static final int GOODS_STATUS_DRIVER_ABANDON = 4;
    /**
     * 货源过期
     */
    public static final int GOODS_STATUS_TIMEOUT = 5;
    /**
     * 订单退款
     */
    public static final int GOODS_STATUS_REFUND = 6;
}
