package com.topjet.shipper.familiar_car.view.activity;

import com.topjet.common.base.view.activity.IListView;

/**
 * creator: yy
 * time:    2017/10/19
 * describe: 我要用车
 */

public interface FindTruckView<D> extends IListView<D> {
    void onPermissionFail();

    void setDepart(String depart);

    void setDestination(String destination);

    void setTruckInfo(String truckLength, String truckType);

    void hideTitleRightImage();
}
