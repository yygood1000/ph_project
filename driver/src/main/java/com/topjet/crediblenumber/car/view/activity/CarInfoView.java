package com.topjet.crediblenumber.car.view.activity;

import com.topjet.common.base.view.activity.IView;
import com.topjet.crediblenumber.car.modle.bean.TruckTeamCar;

/**
 * Created by tsj-004 on 2017/10/12.
 * 车辆详情
 */
public interface CarInfoView extends IView {
    void showViewByParams(TruckTeamCar car);      // 根据参数显示布局内容
}