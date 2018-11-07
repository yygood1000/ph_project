package com.topjet.crediblenumber.car.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.crediblenumber.car.modle.bean.TruckTeamCar;
import com.topjet.crediblenumber.car.modle.params.CarIDParams;
import com.topjet.crediblenumber.car.modle.params.PageParams;
import com.topjet.crediblenumber.car.modle.response.MyFleetResponse;

/**
 * Created by tsj-004 on 2017/10/16.
 * 我的车队
 */

public class CarCommand extends BaseCommand<CarCommandAPI> {

    public CarCommand(Class<CarCommandAPI> c, MvpActivity activity) {
        super(c, activity);
    }

    public CarCommand(Class<CarCommandAPI> c) {
        super(c);
    }

    /**
     * 我的车队--查看车队列表
     */
    public void getTruckTeamList(PageParams pageParams, ObserverOnNextListener<MyFleetResponse> listener) {
        mCommonParams = getParams(CarCommandAPI.GET_TRUCK_TEAM_LIST, pageParams);
        handleOnNextObserver(mApiService.getTruckTeamList(mCommonParams), listener);
    }

    /**
     * 我的车队--求货/休息状态切换-----------单个
     */
    public void changeCarWorkStatus(CarIDParams params, ObserverOnNextListener<Object> listener) {
        mCommonParams = getParams(CarCommandAPI.CHANGE_CAR_WORK_STATUS, params);
        handleOnNextObserver(mApiService.changeCarWorkStatus(mCommonParams), listener);
    }

    /**
     * 我的车队--求货/休息状态切换-----------全部
     */
    public void changeAllCarWorkStatus(CarIDParams params, ObserverOnNextListener<Object> listener) {
        mCommonParams = getParams(CarCommandAPI.CHANGE_ALL_CAR_WORK_STATUS, params);
        handleOnNextObserver(mApiService.changeAllCarWorkStatus(mCommonParams), listener);
    }

    /**
     * 我的车队--查看车辆详情
     */
    public void getTruckInfo(CarIDParams params, ObserverOnNextListener<TruckTeamCar> listener) {
        mCommonParams = getParams(CarCommandAPI.GET_TRUCK_INFO, params);
        handleOnNextObserver(mApiService.getTruckInfo(mCommonParams), listener);
    }

    /**
     * 我的车队--添加车辆
     */
    public void addTruck(TruckTeamCar params, ObserverOnNextListener<CarIDParams> listener) {
        mCommonParams = getParams(CarCommandAPI.ADD_TRUCK, params);
        handleOnNextObserver(mApiService.addTruck(mCommonParams), listener);
    }

    /**
     * 我的车队--修改车辆信息
     */
    public void updateTruck(TruckTeamCar params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(CarCommandAPI.UPDATE_TRUCK, params);
        handleOnResultObserver(mApiService.updateTruck(mCommonParams), listener);
    }

    /**
     * 我的车队--删除车辆信息
     */
    public void deleteTruck(CarIDParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(CarCommandAPI.DELETE_TRUCK, params);
        handleOnResultObserver(mApiService.deleteTruck(mCommonParams), listener);
    }
}
