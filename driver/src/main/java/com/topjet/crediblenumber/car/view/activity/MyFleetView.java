package com.topjet.crediblenumber.car.view.activity;

import com.topjet.common.base.view.activity.IListView;

/**
 * Created by tsj-004 on 2017/10/12.
 * 我的车队
 */
public interface MyFleetView<TruckTeamCar> extends IListView<TruckTeamCar> {
    void refreAllCheckBox(boolean checked);     // 刷新所有checkbox

    void llAllHideOrShow(boolean hide);     // 列表是空，不显示全部求货和休息。false不显示
}