package com.topjet.shipper.familiar_car.model.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.contact.model.InviteTruckResponse;
import com.topjet.shipper.familiar_car.model.params.AroundTruckMapParams;
import com.topjet.shipper.familiar_car.model.params.FindTruckParams;
import com.topjet.common.common.modle.params.TruckParams;
import com.topjet.shipper.familiar_car.model.respons.AroundTruckMapResponse;
import com.topjet.shipper.familiar_car.model.respons.FindTruckListResponse;
import com.topjet.shipper.familiar_car.model.respons.TruckInfoResponse;
import com.topjet.shipper.familiar_car.model.respons.TruckListResponse;

/**
 * creator: zhulunjun
 * time:    2017/10/12
 * describe: 车辆
 */

public class TruckCommand extends BaseCommand<TruckCommandAPI> {
    public TruckCommand(Class<TruckCommandAPI> c, MvpActivity mActivity) {
        super(c, mActivity);
    }

    /**
     * 熟车列表
     */
    public void truckList(TruckParams params, ObserverOnNextListener<TruckListResponse> listener) {
        mCommonParams = getParams(TruckCommandAPI.TRUCK_LIST, params);
        handleOnNextObserver(mApiService.truckList(mCommonParams), listener, false);
    }

    /**
     * 添加，删除 熟车
     */
    public void addOrDeleteTruck(TruckParams params,
                                 ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(TruckCommandAPI.ADD_OR_DELETE_TRUCK, params);
        handleOnResultObserver(mApiService.addOrDeleteTruck(mCommonParams), listener);
    }

    /**
     * 邀请熟车
     */
    public void inviteTruck(TruckParams params,
                            ObserverOnResultListener<InviteTruckResponse> listener) {
        mCommonParams = getParams(TruckCommandAPI.INVITE_TRUCK, params);
        handleOnResultObserver(mApiService.inviteTruck(mCommonParams), listener);
    }

    /**
     * 车辆详情
     */
    public void truckInfo(TruckParams params,
                          ObserverOnResultListener<TruckInfoResponse> listener) {
        mCommonParams = getParams(TruckCommandAPI.TRUCK_INFO, params);
        handleOnResultObserver(mApiService.truckInfo(mCommonParams), listener);
    }

    /**
     * 我要用车-车源列表
     */
    public void getFindTruckList(FindTruckParams params,
                                 ObserverOnNextListener<FindTruckListResponse> listener) {
        mCommonParams = getParams(TruckCommandAPI.FIND_TRUCK, params);
        handleOnNextObserver(mApiService.getFindTruckList(mCommonParams), listener,false);
    }

    /**
     * 货主-附近车源(地图)
     */
    public void getAroundTruckMap(AroundTruckMapParams params,
                                  ObserverOnResultListener<AroundTruckMapResponse> listener) {
        mCommonParams = getParams(TruckCommandAPI.AROUND_TRUCK_MAP, params);
        handleOnResultObserver(mApiService.getAroundTruckMap(mCommonParams), listener);
    }

    /**
     * 货主-附近车源(地图列表)
     */
    public void getAroundTruckMapList(AroundTruckMapParams params,
                                 ObserverOnNextListener<AroundTruckMapResponse> listener) {
        mCommonParams = getParams(TruckCommandAPI.AROUND_TRUCK_MAP_LSIT, params);
        handleOnNextObserver(mApiService.getAroundTruckMapList(mCommonParams), listener);
    }


}
