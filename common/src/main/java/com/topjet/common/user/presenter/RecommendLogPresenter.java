package com.topjet.common.user.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.user.modle.bean.ReferrerInfoBean;
import com.topjet.common.user.modle.params.GetRecommendListParams;
import com.topjet.common.user.modle.response.RecommendListResponse;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.view.activity.RecommendLogView;

/**
 * Created by yy on 2017/11/6.
 * <p>
 * 推荐记录
 */

public class RecommendLogPresenter extends BaseApiPresenter<RecommendLogView<ReferrerInfoBean>, UserCommand> {
    public RecommendLogPresenter(RecommendLogView mView, Context mContext, UserCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    public void getRecommendList(String timeRange, String status, int page) {
        GetRecommendListParams params = new GetRecommendListParams(timeRange, status, page + "");
        mApiCommand.getRecommendlist(params, new ObserverOnNextListener<RecommendListResponse>() {
            @Override
            public void onNext(RecommendListResponse recommendListResponse) {
                mView.loadSuccess(recommendListResponse.getList());
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });
    }
}
