package com.topjet.common.user.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.user.modle.bean.ReferrerInfoBean;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.view.activity.ReferrerView;

/**
 * 修改密码 录入新密码页面
 * Created by yy on 2017/8/14.
 */

public class ReferrerPresenter extends BaseApiPresenter<ReferrerView, UserCommand> {

    public ReferrerPresenter(ReferrerView mView, Context mContext, UserCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 获取我的推荐人信息
     */
    public void getReferrerInfo() {
        mApiCommand.getReferrerInfo(new ObserverOnResultListener<ReferrerInfoBean>() {
            @Override
            public void onResult(ReferrerInfoBean referrerInfoBean) {
                mView.showRefrererInfo(referrerInfoBean);
            }
        });
    }

}
