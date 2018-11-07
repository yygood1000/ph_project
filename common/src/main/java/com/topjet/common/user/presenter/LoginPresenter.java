package com.topjet.common.user.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.jakewharton.rxbinding2.view.RxView;
import com.topjet.common.R;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.model.Session;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.bean.KeyBean;
import com.topjet.common.common.modle.serverAPI.SystemCommand;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.config.ErrorConstant;
import com.topjet.common.config.RespectiveData;
import com.topjet.common.user.modle.params.LoginParams;
import com.topjet.common.user.modle.params.SendCheckCodeParams;
import com.topjet.common.user.modle.response.LoginResponse;
import com.topjet.common.user.modle.response.SendCheckCodeResponse;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.view.activity.LoginView;
import com.topjet.common.utils.AppManager;
import com.topjet.common.utils.ApplyUtils;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.common.utils.Toaster;
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
 * Created by tsj-004 on 2017/7/29.
 * 登录
 */

public class LoginPresenter extends BaseApiPresenter<LoginView, UserCommand> {
    // 当前登录模式 0 -> 密码登录 1 -> 验证码登录
    public int curLoginMode = -1;
    // 密码登录
    private static final int LOGIN_MODE_PASSWORD = 0;
    // 验证码登录
    private static final int LOGIN_MODE_CHECK_CODE = 1;

    // 登录城市id
    private String loginCityId;
    // 登录地址
    private String loginAddress;
    // 经度
    private String loginLongitude;
    // 纬度
    private String loginLatitude;

    public LoginPresenter(LoginView mView, Context mContext, UserCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
        LocationUtils.location(mContext, new LocationUtils.OnLocationListener() {
            @Override
            public void onLocated(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    loginAddress = aMapLocation.getAddress();
                    loginCityId = aMapLocation.getAdCode();
                    loginLatitude = aMapLocation.getLatitude() + "";
                    loginLongitude = aMapLocation.getLongitude() + "";
                }
            }

            @Override
            public void onLocationPermissionfaild() {
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 关闭其他页面
        AppManager.getInstance().finishOtherActivity();
    }

    /**
     * 请求登录接口
     */
    public void doLogin(EditText etPhonenumber, EditText etPassword, EditText etCode) {
        final String phone = etPhonenumber.getText().toString();
        if (!ApplyUtils.validateMobile(phone)) {
            mView.showToast(ResourceUtils.getString(R.string.please_input_correct_phone_number));
            return;
        }

        String pass;
        if (curLoginMode % 2 == LOGIN_MODE_PASSWORD) {
            pass = etPassword.getText().toString();
            if (StringUtils.isEmpty(pass)) {
                mView.showToast(ResourceUtils.getString(R.string.please_input_password));
                return;
            }
        } else {
            pass = etCode.getText().toString();
            if (StringUtils.isEmpty(pass)) {
                mView.showToast(ResourceUtils.getString(R.string.please_input_verification_code));
                return;
            }
        }

        // 将登陆用户手机号记录
        CMemoryData.setUserMobile(phone);
        CPersisData.setUserMobile(phone);

        LoginParams loginParams = new LoginParams();
        loginParams.setMobile(phone);
        if ((curLoginMode % 2 + 1) == LOGIN_MODE_CHECK_CODE) {
            try {
                loginParams.setPass_word(MD5Helper.getMD5(pass));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            loginParams.setPass_word(pass);
        }
        loginParams.setLogin_type(curLoginMode % 2 + 1 + "");//1:密码登录 2:验证码登录
        if (CMemoryData.isDriver()) {
            loginParams.setSystem_type("1");//1 Android司机 2.Android货主
        } else {
            loginParams.setSystem_type("2");
        }
        loginParams.setLogin_address(loginAddress);
        loginParams.setLogin_city_id(loginCityId);
        loginParams.setLogin_latitude(loginLatitude);
        loginParams.setLogin_longitude(loginLongitude);

        mApiCommand.login(loginParams, new ObserverOnNextListener<LoginResponse>() {
            @Override
            public void onNext(LoginResponse loginResponse) {
                CMemoryData.setUserId(loginResponse.getUser_id());
                CPersisData.setIsLogin(true);
                CPersisData.setUserId(loginResponse.getUser_id());
                CPersisData.setUserName(loginResponse.getUser_name());
                CPersisData.setUserVersion(loginResponse.getVersion());

                /**
                 * 是否冻结
                 * 0：未冻结 1：冻结
                 */
                if (loginResponse.getIs_congeal()) {
                    String time = TimeUtils.showTimeDaySecond(loginResponse.getCongeal_time());
                    mView.showUserBeBanned(time, loginResponse.getCongeal_remark()); // 用户是否被冻结
                    return;
                }

                /**
                 * 是否属于H5注册第一次登陆
                 * 1是 2不是
                 */
                if (loginResponse.getIs_h5_login_one()) {
                    mView.turnToActivityWithFinish(RespectiveData.getRealNameAuthenticationActivityClass()); // 跳转身份认证
                } else {
                    mView.turnToActivityWithFinish(RespectiveData.getMainActivityClass());// 跳转主页
                }
            }

            @Override
            public void onError(String errorCode, String msg) {
                switch (errorCode) {
                    case ErrorConstant.E_USER_5:
                        mView.showToast(msg);
                        curLoginMode = LOGIN_MODE_CHECK_CODE; // 1 -> 验证码登录
                        mView.changeLoginWay(curLoginMode);
                        mView.autoSendCode();   // 自动发送验证码
                        break;
                    case ErrorConstant.E_USER_1:
                        mView.showUserNotRegisterDialog(msg); // 用户没注册就跑来登录
                    default:
                        mView.showToast(msg);
                        break;
                }
            }
        });
    }

    /**
     * 关闭服务
     */
    public void stopServices() {
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

    /**
     * 切换登录模式
     */
    public void changeLoginWay() {
        curLoginMode++;
        mView.changeLoginWay(curLoginMode);
    }

    /**
     * 开始倒计时
     *
     * @param flag 0：普通验证码 1：语音验证码
     */
    public void startCountDown(final TextView tvSendCode, final TextView tvVoiceCode, final int flag) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(CConstants.SECOND + 1)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        RxView.enabled(tvSendCode).accept(false);
                        RxView.enabled(tvVoiceCode).accept(false);
                        if (flag == RegisterPresenter.GET_VERIFY_CODE) {
                            getVerifyCode(disposable, tvSendCode, tvVoiceCode);// 获取验证码
                        } else {
                            getVoiceVerifiCode(disposable, tvSendCode, tvVoiceCode);// 获取语音验证码
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(mActivity.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        tvSendCode.setText(CConstants.SECOND - aLong + "s");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {

                    @Override
                    public void run() throws Exception {
                        resetCountDown(tvSendCode, tvVoiceCode);
                    }
                });
    }

    /**
     * 重置倒计时
     */
    public void resetCountDown(TextView tvSendCode, TextView tvVoiceCode) {
        try {
            RxView.enabled(tvSendCode).accept(true);
            RxView.enabled(tvVoiceCode).accept(true);
            tvSendCode.setText(ResourceUtils.getString(R.string.get_verification_code));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVerifyCode(final Disposable disposable, final TextView tvSendCode, final TextView tvVoiceCode) {
        getVerifyCode(disposable, tvSendCode, tvVoiceCode, SendCheckCodeParams.LOGIN);// 获取验证码
    }

    public void getVoiceVerifiCode(final Disposable disposable, final TextView tvSendCode, final TextView tvVoiceCode) {
        getVoiceVerifiCode(disposable, tvSendCode, tvVoiceCode, SendCheckCodeParams.LOGIN);
    }

    /**
     * 获取验证码
     */
    public void getVerifyCode(final Disposable disposable, final TextView tvSendCode, final TextView tvVoiceCode,
                              String smsType) {
        if(StringUtils.isEmpty(CPersisData.getUserSession())){
            getSession(disposable, tvSendCode, tvVoiceCode, smsType);
            return;
        }
        SendCheckCodeParams sendCheckCodeParams = new SendCheckCodeParams();
        sendCheckCodeParams.setMobile(CMemoryData.getUserMobile());
        sendCheckCodeParams.setSms_type(smsType);
        mApiCommand.sendCheckCode(sendCheckCodeParams, new ObserverOnNextListener<SendCheckCodeResponse>() {
            @Override
            public void onNext(SendCheckCodeResponse sendCheckCodeResponse) {
                Toaster.showLongToast(R.string.check_code_send_succeed);
            }

            @Override
            public void onError(String errorCode, String msg) {
                if (TextUtils.isEmpty(msg)) {
                    Toaster.showLongToast(R.string.send_fail);
                } else if (errorCode.equals(ErrorConstant.E_USER_1)) {
                    mView.showUserNotRegisterDialog(msg);
                } else {
                    Toaster.showLongToast(msg);
                }
                if (!TextUtils.isEmpty(errorCode)) {
                    disposable.dispose();
                    resetCountDown(tvSendCode, tvVoiceCode);
                }
            }
        });
    }

    /**
     * 第一次进入登录页面，需要向获取session,验证码接口重连5次会失效，所以主动请求session
     */
    private void getSession(final Disposable disposable, final TextView tvSendCode, final TextView tvVoiceCode,
                            final String smsType){
        new SystemCommand().getSession(new ObserverOnNextListener<Session>() {
            @Override
            public void onNext(Session session) {
                Logger.d("sessionid  " + session.getSession_id());
                CMemoryData.setSessionId(session.getSession_id());
                CPersisData.setUserSession(session.getSession_id());
                getKey(disposable, tvSendCode, tvVoiceCode, smsType);
            }

            @Override
            public void onError(String errorCode, String msg) {
                Toaster.showShortToast(msg);
            }
        });
    }

    /**
     * 获取key
     */
    private void getKey(final Disposable disposable, final TextView tvSendCode, final TextView tvVoiceCode,
                        final String smsType){
        new SystemCommand().getKey(new ObserverOnNextListener<KeyBean>() {
            @Override
            public void onNext(KeyBean keyBean) {
                Logger.d(" 获取key " + keyBean.getAes_key()+" "+keyBean.getAes_ivcode());
                CPersisData.setKey(keyBean.getAes_key());
                CPersisData.setKeyIvCode(keyBean.getAes_ivcode());
                getVerifyCode(disposable, tvSendCode, tvVoiceCode, smsType);
            }

            @Override
            public void onError(String errorCode, String msg) {
                Toaster.showShortToast(msg);
            }
        });

    }

    /**
     * 获取语音验证码
     */
    private void getVoiceVerifiCode(final Disposable disposable, final TextView tvSendCode, final TextView
            tvVoiceCode, String smsType) {
        SendCheckCodeParams sendCheckCodeParams = new SendCheckCodeParams();
        sendCheckCodeParams.setMobile(CMemoryData.getUserMobile());
        sendCheckCodeParams.setSms_type(smsType);
        mApiCommand.sendVoiceCheckCode(sendCheckCodeParams, new ObserverOnNextListener<SendCheckCodeResponse>() {
            @Override
            public void onNext(SendCheckCodeResponse status) {
                if ("1".equals(status.getStatus())) {
                    Toaster.showLongToast(R.string.check_code_send_succeed);
                } else {
                    Toaster.showLongToast(R.string.send_fail);
                }
            }

            @Override
            public void onError(String errorCode, String msg) {
                if (TextUtils.isEmpty(msg)) {
                    Toaster.showLongToast(R.string.send_fail);
                } else if (errorCode.equals(ErrorConstant.E_USER_1)) {
                    mView.showUserNotRegisterDialog(msg);
                } else {
                    Toaster.showLongToast(msg);
                }
                if (!TextUtils.isEmpty(errorCode)) {
                    disposable.dispose();
                    resetCountDown(tvSendCode, tvVoiceCode);
                }
            }

        });
    }

    public int getCurLoginMode() {
        return curLoginMode;
    }

    public void setCurLoginMode(int curLoginMode) {
        this.curLoginMode = curLoginMode;
    }

    /**
     * 切换到密码登录
     */
    public void setCurLoginModeToPass() {
        this.curLoginMode = LOGIN_MODE_PASSWORD;
        mView.changeLoginWay(curLoginMode);
    }

    /**
     * 切换到验证码登录
     */
    public void setCurLoginModeToCode() {
        this.curLoginMode = LOGIN_MODE_CHECK_CODE;
        mView.changeLoginWay(curLoginMode);
    }
}
