package com.topjet.crediblenumber.user.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.user.modle.response.UserCenterRealNameAuthenticationResponse;
import com.topjet.common.user.modle.serverAPI.UserCertificationCommandAPI;
import com.topjet.crediblenumber.user.modle.params.SaveTruckParams;

/**
 * Created by tsj-004 on 2017/7/25.
 * 认证
 */

public class AuthenticationCommand extends BaseCommand<AuthenticationCommandAPI> {

    public AuthenticationCommand(Class<AuthenticationCommandAPI> c, MvpActivity mActivity) {
        super(c, mActivity);
    }

    /**
     * 车辆认证司机版
     */
    public void savetruck(SaveTruckParams params, ObserverOnResultListener listener) {
        mCommonParams = getParams(AuthenticationCommandAPI.SAVE_TRUCK, params);
        handleOnResultObserver(mApiService.savetruck(mCommonParams), listener);
    }

    /**
     * 获取车辆认证状态
     */
    public void queryCarAuthenStatus(ObserverOnResultListener<SaveTruckParams> listener) {
        mCommonParams = getParams(AuthenticationCommandAPI.QUERY_CAR_AUTHEN_STATUS);
        handleOnResultObserver(mApiService.queryCarAuthenStatus(mCommonParams), listener);
    }
}
