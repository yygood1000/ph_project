package com.topjet.common.base.view.activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.order_detail.modle.bean.MyOfferList;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/9/27
 * describe: 有list的页面的接口类
 */

public interface IListView<D> extends IView {

    BaseQuickAdapter getAdapter();

    void loadData();

    void refresh();

    void loadSuccess(List<D> data);

    void loadFail(String errorMessage);

    void addData(List<D> data);

    void replaceData(List<D> data);
}
