package com.topjet.crediblenumber.user.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.crediblenumber.user.modle.params.UploadUsualCityInCenterParams;
import com.topjet.crediblenumber.user.modle.params.UploadUsualCityParams;
import com.topjet.crediblenumber.user.modle.response.GetUsualCityListResponse;

import static com.topjet.crediblenumber.user.modle.serverAPI.UsualCityCommandAPI.GET_USUAL_CITY_LIST_IN_CENTER;
import static com.topjet.crediblenumber.user.modle.serverAPI.UsualCityCommandAPI.UPDATA_USUAL_CITY_LIST_IN_CENTER;
import static com.topjet.crediblenumber.user.modle.serverAPI.UsualCityCommandAPI.UPLOAD_USUAL_CITY;

/**
 * Created by tsj-004 on 2017/7/25.
 * 常跑城市相关
 */

public class UsualCityCommand extends BaseCommand<UsualCityCommandAPI> {
    public UsualCityCommand(Class<UsualCityCommandAPI> c, MvpActivity activity) {
        super(c, activity);
    }

    /**
     * 上传常跑城市
     */
    public void uploadUsualCity(UploadUsualCityParams params,
                                ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(UPLOAD_USUAL_CITY, params);
        handleOnResultObserver(mApiService.uploadUsualCity(mCommonParams), listener);
    }

    /**
     * 获取常跑路线列表
     */
    public void getUsualCityList(CommonParams commonParams,
                                 ObserverOnNextListener<GetUsualCityListResponse> listener) {
        handleOnNextObserver(mApiService.getUsualCityList(commonParams), listener);
    }

    /**
     * 常跑城市-个人中心列表
     */
    public void getUsualCityListInCenter(CommonParams commonParams,
                                         ObserverOnNextListener<GetUsualCityListResponse>
                                                 listener) {
        mCommonParams = getParams(GET_USUAL_CITY_LIST_IN_CENTER, commonParams);
        handleOnNextObserver(mApiService.getUsualCityListInCenter(mCommonParams), listener, false);
    }

    /**
     * 常跑路线-个人中心提交
     */
    public void updataUsualCityListInCenter(UploadUsualCityInCenterParams params,
                                            ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(UPDATA_USUAL_CITY_LIST_IN_CENTER, params);
        handleOnResultObserver(mApiService.updataUsualCityListInCenter(mCommonParams), listener);
    }


}
