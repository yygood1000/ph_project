package com.topjet.common.user.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.user.modle.params.RealNameAuthenticationParams;
import com.topjet.common.user.modle.params.TypeAuthDriverParams;
import com.topjet.common.user.modle.params.TypeAuthOwnerParams;
import com.topjet.common.user.modle.response.RealNameAuthenticationResponse;
import com.topjet.common.user.modle.response.UserCenterRealNameAuthenticationResponse;

/**
 * Created by tsj-004 on 2017/7/25.
 * 认证相关
 */

public class UserCertificationCommand extends BaseCommand<UserCertificationCommandAPI>{

    public UserCertificationCommand(Class c, MvpActivity activity) {
        super(c, activity);
    }

    /**
     * 实名认证，个人中心
     */
    public void userCentreAuth(RealNameAuthenticationParams params, ObserverOnResultListener<RealNameAuthenticationResponse> listener) {
        mCommonParams = getParams(UserCertificationCommandAPI.USER_CENTRE_AUTH, params);
        handleOnResultObserver(mApiService.userCentreAuth(mCommonParams),listener);
    }

    /**
     * 获取实名认证状态
     */
    public void queryRealNameAuthenStatus(ObserverOnResultListener<UserCenterRealNameAuthenticationResponse> listener) {
        mCommonParams = getParams(UserCertificationCommandAPI.QUERY_REAL_NAME_AUTHEN_STATUS);
        handleOnResultObserver(mApiService.queryRealNameAuthenStatus(mCommonParams), listener);
    }

    /**
     * 获取身份认证状态(司机)
     */
    public void queryDriverIdentityAuthenStatus(ObserverOnResultListener<TypeAuthDriverParams> listener) {
        mCommonParams = getParams(UserCertificationCommandAPI.QUERY_DRIVER_IDENTITY_AUTHEN_STATUS);
        handleOnResultObserver(mApiService.queryDriverIdentityAuthenStatus(mCommonParams), listener);
    }

    /**
     * 获取身份认证状态(货主)
     */
    public void queryOwnerIdentityAuthenStatus(ObserverOnResultListener<TypeAuthOwnerParams> listener) {
        mCommonParams = getParams(UserCertificationCommandAPI.QUERY_OWNER_IDENTITY_AUTHEN_STATUS);
        handleOnResultObserver(mApiService.queryOwnerIdentityAuthenStatus(mCommonParams), listener);
    }

    /**
     * 添加或修改身份认证（司机）
     */
    public void typeAuthDriver(TypeAuthDriverParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(UserCertificationCommandAPI.TYPE_AUTH_DRIVER, params);
        handleOnResultObserver(mApiService.typeauthdriver(mCommonParams),listener);
    }

    /**
     * 添加或修改身份认证（货主）
     */
    public void typeauthowner(TypeAuthOwnerParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(UserCertificationCommandAPI.TYPE_AUTH_SHIPPER, params);
        handleOnResultObserver(mApiService.typeauthowner(mCommonParams),listener);
    }
}
