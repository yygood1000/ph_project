package com.topjet.crediblenumber.user.view.activity;

import com.topjet.common.base.view.activity.IView;

/**
 * Created by yy on 2017/7/29.
 * 车辆认证
 */

public interface CarAuthenticationView extends IView {
    void submitSuccess();

    void showCarFrontPhoto(String url, String key);      // 显示车头照

    void showVehicleLicense(String url, String key);     // 显示行驶证

    void setTypeAndLengthText(String txt);   // 显示车型车长

    void setUseStatus(String use_status_remark, String use_status);  // 根据状态设置界面按钮是否可点击

    void showViewByParams(String plate_color, String plate_no1, String carNumberNoLocation, String carNumber);     // 根据参数设置界面

    void changeShowMode(boolean showResult); // 根据参数选择显示结果还是编辑项
}
