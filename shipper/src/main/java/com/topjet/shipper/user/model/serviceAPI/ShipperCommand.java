package com.topjet.shipper.user.model.serviceAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.user.modle.response.GetAnonymousResponse;
import com.topjet.common.user.modle.response.UserCenterParameterResponse;
import com.topjet.shipper.user.model.params.SettingAnonymousParams;

/**
 * Created by tsj-004 on 2017/10/25.
 * 货主用户相关
 */

public class ShipperCommand extends BaseCommand<ShipperCommandAPI> {
    public ShipperCommand(Class<ShipperCommandAPI> c, MvpActivity mActivity) {
        super(c, mActivity);
    }

    /**
     * 个人中心 - 参数
     */
    public void getUserCentreParameter(ObserverOnNextListener<UserCenterParameterResponse> listener) {
        mCommonParams = getParams(ShipperCommandAPI.USER_CENTRE_PARAMETER);
        handleOnNextObserver(mApiService.getUserCentreParameter(mCommonParams), listener);
    }

    /**
     * 个人中心-货主-匿名设置
     */
    public void settingAnonymous(SettingAnonymousParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(ShipperCommandAPI.SETTING_ANONYMOUS, params);
        handleOnResultObserver(mApiService.settingAnonymous(mCommonParams), listener);
    }

    /**
     * 个人中心-货主-匿名设置
     */
    public void getAnonymous(ObserverOnResultListener<GetAnonymousResponse> listener) {
        mCommonParams = getParams(ShipperCommandAPI.SETTING_ANONYMOUS);
        handleOnResultObserver(mApiService.getAnonymous(mCommonParams), listener);
    }
}
