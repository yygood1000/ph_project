package com.topjet.crediblenumber.order.view.fragment;

import com.topjet.common.base.view.activity.IListView;

/**
 * Created by yy on 2017/11/27.
 * 司机版我的订单
 */

public interface MyOrderFragmentView<D> extends IListView<D> {
    void shareImage(String path);

    void changePage(int index);

    void showGif();
}
