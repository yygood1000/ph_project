package com.topjet.crediblenumber.goods.view.fragment;

import com.topjet.common.base.view.activity.IListView;

/**
 * Created by yy on 2017/7/29.
 * 智能找货
 */

public interface SmartSearchGoodsView<D> extends IListView<D> {
    void onPermissionFail();

    void setDepart(String depart);

    void setGoodsCount(String goods_count);

    void insertAdvInfoToList();

    void showMarquee(String text);

//    void addEconomicView();
}
