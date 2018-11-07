package com.topjet.common.common.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.params.PhoneParams;
import com.topjet.common.common.modle.params.UserInfoParams;
import com.topjet.common.common.modle.response.UserInfoGoodsResponse;
import com.topjet.common.common.modle.response.UserInfoResponse;
import com.topjet.common.common.modle.response.UserInfoTruckResponse;

/**
 * creator: zhulunjun
 * time:    2017/11/13
 * describe: 诚信查询 用户信息
 */

public class UserInfoCommand extends BaseCommand<UserInfoCommandAPI> {
    public UserInfoCommand(Class<UserInfoCommandAPI> c, MvpActivity mActivity) {
        super(c, mActivity);
    }

    /**
     * 货主查询司机信息
     */
    public void queryDriverInfo(PhoneParams params , ObserverOnNextListener<UserInfoResponse> listener){
        mCommonParams = getParams(UserInfoCommandAPI.QUERY_DRIVER_INFO, params);
        handleOnNextObserver(mApiService.queryDriverInfo(mCommonParams), listener);
    }

    /**
     * 司机查询货主信息
     */
    public void queryShipperInfo(PhoneParams params , ObserverOnNextListener<UserInfoResponse> listener){
        mCommonParams = getParams(UserInfoCommandAPI.QUERY_SHIPPER_INFO, params);
        handleOnNextObserver(mApiService.queryShipperInfo(mCommonParams), listener);
    }

    /**
     * 查询司机车辆信息
     */
    public void queryDriverInfoTruckList(UserInfoParams params , ObserverOnNextListener<UserInfoTruckResponse> listener){
        mCommonParams = getParams(UserInfoCommandAPI.QUERY_DRIVER_INFO_TRUCK_LIST, params);
        handleOnNextObserver(mApiService.queryDriverInfoTruckList(mCommonParams), listener, false);
    }

    /**
     * 查询货主货源信息
     */
    public void queryShipperInfoGoodsList(UserInfoParams params , ObserverOnNextListener<UserInfoGoodsResponse> listener){
        mCommonParams = getParams(UserInfoCommandAPI.QUERY_SHIPPER_INFO_GOODS_LIST, params);
        handleOnNextObserver(mApiService.queryShipperInfoGoodsList(mCommonParams), listener, false);
    }
}
