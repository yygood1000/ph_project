package com.topjet.crediblenumber.car.modle.extra;

import com.topjet.crediblenumber.car.modle.bean.TruckTeamCar;

/**
 * Created by tsj-004 on 2017/10/17.
 * 进入车辆详情、修改、删除、添加车辆界面传递的数据
 */

public class GoToCarInfoExtra extends TruckTeamCar {
    public final static int IN_TYPE_SHOW = 0;        // 进入类型，显示详情
    public final static int IN_TYPE_EDIT = 1;        // 进入类型，编辑
    public final static int IN_TYPE_ADD = 2;        // 进入类型，添加车辆
    private int inType = IN_TYPE_SHOW;              // 进入类型

    public GoToCarInfoExtra() {
    }

    public GoToCarInfoExtra(int type) {
        this.inType = type;
    }

    public int getInType() {
        return inType;
    }

    public void setInType(int inType) {
        this.inType = inType;
    }
}