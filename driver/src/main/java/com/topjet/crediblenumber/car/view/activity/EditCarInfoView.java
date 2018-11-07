package com.topjet.crediblenumber.car.view.activity;

import com.topjet.common.base.view.activity.IView;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.crediblenumber.car.modle.bean.TruckTeamCar;

/**
 * Created by tsj-004 on 2017/10/12.
 * 车辆详情
 */
public interface EditCarInfoView extends IView {
    void showViewByParams(TruckTeamCar car);      // 根据参数显示布局内容

    void showError(int error);   // 显示错误

    void setTypeAndLengthText(String txt);  // 显示车型车长

    void showCarFrontPhoto(String url, String key); //设置车头照

    void showVehicleLicense(String url, String key);//设置行驶证
}