package com.topjet.crediblenumber.goods.view.fragment;

import com.topjet.common.base.view.activity.IView;
import com.topjet.common.order_detail.modle.extra.CityAndLocationExtra;

/**
 * Created by yy on 2017/9/11.
 * 听单View
 */

public interface ListenOrderView extends IView {
    void setStartText(String text, boolean isNull, boolean isNeeedLocation);

    void setEndText(String text);

    /**
     * 设置popup中的地址
     */
    void setPopAddress(CityAndLocationExtra extra);


    void showOrHidePermissionsFail(boolean show);           // 显示或隐藏无法获得定位权限

    void requestOverlayPermission();     // 申请悬浮窗权限
}
