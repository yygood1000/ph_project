package com.topjet.shipper.user.view.fragment;

import com.topjet.common.base.view.activity.IView;

/**
 * Created by yy on 2017/8/14.
 * 首页View
 */

public interface PersonCenterView extends IView {
    void setViewByParams(String user_name, String icon_image_url, String icon_image_key, String
            icon_image_status, String sign_status, boolean isHideRightArrow, String today_integral, String
                                 degree_of_praise, String integral_amount, String wallet_balance, String
                                 use_status);          // 根据参数设置界面内容


    void requestFail();    // 请求网络参数错误
}
