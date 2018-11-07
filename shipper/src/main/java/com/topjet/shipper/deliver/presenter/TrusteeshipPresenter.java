package com.topjet.shipper.deliver.presenter;

import android.content.Context;

import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.shipper.deliver.modle.params.OwnerGoodsParams;
import com.topjet.shipper.deliver.view.activity.TrusteeshipView;
import com.topjet.shipper.deliver.controller.OwnerGoodsController;

/**
 * Created by tsj-004 on 2017/8/22.
 * 是否托管运费
 */

public class TrusteeshipPresenter extends BasePresenter<TrusteeshipView> {
    public TrusteeshipPresenter(TrusteeshipView mView, Context mContext) {
        super(mView, mContext);
    }

    /**
     * 发货
     */
    public void netWork2AddGoods(String tag, OwnerGoodsParams params) {
        new OwnerGoodsController(mActivity).addGoods(params, tag);
    }
}