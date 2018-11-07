package com.topjet.crediblenumber.car.presenter;

import android.app.Activity;
import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.utils.Toaster;
import com.topjet.crediblenumber.car.modle.bean.TruckTeamCar;
import com.topjet.crediblenumber.car.modle.params.CarIDParams;
import com.topjet.crediblenumber.car.modle.serverAPI.CarCommand;
import com.topjet.crediblenumber.car.modle.serverAPI.CarCommandAPI;
import com.topjet.crediblenumber.car.view.activity.CarInfoView;

/**
 * Created by tsj-004 on 2017/10/12.
 * 车辆详情
 */

public class CarInfoPresenter extends BasePresenter<CarInfoView> {
    private TruckTeamCar truckTeamCar = null;
    public CarInfoPresenter(CarInfoView mView, Context mContext) {
        super(mView, mContext);
    }

    /**
     * 请求服务器接口
     * 查看车辆详情
     */
    public void getTruckInfo(String id) {
        CarIDParams carID = new CarIDParams(id);
        new CarCommand(CarCommandAPI.class, mActivity).getTruckInfo(carID, new ObserverOnNextListener<TruckTeamCar>() {
            @Override
            public void onNext(TruckTeamCar car) {
                if (car != null) {
                    mView.showViewByParams(car);
                    truckTeamCar = car;
                }
            }

            @Override
            public void onError(String errorCode, String msg) {

            }
        });
    }

    /**
     * 请求服务器接口
     * 删除车辆信息
     */
    public void deleteTruckInfo() {
        CarIDParams carIDParams = new CarIDParams();
        if (truckTeamCar != null) {
            carIDParams.setDriver_truck_id(truckTeamCar.getDriver_truck_id());
            carIDParams.setDriver_truck_version(truckTeamCar.getDriver_truck_version());
        }
        new CarCommand(CarCommandAPI.class, mActivity).deleteTruck(carIDParams, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object object) {
                Toaster.showLongToast("删除成功");
                mActivity.setResultAndFinish(Activity.RESULT_OK);
            }
        });
    }
}
