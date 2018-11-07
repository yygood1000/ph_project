package com.topjet.common.user.view.activity;

import com.topjet.common.common.view.activity.SystemView;

/**
 * Created by tsj-004 on 2017/8/7.
 */

public interface LoginView extends SystemView {
    void changeLoginWay(int curLoinType); // 切换登录模式

    void autoSendCode();// 自动发送验证码

    void showUserNotRegisterDialog(String title);        // 还没注册

    void showUserBeBanned(String time, String remark);        // 用户是否被冻结
}
