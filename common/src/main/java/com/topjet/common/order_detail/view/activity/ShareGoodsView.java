package com.topjet.common.order_detail.view.activity;

import com.topjet.common.base.view.activity.IListView;

/**
 * creator: zhulunjun
 * time:    2017/10/26
 * describe: 分享货源
 */

public interface ShareGoodsView<D> extends IListView<D> {
    void shareImage(String path);
}
