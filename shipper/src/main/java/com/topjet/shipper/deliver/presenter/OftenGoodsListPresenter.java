package com.topjet.shipper.deliver.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.base.view.activity.IListView;
import com.topjet.common.user.modle.params.PageNumParams;
import com.topjet.shipper.deliver.modle.bean.OftenGoodsListItem;
import com.topjet.shipper.deliver.modle.serverAPI.DeliverCommand;
import com.topjet.shipper.order.modle.response.OftenGoodsListReponse;

/**
 * 常发货源列表
 * Created by tsj004 on 2017/8/29.
 */

public class OftenGoodsListPresenter extends BaseApiPresenter<IListView<OftenGoodsListItem>, DeliverCommand> {

    public OftenGoodsListPresenter(IListView<OftenGoodsListItem> mView, Context mContext, DeliverCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 货主-常发货源列表
     */
    public void oftenGoodsList(int page) {
        mApiCommand.oftenGoodsList(new PageNumParams(page + ""), new ObserverOnNextListener<OftenGoodsListReponse>
                () {
            @Override
            public void onNext(OftenGoodsListReponse reponse) {
                mView.loadSuccess(reponse.getList());
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }

        });
    }
}
