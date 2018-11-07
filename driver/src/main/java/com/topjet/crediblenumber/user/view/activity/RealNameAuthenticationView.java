package com.topjet.crediblenumber.user.view.activity;

import com.topjet.common.base.view.activity.IView;

/**
 * Created by yy on 2017/7/29.
 * <p>
 * 实名认证界面的View接口
 */

public interface RealNameAuthenticationView extends IView {
    void submitSuccess();    // 提交认证信息成功

    void showAvatarPhoto(String url, String key);

    void showIdCardPhoto(String url, String key);

    void setUseStatus(String use_status, String use_status_remark);  // 根据状态设置界面按钮是否可点击
}