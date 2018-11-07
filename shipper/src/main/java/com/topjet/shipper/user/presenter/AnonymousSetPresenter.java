package com.topjet.shipper.user.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.user.modle.response.GetAnonymousResponse;
import com.topjet.shipper.user.model.params.SettingAnonymousParams;
import com.topjet.shipper.user.model.serviceAPI.ShipperCommand;
import com.topjet.shipper.user.view.activity.AnonymousSetView;

/**
 * 匿名设置
 * Created by tsj004 on 2017/8/28.
 */

public class AnonymousSetPresenter extends BaseApiPresenter<AnonymousSetView, ShipperCommand> {

    public AnonymousSetPresenter(AnonymousSetView mView, Context mContext, ShipperCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 设置匿名开关
     */
    public void settingAnonymous(String isAnonymous) {
        mApiCommand.settingAnonymous(new SettingAnonymousParams(isAnonymous), new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                mView.setImage();
            }
        });
    }

    /**
     * 获取匿名状态
     */
    public void getAnonymous() {
        mApiCommand.getAnonymous(new ObserverOnResultListener<GetAnonymousResponse>() {
            @Override
            public void onResult(GetAnonymousResponse response) {
                mView.setIsAnonymous(response.getIs_anonymous());
                mView.setCheckBoxListener();
                mView.setImage();
            }
        });
    }
}
