package com.topjet.common.order_detail.modle.extra;

import com.topjet.common.base.model.BaseEvent;

/**
 * creator: yy
 * time:    2017/12/12
 * describe: 写一页点击同意按钮事件
 */

public class ClickAgreeBtnEvent extends BaseEvent {
    private String truckId;

    public ClickAgreeBtnEvent(String tag) {
        super.tag = tag;
    }

    public ClickAgreeBtnEvent(String tag, String truckId) {
        super.tag = tag;
        this.truckId = truckId;
    }

    public String getTruckId() {
        return truckId;
    }
}
