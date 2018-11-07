package com.topjet.shipper.familiar_car.presenter;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.common.modle.params.TruckParams;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.Toaster;
import com.topjet.shipper.familiar_car.model.extra.RefindTruckExtra;
import com.topjet.shipper.familiar_car.model.params.FindTruckParams;
import com.topjet.shipper.familiar_car.model.respons.FindTruckListResponse;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommand;
import com.topjet.shipper.familiar_car.view.activity.FindTruckView;

/**
 * 我要用车
 */
public class FindTruckPresenter extends BaseApiPresenter<FindTruckView<TruckInfo>, TruckCommand> {
    public final RefindTruckExtra extra;
    public String startCityId;
    public String destinationCityId;
    public double lat;
    public double lng;
    public TruckTypeLengthSelectedData truckSelectedData;
    public String departCityNmae;
    public String destinitionCityNmae;
    public boolean isNeedLocat = true;

    public FindTruckPresenter(FindTruckView<TruckInfo> mView, Context mContext, TruckCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
        extra = (RefindTruckExtra) mActivity.getIntentExtra(RefindTruckExtra.getExtraName());
        if (extra != null) {
            // 重新找车进来，隐藏地图图标
            if(extra.isRefund()){
                mView.hideTitleRightImage();
            }
            isNeedLocat = false;
            startCityId = extra.getDepartCityId();
            destinationCityId = extra.getDestinationCityId();
            departCityNmae = extra.getDepartCityName();
            destinitionCityNmae = extra.getDestinationCityName();
            startCityId = extra.getDepartCityId();
            mView.setDepart(extra.getDepartCityName());
            mView.setDestination(extra.getDestinationCityName());
        }
    }

    /**
     * 定位当前位置，这里需要添加
     */
    public void getLocate() {
        LocationUtils.location(mContext, new LocationUtils.OnLocationListener() {
            @Override
            public void onLocated(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == LocationUtils.LOCATE_SUCCEED) {
                    startCityId = aMapLocation.getAdCode();
                    lat = aMapLocation.getLatitude();
                    lng = aMapLocation.getLongitude();
                    Logger.i("oye", "lat = " + lat + " lng = " + lng);
                    mView.setDepart(departCityNmae = AreaDataDict.getLastCityNameByLocation(aMapLocation));
                } else {
                    Toaster.showShortToast("定位失败");
                    startCityId = AreaDataDict.DEFAULT_CITY_CODE;
                    lat = AreaDataDict.DEFAULT_CITY_LAT;
                    lng = AreaDataDict.DEFAULT_CITY_LNG;
                    mView.setDepart(departCityNmae = AreaDataDict.DEFAULT_CITY_NAME);
                }
                // 获取我要用车列表数据
                mView.refresh();
            }

            // 定位权限获取失败
            @Override
            public void onLocationPermissionfaild() {
                mView.onPermissionFail();
            }
        });
    }

    /**
     * 获取我要用车列表数据
     */
    public void getTruckList(int page) {
        FindTruckParams findTruckParams = new FindTruckParams();
        findTruckParams.setPage(page + "");
        findTruckParams.setStart_city_id(startCityId);
        findTruckParams.setDestination_city_id(destinationCityId);
        findTruckParams.setTruck_type_id(truckSelectedData != null ? truckSelectedData.getSingleTruckTypeId() : "");
        findTruckParams.setTruck_length_id(truckSelectedData != null ? truckSelectedData.getSingleTruckLengthId() : "");

        mApiCommand.getFindTruckList(findTruckParams, new ObserverOnNextListener<FindTruckListResponse>() {
            @Override
            public void onNext(FindTruckListResponse response) {
                mView.loadSuccess(response.getList());
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });
    }

    /**
     * 删除，或者，添加
     *
     * @param carId 车辆id
     * @param flag  添加0，删除1
     */
    public void addOrDeleteCar(String carId, final int flag) {
        TruckParams params = new TruckParams();
        params.setTruck_id(carId);
        params.setFlag(flag + "");
        mApiCommand.addOrDeleteTruck(params, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                if (flag == TruckParams.ADD) {
                    Toaster.showShortToast("添加成功");
                } else {
                    Toaster.showShortToast("删除成功");
                }
                mView.refresh();
            }
        });
    }
}
