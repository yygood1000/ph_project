package com.topjet.shipper.order.view.fragment;


import com.topjet.common.base.view.activity.IListView;

/**
 * Created by yy on 2017/8/14.
 * 首页View
 */

public interface MyOrderView<D> extends IListView<D> {
    void setTvShareGoodsVisiable(int isVisiable);

    void showMarquee(String text);

    void changeOrderListTabLayout(int index);
}
