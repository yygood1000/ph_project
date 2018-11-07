package com.topjet.crediblenumber.goods.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.utils.ListUtils;
import com.topjet.crediblenumber.goods.modle.bean.SubscribeRouteListItem;
import com.topjet.crediblenumber.goods.modle.params.DeleteSubscribeRouteParams;
import com.topjet.crediblenumber.goods.modle.response.SubscribeRouteListResponse;
import com.topjet.crediblenumber.goods.modle.serverAPI.SubscribeRouteCommand;
import com.topjet.crediblenumber.goods.view.activity.SubscribeRouteView;

import java.util.ArrayList;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * 订阅路线
 */
public class SubscribeRoutePresenter extends BaseApiPresenter<SubscribeRouteView<SubscribeRouteListItem>,
        SubscribeRouteCommand> {
    // 是否处于正在编辑状态
    public boolean isEditing = false;

    public SubscribeRoutePresenter(SubscribeRouteView<SubscribeRouteListItem> mView, Context mContext,
                                   SubscribeRouteCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        mApiCommand.getSubscribeRouteList(new ObserverOnNextListener<SubscribeRouteListResponse>() {
            @Override
            public void onNext(SubscribeRouteListResponse response) {
                if (!ListUtils.isEmpty(response.getList()) && !isEditing) {
                    // 添加第一项全部路线
                    response.getList().add(0, new SubscribeRouteListItem(response.getSupply_of_goods_count()));
                }
                mView.loadSuccess(response.getList());
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });
    }

    /**
     * 删除订阅路线
     */
    public void deleteRoute(ArrayList<String> deleteRouteId) {
        if (!ListUtils.isEmpty(deleteRouteId)) {
            DeleteSubscribeRouteParams params = new DeleteSubscribeRouteParams(deleteRouteId);
            mApiCommand.deleteSubscribeRouteList(params, new ObserverOnResultListener<Object>() {
                @Override
                public void onResult(Object o) {
                    mView.showToast("删除成功");
                    mView.clearDeleteRouteIdsList();
                    refreshData();// 刷新列表
                }
            });
        }
    }
}
