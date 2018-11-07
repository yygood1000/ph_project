package com.topjet.common.user.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.params.CheckVerificationCodeParams;
import com.topjet.common.config.CPersisData;
import com.topjet.common.user.modle.bean.ReferrerInfoBean;
import com.topjet.common.user.modle.params.EditAvatarParams;
import com.topjet.common.user.modle.params.GetRecommendListParams;
import com.topjet.common.user.modle.params.LoginParams;
import com.topjet.common.user.modle.params.PasswordParams;
import com.topjet.common.user.modle.params.RegisterParams;
import com.topjet.common.user.modle.params.SaveCallPhoneInfoParams;
import com.topjet.common.user.modle.params.SendCheckCodeParams;
import com.topjet.common.user.modle.params.SwitchLoginParams;
import com.topjet.common.user.modle.params.UpdateUserPassParams;
import com.topjet.common.user.modle.response.GetAvatarInfoResponse;
import com.topjet.common.user.modle.response.LoginResponse;
import com.topjet.common.user.modle.response.PasswordResponse;
import com.topjet.common.user.modle.response.RecommendListResponse;
import com.topjet.common.user.modle.response.RegisterReponse;
import com.topjet.common.user.modle.response.RetrievePasswordResponse;
import com.topjet.common.user.modle.response.SendCheckCodeResponse;
import com.topjet.common.user.modle.response.SwitchLoginResponse;

/**
 * Created by tsj-004 on 2017/7/25.
 * 用户相关
 */

public class UserCommand extends BaseCommand<UserCommandAPI> {

    public UserCommand(Class c, MvpActivity activity) {
        super(c, activity);
    }


    /**
     * 登录
     */
    public void login(LoginParams params, ObserverOnNextListener<LoginResponse> listener) {
        // 设置推送token
        params.setMsg_push_token(CPersisData.getPushToken(""));
        mCommonParams = getParams(UserCommandAPI.LOGIN, params);
        handleOnNextObserver(mApiService.login(mCommonParams), listener);
    }

    /**
     * 发送验证码
     */
    public void sendCheckCode(SendCheckCodeParams params, ObserverOnNextListener<SendCheckCodeResponse> listener) {
        mCommonParams = getParams(UserCommandAPI.CHECK_CODE, params);
        handleOnNextObserver(mApiService.sendCheckCode(mCommonParams), listener);
    }

    /**
     * 发送语音验证码
     */
    public void sendVoiceCheckCode(SendCheckCodeParams params, ObserverOnNextListener<SendCheckCodeResponse> listener) {
        mCommonParams = getParams(UserCommandAPI.CHECK_VOICE_CODE, params);
        handleOnNextObserver(mApiService.sendVoiceCheckCode(mCommonParams), listener);
    }

    /**
     * 注册请求
     */
    public void register(RegisterParams params, ObserverOnNextListener<RegisterReponse> listener) {
        mCommonParams = getParams(UserCommandAPI.REGISTER, params);
        handleOnNextObserver(mApiService.register(mCommonParams), listener);
    }

    /**
     * 重置密码
     */
    public void updateUserPass(UpdateUserPassParams params, String mobile,
                               ObserverOnResultListener<RetrievePasswordResponse> listener) {
        mCommonParams = getParams(UserCommandAPI.UPDATE_PASSWORD, params);
        mCommonParams.setMobile(mobile);
        handleOnResultObserver(mApiService.updateUserPass(mCommonParams), listener);
    }

    /**
     * 校验验证码
     */
    public void checkCode(CheckVerificationCodeParams params, ObserverOnResultListener<SendCheckCodeResponse>
            listener) {
        mCommonParams = getParams(UserCommandAPI.CHECK_OUT_CODE, params);
        handleOnResultObserver(mApiService.checkCode(mCommonParams), listener);
    }

    /**
     * 保存通话记录
     */
    public void saveCallPhoneInfo(SaveCallPhoneInfoParams saveCallPhoneInfoParams, ObserverOnResultListener<Object>
            listener) {
        mCommonParams = getParams(UserCommandAPI.SAVE_CALL_PHONE_INFO, saveCallPhoneInfoParams);
        handleOnResultObserver(mApiService.saveCallPhoneInfo(mCommonParams), listener);
    }

    /**
     * 校验旧密码
     */
    public void checkOldPassword(PasswordParams saveCallPhoneInfoParams,
                                 ObserverOnNextListener<RetrievePasswordResponse>
                                         listener) {
        mCommonParams = getParams(UserCommandAPI.CHECK_OLD_PASSWORD, saveCallPhoneInfoParams);
        handleOnNextObserver(mApiService.checkOldPassword(mCommonParams), listener);
    }

    /**
     * 修改密码
     */
    public void changePassword(PasswordParams saveCallPhoneInfoParams, ObserverOnResultListener<PasswordResponse>
            listener) {
        mCommonParams = getParams(UserCommandAPI.CHANGE_PASSWORD, saveCallPhoneInfoParams);
        handleOnResultObserver(mApiService.changePassword(mCommonParams), listener);
    }

    /**
     * 个人中心 - 退出
     */
    public void doLogout(ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(UserCommandAPI.LOGOUT);
        handleOnResultObserver(mApiService.doLogout(mCommonParams), listener);
    }

    /**
     * 个人中心 - 获取推荐人信息
     */
    public void getReferrerInfo(ObserverOnResultListener<ReferrerInfoBean> listener) {
        mCommonParams = getParams(UserCommandAPI.GET_REFERRER_INFO);
        handleOnResultObserver(mApiService.getReferrerInfo(mCommonParams), listener);
    }

    /**
     * 个人中心 - 获取推荐记录
     */
    public void getRecommendlist(GetRecommendListParams params, ObserverOnNextListener<RecommendListResponse>
            listener) {
        mCommonParams = getParams(UserCommandAPI.GET_RECOMMEND_LIST, params);
        handleOnNextObserver(mApiService.getRecommendList(mCommonParams), listener);
    }

    /**
     * 个人中心->修改头像  获取头像信息
     */
    public void getAvatarInfo(ObserverOnResultListener<GetAvatarInfoResponse> listener) {
        mCommonParams = getParams(UserCommandAPI.GET_AVATAR_INFO);
        handleOnResultObserver(mApiService.getAvatarInfo(mCommonParams), listener);
    }

    /**
     * 个人中心->修改头像  修改用户头像
     */
    public void editAvatarInfo(EditAvatarParams params, ObserverOnResultListener<Object>
            listener) {
        mCommonParams = getParams(UserCommandAPI.EDIT_AVATAR_INFO, params);
        handleOnResultObserver(mApiService.editAvatarInfo(mCommonParams), listener);
    }

    /**
     * 切换后登录
     */
    public void switchLogin(SwitchLoginParams params, ObserverOnNextListener<SwitchLoginResponse>
            listener) {
        mCommonParams = getParams(UserCommandAPI.SWITCH_LOGIN, params);
        handleOnNextObserver(mApiService.switchLogin(mCommonParams), listener);
    }
}


