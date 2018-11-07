package com.topjet.shipper.user.view.activity;

import com.topjet.common.base.view.activity.IView;

/**
 * 匿名设置
 */

public interface AnonymousSetView extends IView {
    void setIsAnonymous(boolean b);

    void setCheckBoxListener();

    void setImage();
}
