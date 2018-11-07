package com.topjet.common.common.presenter;

import android.content.Context;

import com.hyphenate.chat.EMClient;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.extra.SettingExtra;
import com.topjet.common.common.view.activity.SettingView;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.config.RespectiveData;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.utils.AppManager;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;

/**
 * Created by yy on 2017/11/2.
 * <p>
 * 设置页面
 */

public class SettingPresenter extends BaseApiPresenter<SettingView, UserCommand> {
    public SettingExtra extra;

    public SettingPresenter(SettingView mView, Context mContext, UserCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
        extra = (SettingExtra) mActivity.getIntentExtra(SettingExtra.getExtraName());
    }


    /**
     * 退出登录
     */
    public void doLogout() {
        mApiCommand.doLogout(new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object object) {
                CPersisData.setIsLogin(false);  // 退出登录
                EMClient.getInstance().logout(true);// 退出环信
                stopServices();      // 关闭听单、定位、悬浮窗等服务
                mView.turnToActivityWithFinish(RespectiveData.getLoginActivityClass());
                Logger.i("oye", "count == " + AppManager.getInstance().getActivityStackSize());
            }
        });
    }

    /**
     * 关闭服务
     */
    private void stopServices() {
        CMemoryData.setSessionId("");
        if (CMemoryData.isDriver()) {
            if (StringUtils.isNotBlank(CPersisData.getFloatViewStatus())) {
                CPersisData.setFloatViewStatus("false");
                // 开启/关闭悬浮窗服务（可控制语音是否播报）
                CommonProvider.getInstance().getListenerOrderProvider().stopFloatViewService(mActivity);
            }
            CommonProvider.getInstance().getListenerOrderProvider().stopListenOrderService(mActivity);

            // 停止定位服务
            CommonProvider.getInstance().getListenerOrderProvider().stopLocationService(mActivity);
        }
    }

}
