package com.topjet.common.user.presenter;


import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.topjet.common.R;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.config.ErrorConstant;
import com.topjet.common.config.RespectiveData;
import com.topjet.common.user.modle.extra.PhoneExtra;
import com.topjet.common.user.modle.params.RegisterParams;
import com.topjet.common.user.modle.params.SendCheckCodeParams;
import com.topjet.common.user.modle.response.RegisterReponse;
import com.topjet.common.user.modle.response.SendCheckCodeResponse;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.view.activity.RegisterView;
import com.topjet.common.utils.ApplyUtils;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjetpaylib.encrypt.MD5Helper;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by yy on 2017/7/29.
 * <p>
 * 注册页Presenter
 */

public class RegisterPresenter extends BaseApiPresenter<RegisterView, UserCommand> {

    public final static int GET_VERIFY_CODE = 0;
    public final static int GET_VOICE_VERIFY_CODE = 1;


    private String cityId;
    private String addressDetail;
    private String lat;
    private String lng;
    public PhoneExtra phoneExtra;


    public RegisterPresenter(RegisterView mView, Context mContext, UserCommand mApiCommand) {
        super(mView, mContext, mApiCommand);

    }

    @Override
    public void onCreate() {
        phoneExtra = (PhoneExtra) mActivity.getIntentExtra(PhoneExtra.getExtraName());
    }

    /**
     * 定位当前位置，这里需要添加
     */
    public void getLocate() {
        LocationUtils.location(mContext, new LocationUtils.OnLocationListener() {
            @Override
            public void onLocated(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == LocationUtils.LOCATE_SUCCEED) {
                    cityId = aMapLocation.getAdCode();
                    addressDetail = aMapLocation.getAddress();
                    lat = aMapLocation.getLatitude() + "";
                    lng = aMapLocation.getLongitude() + "";
                }
            }

            // 定位权限获取失败
            @Override
            public void onLocationPermissionfaild() {
            }
        });
    }

    /**
     * 获取验证码 请求服务端
     */
    private void getVerifyCode(final Disposable disposable) {
        SendCheckCodeParams sendCheckCodeParams = new SendCheckCodeParams();
        sendCheckCodeParams.setMobile(CMemoryData.getUserMobile());
        sendCheckCodeParams.setSms_type(SendCheckCodeParams.REGISTER);
        mApiCommand.sendCheckCode(sendCheckCodeParams, new ObserverOnNextListener<SendCheckCodeResponse>() {
            @Override
            public void onNext(SendCheckCodeResponse sendCheckCodeResponse) {
                mView.showToast(ResourceUtils.getString(R.string.check_code_send_succeed));
            }

            @Override
            public void onError(String errorCode, String msg) {
                processSendCheckCodeError(errorCode, msg, disposable);
            }
        });
    }

    /**
     * 获取语音验证码
     */
    private void getVoiceVerifyCode(final Disposable disposable) {
        SendCheckCodeParams sendCheckCodeParams = new SendCheckCodeParams();
        sendCheckCodeParams.setMobile(CMemoryData.getUserMobile());
        sendCheckCodeParams.setSms_type(SendCheckCodeParams.REGISTER);
        mApiCommand.sendVoiceCheckCode(sendCheckCodeParams, new ObserverOnNextListener<SendCheckCodeResponse>() {
            @Override
            public void onNext(SendCheckCodeResponse sendCheckCodeResponse) {
                mView.showToast(ResourceUtils.getString(R.string.check_code_send_succeed));
            }

            @Override
            public void onError(String errorCode, String msg) {
                processSendCheckCodeError(errorCode, msg, disposable);
            }
        });
    }

    /**
     * 发送验证码失败处理方法
     */
    private void processSendCheckCodeError(String errorCode, String msg, Disposable disposable) {
        disposable.dispose();
        switch (errorCode) {
            case ErrorConstant.E_USER_2:
                mView.showUserAlreadyRegisterDialog(CMemoryData.getUserMobile());
                break;
            default:
                mView.showToast(msg);
                break;
        }
        try {
            setBtnVerifyEnable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册/下一步 请求服务端
     */
    public void doRegister(final String mobile, String password, String check_code) {
        if (ApplyUtils.checkRegisterInfo(mobile, password, check_code)) {
            RegisterParams registerParams = new RegisterParams();
            registerParams.setMobile(mobile);
            try {
                registerParams.setPass_word(MD5Helper.getMD5(password));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            registerParams.setCheck_code(check_code);
            registerParams.setRegistered_city_id(cityId);
            registerParams.setGps_address_city_id(cityId);
            registerParams.setGps_detail(addressDetail);
            registerParams.setGps_latitude(lat);
            registerParams.setGps_longitude(lng);

            mApiCommand.register(registerParams, new ObserverOnNextListener<RegisterReponse>() {
                @Override
                public void onNext(RegisterReponse reponseData) {
                    // 记录用户信息
                    CMemoryData.setUserId(reponseData.getUser_id());
                    CMemoryData.setUserMobile(mobile);
                    CPersisData.setIsLogin(true);
                    CPersisData.setUserMobile(mobile);
                    CPersisData.setUserId(reponseData.getUser_id());
                    CPersisData.setUserVersion(reponseData.getVersion());
                    mView.turnToActivityWithFinish(RespectiveData.getRealNameAuthenticationActivityClass());
                }

                @Override
                public void onError(String errorCode, String msg) {
                    mView.showToast(msg);
                }
            });
        }
    }

    /**
     * 开始倒计时
     *
     * @param flag 0：普通验证码 1：语音验证码
     */
    public void startCountDown(final int flag) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(CConstants.SECOND + 1)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.setBtnVerifyCodeEnable(false);
                        if (flag == GET_VERIFY_CODE) {
                            getVerifyCode(disposable);// 获取验证码
                        } else if (flag == GET_VOICE_VERIFY_CODE) {
                            getVoiceVerifyCode(disposable);// 获取语音验证码
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(mActivity.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mView.setBtnVerifyCodeText(CConstants.SECOND - aLong + "s");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e("oye", "验证码倒计时出错" + throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        setBtnVerifyEnable();
                    }
                });
    }

    /**
     * s设置验证码按钮可点击，文字变为默认状态
     */
    private void setBtnVerifyEnable() throws Exception {
        mView.setBtnVerifyCodeEnable(true);
        mView.setBtnVerifyCodeText(ResourceUtils.getString(R.string.get_verification_code));
    }

}
