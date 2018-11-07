package com.topjet.shipper.deliver.presenter;

import android.content.Context;

import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.shipper.deliver.view.activity.SelectAddressMapView;

/**
 * Created by tsj-004 on 2017/8/29.
 * 拖选和输入 选择详细地址页
 * 货主版
 */

public class SelectAddressMapPresenter extends BasePresenter<SelectAddressMapView> {
    public SelectAddressMapPresenter(SelectAddressMapView mView, Context mContext) {
        super(mView, mContext);
    }
}