package com.topjet.shipper.order.view.activity;

import com.topjet.common.base.view.activity.IListView;

/**
 * creator: zhulunjun
 * time:    2017/9/12
 * describe: 货主查看报价
 */

public interface ShowOfferView<D> extends IListView<D> {
    /**
     * 清空确认成交实体对象
     */
    void clearConfirmOfferItem();
}
