package com.topjet.common.order_detail.modle.event;

import com.topjet.common.base.model.BaseEvent;

/**
 * creator: zhulunjun
 * time:    2017/11/8
 * describe: 关闭订单详情页面
 */

public class CancelOrderDetailEvent extends BaseEvent {
    private boolean isCancel;

    public CancelOrderDetailEvent(boolean isCancel) {
        this.isCancel = isCancel;
    }

    public boolean isCancel() {
        return isCancel;
    }
}
