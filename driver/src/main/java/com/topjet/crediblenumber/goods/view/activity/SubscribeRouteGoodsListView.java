package com.topjet.crediblenumber.goods.view.activity;

import com.topjet.common.base.view.activity.IListView;

/**
 * Created by yy on 2017/7/29.
 * 订阅路线
 */

public interface SubscribeRouteGoodsListView<D> extends IListView<D> {

    void setTitleAllRoute();

    void setTitleText(String depart, String destination);

}
