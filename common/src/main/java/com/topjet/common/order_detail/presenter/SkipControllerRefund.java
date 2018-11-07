package com.topjet.common.order_detail.presenter;

import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.order_detail.modle.bean.FreightInfo;
import com.topjet.common.order_detail.modle.bean.H5RefundInfo;
import com.topjet.common.order_detail.modle.bean.RefundInfo;
import com.topjet.common.order_detail.view.activity.RefundActivity;

/**
 * Created by yy on 2017/10/16.
 * 退款跳转方法
 */

public class SkipControllerRefund {
    /**
     * 跳转退款详情页面
     */
    public static void skipToRefund(MvpActivity mActivity, String orderId, String orderStatus, String version,
                                    String userId, FreightInfo freight, RefundInfo refundInfo) {
        H5RefundInfo extra = new H5RefundInfo();
        extra.setOrder_id(orderId);
        extra.setOrder_status(orderStatus);
        extra.setVersion(version);
        extra.setTrigger_id(userId);
        extra.setFreight(freight);
        extra.setRefund_info(refundInfo);

        mActivity.turnToActivity(RefundActivity.class, extra);
    }
}
