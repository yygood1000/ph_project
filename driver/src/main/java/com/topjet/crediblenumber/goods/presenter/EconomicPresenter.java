package com.topjet.crediblenumber.goods.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.base.view.activity.IListView;
import com.topjet.crediblenumber.goods.modle.bean.EconomicListData;
import com.topjet.crediblenumber.goods.modle.params.GetEconomicParams;
import com.topjet.crediblenumber.goods.modle.response.GetEconomicListResponse;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommand;

/**
 * Created by yy on 2017/11/17.
 * 货运经纪人
 */

public class EconomicPresenter extends BaseApiPresenter<IListView<EconomicListData>, GoodsCommand> {
    private GetEconomicParams params;

    public EconomicPresenter(IListView<EconomicListData> mView, Context mContext, GoodsCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    @Override
    public void onCreate() {
        params = (GetEconomicParams) mActivity.getIntentExtra(GetEconomicParams.getExtraName());
    }

    public void getEconomicList(int page) {
        params.setPage(page + "");
        mApiCommand.getEconomicList(params, new ObserverOnNextListener<GetEconomicListResponse>() {
            @Override
            public void onNext(GetEconomicListResponse getEconomicListResponse) {
                mView.loadSuccess(getEconomicListResponse.getEconomic_list());
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });
    }
}
