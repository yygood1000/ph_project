package com.topjet.crediblenumber.user.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.user.modle.response.SignResponse;
import com.topjet.crediblenumber.user.modle.params.GetCallRecordsParams;
import com.topjet.crediblenumber.user.modle.response.GetCollLogListResponse;
import com.topjet.common.user.modle.response.UserCenterParameterResponse;

/**
 * Created by yy on 2017/10/23.
 * 司机用户相关
 */

public class DriverCommand extends BaseCommand<DriverCommandAPI> {
    public DriverCommand(Class<DriverCommandAPI> c, MvpActivity mActivity) {
        super(c, mActivity);
    }

    /**
     * 司机-通话记录
     */
    public void getCallPhoneInfo(GetCallRecordsParams params, ObserverOnNextListener<GetCollLogListResponse> listener) {
        mCommonParams = getParams(DriverCommandAPI.GET_CALL_PHONE_INFO, params);
        handleOnNextObserver(mApiService.getCallPhoneInfo(mCommonParams), listener, false);
    }

    /**
     * TODO 清空通话记录
     */
    public void clearCallPhoneInfo(CommonParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(DriverCommandAPI.CLEAR_CALL_PHONE_INFO, params);
        handleOnResultObserver(mApiService.clearCallPhoneInfo(mCommonParams), listener);
    }

    /**
     * 个人中心 - 签到
     */
    public void signin(ObserverOnResultListener<SignResponse> listener) {
        mCommonParams = getParams(DriverCommandAPI.SIGN);
        handleOnResultObserver(mApiService.signin(mCommonParams), listener);
    }

    /**
     * 个人中心 - 参数
     */
    public void getUserCentreParameter(ObserverOnNextListener<UserCenterParameterResponse> listener) {
        mCommonParams = getParams(DriverCommandAPI.USER_CENTRE_PARAMETER);
        handleOnNextObserver(mApiService.getUserCentreParameter(mCommonParams), listener);
    }
}
