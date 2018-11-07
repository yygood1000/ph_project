package com.topjet.common.common.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.params.GetPayForInfoParams;
import com.topjet.common.common.modle.response.GetPayForInfoResponse;

/**
 * Created by yy on 2017/9/28.
 * <p>
 * 支付相关
 */

public class PayForCommand extends BaseCommand<PayForCommandAPI> {
    public PayForCommand(Class<PayForCommandAPI> c, MvpActivity mActivity) {
        super(c, mActivity);
    }

    /**
     * Android-获取支付信息
     */
    public void getPayForInfo(GetPayForInfoParams params, boolean isShowProgress,
                              ObserverOnNextListener<GetPayForInfoResponse> listener) {
        mCommonParams = getParams(PayForCommandAPI.GET_PAY_FOR_INFO, params);
        handleOnNextObserver(mApiService.getPayForInfo(mCommonParams), listener, isShowProgress);
    }
}
