package com.topjet.common.common.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.model.Session;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.KeyBean;
import com.topjet.common.common.modle.params.FixedCycleLocationParams;
import com.topjet.common.common.modle.params.PhoneParams;
import com.topjet.common.common.modle.response.AppUpgradeResponse;
import com.topjet.common.common.modle.response.MessageCenterCountResponse;
import com.topjet.common.common.modle.response.ShareResponse;

/**
 * Created by tsj-004 on 2017/7/25.
 * 基础服务
 */

public class SystemCommand extends BaseCommand<SystemCommandAPI> {

    public SystemCommand(MvpActivity mActivity) {
        super(SystemCommandAPI.class, mActivity);
    }

    public SystemCommand() {
        super(SystemCommandAPI.class);
    }

    /**
     * 检测APP更新
     */
    public void appUpgrade(ObserverOnResultListener<AppUpgradeResponse> listener) {
        mCommonParams = getParams(SystemCommandAPI.APP_UPGRADE);
        handleOnResultObserver(mApiService.appUpgrade(mCommonParams), listener, false);
    }

    /**
     * 分享，二维码
     *
     * @param listener
     */
    public void shareAppOfQrcode(ObserverOnResultListener<ShareResponse> listener) {
        mCommonParams = getParams(SystemCommandAPI.SHARE_APP_OF_QRCODE, new PhoneParams());
        handleOnResultObserver(mApiService.shareAppOfQrcode(mCommonParams), listener);
    }

    /**
     * 分享，短信
     *
     * @param listener
     */
    public void shareAppOfSms(ObserverOnResultListener<ShareResponse> listener) {
        mCommonParams = getParams(SystemCommandAPI.SHARE_APPOF_SMS, new PhoneParams());
        handleOnResultObserver(mApiService.shareAppOfSms(mCommonParams), listener);
    }

    /**
     * 定时上传定位信息
     */
    public void saveUserGpsInfo(FixedCycleLocationParams params, ObserverOnNextListener<Object> listener) {
        mCommonParams = getParams(SystemCommandAPI.SAVE_USER_GPS_INFO, params);
        handleOnNextObserverNotActivity(mApiService.saveUserGpsInfo(mCommonParams), listener);
    }

    /**
     * 获取session
     */
    public void getSession(ObserverOnNextListener<Session> listener) {
        mCommonParams = getParams(SystemCommandAPI.GET_SESSION);
        handleOnNextObserverNotActivity(mApiService.getSession(mCommonParams), listener);
    }

    /**
     * 获取消息中心的未读数
     */
    public void getMessageCenterCount(ObserverOnResultListener<MessageCenterCountResponse> listener) {
        mCommonParams = getParams(SystemCommandAPI.MESSAGE_CENTER_LIST_COUNT);
        handleOnResultObserver(mApiService.getMessageCenterCount(mCommonParams), listener);
    }

    /**
     * 获取加密key
     * @param listener 监听
     */
    public void getKey(ObserverOnNextListener<KeyBean> listener){
        mCommonParams = getParams(SystemCommandAPI.GET_KEY);
        handleOnNextObserverNotActivity(mApiService.getKey(mCommonParams), listener);
    }
}
