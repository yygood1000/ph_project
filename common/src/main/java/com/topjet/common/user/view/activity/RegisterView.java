package com.topjet.common.user.view.activity;

import com.topjet.common.base.view.activity.IView;

/**
 * Created by yy on 2017/8/8.
 * <p>
 * 注册页View接口
 */

public interface RegisterView extends IView {
    void setBtnVerifyCodeEnable(boolean b) throws Exception;

    void setBtnVerifyCodeText(String str);

    void showUserAlreadyRegisterDialog(String mobile);

    void setPhone(String phone);

}
