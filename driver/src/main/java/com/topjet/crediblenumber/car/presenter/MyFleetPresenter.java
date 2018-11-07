package com.topjet.crediblenumber.car.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.utils.Toaster;
import com.topjet.crediblenumber.car.modle.bean.TruckTeamCar;
import com.topjet.crediblenumber.car.modle.params.CarIDParams;
import com.topjet.crediblenumber.car.modle.params.PageParams;
import com.topjet.crediblenumber.car.modle.response.MyFleetResponse;
import com.topjet.crediblenumber.car.modle.serverAPI.CarCommand;
import com.topjet.crediblenumber.car.modle.serverAPI.CarCommandAPI;
import com.topjet.crediblenumber.car.view.activity.MyFleetView;

import java.util.List;

/**
 * Created by tsj-004 on 2017/10/12.
 * 我的车队
 */

public class MyFleetPresenter extends BaseApiPresenter<MyFleetView<TruckTeamCar>, CarCommand> {
    private List<TruckTeamCar> truckTeams = null;  // 车队列表数据

    public MyFleetPresenter(MyFleetView<TruckTeamCar> mView, Context mContext) {
        super(mView, mContext);
        mApiCommand = new CarCommand(CarCommandAPI.class, mActivity);
    }

    /**
     * 请求服务器接口
     * 查看车队列表
     */
    public void getTruckTeamList(int curPage) {
        PageParams pageParams = new PageParams(curPage + "");
        mApiCommand.getTruckTeamList(pageParams, new ObserverOnNextListener<MyFleetResponse>() {
            @Override
            public void onNext(MyFleetResponse myFleetResponse) {
                if (myFleetResponse != null && myFleetResponse.getTruck_team_list() != null) {
                    truckTeams = myFleetResponse.getTruck_team_list();
                    mView.loadSuccess(truckTeams);

                    if (myFleetResponse.getTruck_team_list().size() == 0) {
                        mView.llAllHideOrShow(true);   // 列表是空，不显示全部求货和休息。false不显示
                    } else {
                        mView.llAllHideOrShow(false);
                    }
                } else {
                    mView.llAllHideOrShow(true);   // 列表是空，不显示全部求货和休息。false不显示
                }
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
                mView.llAllHideOrShow(false);   // 列表是空，不显示全部求货和休息。false不显示
            }
        });
    }

    /**
     * 请求服务器接口
     * 求货/休息状态切换----单个
     */
    public void changeOnlyOneCarWorkStatus(int position, boolean checked) {
        if (truckTeams != null) {
            String carId = truckTeams.get(position).getDriver_truck_id();
            CarIDParams params = new CarIDParams();
            params.setDriver_truck_id(carId);
            makeParams(params, checked);        // 处理参数
            new CarCommand(CarCommandAPI.class, mActivity).changeCarWorkStatus(params, new ObserverOnNextListener<Object>() {
                @Override
                public void onNext(Object object) {
                    Toaster.showLongToast("状态设置成功");
                }

                @Override
                public void onError(String errorCode, String msg) {

                }
            });
        }
    }

    /**
     * 请求服务器接口
     * 求货/休息状态切换----全部
     */
    public void changeAllCarWorkStatus(final boolean checked) {
        if (truckTeams != null) {
            CarIDParams params = new CarIDParams();
            makeParams(params, checked);        // 处理参数
            new CarCommand(CarCommandAPI.class, mActivity).changeAllCarWorkStatus(params, new ObserverOnNextListener<Object>() {
                @Override
                public void onNext(Object object) {
                    for (int i = 0; i < truckTeams.size(); i++) {
                        truckTeams.get(i).setTruck_status(getStatusStr(checked));
                    }
                    mView.refreAllCheckBox(checked);   // 刷新所有checkbox
                }

                @Override
                public void onError(String errorCode, String msg) {

                }
            });
        }
    }

    /**
     * 处理参数
     */
    private void makeParams(CarIDParams params, boolean checked) {
        params.setTruck_status(getStatusStr(checked));
    }

    /**
     * 获取状态string
     */
    private String getStatusStr(boolean checked) {
        if (checked) {
            return "1";
        } else {
            return "2";
        }
    }

    public List<TruckTeamCar> getTruckTeams() {
        return truckTeams;
    }
}
