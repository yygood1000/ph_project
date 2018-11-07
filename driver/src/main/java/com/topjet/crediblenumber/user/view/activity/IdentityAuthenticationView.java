package com.topjet.crediblenumber.user.view.activity;

import com.topjet.common.base.view.activity.IView;

/**
 * Created by yy on 2017/7/29.
 * 身份认证
 */

public interface IdentityAuthenticationView extends IView {
    void submitSuccess();

    void showDrivingLiscensePhoto(String url, String key);

    void showEnterpriseLicensePhoto(String url, String key);

    void setUseStatus(String use_status_remark, String user_auth_status, String driver_license_img_url, String driver_license_img_key, String driver_operation_img_url, String driver_operation_img_key);  // 根据状态设置界面按钮是否可点击
}
