package com.topjet.common.common.view.activity;

import com.topjet.common.base.view.activity.IView;
import com.topjet.common.common.modle.response.AppUpgradeResponse;

/**
 * creator: zhulunjun
 * time:    2017/11/8
 * describe:
 */

public interface SystemView extends IView {

    /**
     * 更新弹窗
     * @param result 返回结果
     */
    void showUpdateDialog(AppUpgradeResponse result);

}
