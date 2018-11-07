package com.topjet.crediblenumber.goods.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.crediblenumber.goods.modle.extra.SubscribeRouteGoodsExtra;
import com.topjet.crediblenumber.goods.modle.params.SubscribeRouteGoodsParams;
import com.topjet.crediblenumber.goods.modle.params.UpdataGoodsQueryTimeParams;
import com.topjet.crediblenumber.goods.modle.response.SubscribeRouteGoodsListResponse;
import com.topjet.crediblenumber.goods.modle.serverAPI.SubscribeRouteCommand;
import com.topjet.crediblenumber.goods.view.activity.SubscribeRouteGoodsListView;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * 订阅路线 货源 列表
 */

public class SubscribeRouteGoodsListPresenter extends BaseApiPresenter<SubscribeRouteGoodsListView<GoodsListData>,
        SubscribeRouteCommand> {
    private boolean isAllRoute;// 是否是查阅全部订阅路线货源
    private String subscribeLineId;// 订阅路线ID

    public SubscribeRouteGoodsListPresenter(SubscribeRouteGoodsListView<GoodsListData> mView, Context mContext,
                                            SubscribeRouteCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SubscribeRouteGoodsExtra extra = (SubscribeRouteGoodsExtra) mActivity.getIntentExtra(SubscribeRouteGoodsExtra
                .getExtraName());
        isAllRoute = extra.isAllRoute();
        subscribeLineId = extra.getSubscribeLineId();

        // 设置标题栏
        if (isAllRoute) {
            mView.setTitleAllRoute();
        } else {
            mView.setTitleText(extra.getDepart(), extra.getDestination());
        }
    }

    /**
     * 加载数据
     */
    public void loadData(int page) {
        if (isAllRoute) {
            getAllSubscribeRouteGoodsList(page);
        } else {
            getSubscribeRouteGoodsList(page);
        }
    }


    /**
     * 获取 订阅路线 货源列表
     */
    private void getSubscribeRouteGoodsList(int page) {

        SubscribeRouteGoodsParams params = new SubscribeRouteGoodsParams(page + "", subscribeLineId);
        mApiCommand.getSubscribeRouteGoodsList(params, new ObserverOnResultListener<SubscribeRouteGoodsListResponse>() {
            @Override
            public void onResult(SubscribeRouteGoodsListResponse response) {
                mView.loadSuccess(response.getList());
            }
        });

    }

    /**
     * 获取 全部订阅路线 货源列表
     */
    private void getAllSubscribeRouteGoodsList(int page) {

        SubscribeRouteGoodsParams params = new SubscribeRouteGoodsParams(page + "");

        mApiCommand.getAllSubscribeRouteGoodsList(params, new
                ObserverOnResultListener<SubscribeRouteGoodsListResponse>() {
                    @Override
                    public void onResult(SubscribeRouteGoodsListResponse response) {
                        mView.loadSuccess(response.getList());
                    }
                });

    }


    /**
     * 修改订阅路线的货源查询时间
     */
    public void updataSubscribeLineQueryTime() {
        UpdataGoodsQueryTimeParams params;
        if (isAllRoute) {
            params = new UpdataGoodsQueryTimeParams("0");
        } else {
            params = new UpdataGoodsQueryTimeParams(subscribeLineId);
        }
        mApiCommand.updataSubscribeLineQueryTime(params, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {

            }
        });
    }
}
