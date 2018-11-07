package com.topjet.crediblenumber;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.topjet.common.adv.modle.bean.BannerBean;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.model.Session;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.common.modle.extra.SwitchExtra;
import com.topjet.common.common.modle.serverAPI.SystemCommand;
import com.topjet.common.common.view.activity.GuideActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.user.modle.params.SwitchLoginParams;
import com.topjet.common.user.modle.response.SwitchLoginResponse;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.modle.serverAPI.UserCommandAPI;
import com.topjet.common.user.view.activity.LoginActivity;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.RxHelper;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.SystemUtils;
import com.topjet.common.utils.Toaster;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by yy on 2017/7/29.
 * 启动页Presenter
 */

class SplashPresenter extends BasePresenter<SplashView> {
    Disposable mTimeDsposable;
    private UserCommand mApiCommand = null;

    private String address = "";
    private String city_id = "";
    private boolean isSwitchLoginFinished = false;  // 是否切换app登录完成
    private boolean isCountDownFinished = false;    // 是否等待5秒结束
    private BannerBean splashAdvInfo;
    private boolean isSwitchLogin = false;      // 是否是切换app登录
    private SwitchExtra switchExtra = null;      // 切换app 传入数据

    SplashPresenter(SplashView mView, Context mContext) {
        super(mView, mContext);
        mApiCommand = new UserCommand(UserCommandAPI.class, mActivity);
        // 从SP中获取广告图片的信息
        splashAdvInfo = CPersisData.getSplashAdvInfo();
    }

    public BannerBean getSplashAdvInfo() {
        return splashAdvInfo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        switchExtra = (SwitchExtra) mActivity.getIntentExtra(SwitchExtra.getExtraName());
        if (switchExtra != null) {
            isSwitchLogin = true;
            mView.setSkipView(View.GONE);

        }
    }

    /**
     * 进入启动页就开始播放平移动画
     * 播放结束后进行广告图片的加载
     */
    void startTranslateAnim(ImageView ivLogo) {
        ivLogo.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int ivHeigth = ivLogo.getMeasuredHeight();// 获取LOGO 高度
        int screenHeigth = ScreenUtils.getScreenHeight(mContext);


        // 计算平移动画移动到底部的距离(屏幕高度 - logo高度 - 初始marginTop值)
        float statrtY = screenHeigth - ((screenHeigth + ivHeigth) / 2);

        ObjectAnimator animator = ObjectAnimator.ofFloat(ivLogo, "translationY", -statrtY, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //加载广告图
                setAdvImage();
            }
        });
        animator.setDuration(1000);
        animator.start();
    }

    /**
     * 设置广告图片
     */
    private void setAdvImage(){
        if (splashAdvInfo != null
                && StringUtils.isNotBlank(splashAdvInfo.getPicture_url())
                && StringUtils.isNotBlank(splashAdvInfo.getPicture_key())) {
            Logger.i("oye", "splash adv info is not empty" + splashAdvInfo.toString());
            if (splashAdvInfo.getPicture_url().endsWith(".gif")) {
                mView.loadSplashGif(splashAdvInfo);
            } else {
                mView.loadSplashImg(splashAdvInfo);
            }
        } else {
            Logger.i("oye", "splash adv info is empty");
            // 没有图片，则不倒计时，所以直接设置成已经倒计时结束
            isCountDownFinished = true;
            initPermissionAndGoMain(true);// 没有广告图片，进行权限获取，获取成功后自动跳转
        }
    }

    /**
     * 获取渐显动画动画
     */
    AlphaAnimation getAlphaAnim() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(2000);
        return alphaAnimation;
    }

    /**
     * 展示了广告，计时5秒后进行跳转
     */
    void startTheTime() {
        Logger.d("loadSplashImg startTheTime");
        Observable.timer(5, TimeUnit.SECONDS)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mTimeDsposable = disposable;
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        isCountDownFinished = true;
                        Logger.d("loadSplashImg subscribe");
                        if (!isSwitchLogin) {
                            skip();
                        } else {
                            turnToMain();
                        }
                    }
                });
    }

    /**
     * 初始化权限信息，申请了几个权限 走几次accept。如果使用request 则是只返回一次。
     * 该方法不管是否加载广告图都会执行
     *
     * @param isSkip 是否在获取权限后进行跳转
     */
    void initPermissionAndGoMain(final boolean isSkip) {
        RxPermissions rxPermissions = new RxPermissions(mActivity);
        Observable.timer(0, TimeUnit.SECONDS)
                .compose(rxPermissions.ensureEach(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ))
                .compose(RxHelper.<Permission>rxSchedulerHelper())
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        // 对读取手机信息权限做单独处理
                        Logger.d("Permission", "accept  " + permission.name + "  " + permission.granted);
                        if (!permission.granted && permission.name.equals(Manifest.permission.READ_PHONE_STATE)) {
                            // 读取手机信息权限被拒绝，停止计时，不跳转首页
                            disposePHONE_STATEPermissionResult(permission);
                            mTimeDsposable.dispose();
                            mView.setSkipView(View.GONE);
                        }
                    }
                }, new Consumer<Throwable>() {// 获取权限发生异常，必须得写，华为系统当用户拒绝后，第二次请求会直接报error
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.d("Permission", "onError" + throwable);
                    }
                }, new Action() {// 结束
                    @Override
                    public void run() throws Exception {
                        Logger.d("Permission", "initPermissionAndGoMain OnComplete");
                        // 是否是切换APP
                        if (isSwitchLogin) {
                            if (switchExtra != null) {
                                // 切换app登录
                                getSwitchLoginDataFromServer(switchExtra.getSwitchKey(), switchExtra.getMobile());
                            }
                        } else {
                            if (isSkip) {
                                skip();
                            }
                        }
                    }
                });
    }

    /**
     * 对读取手机信息权限做单独处理
     */
    private void disposePHONE_STATEPermissionResult(Permission permission) {
        if (permission.granted) {// 用户同意
        } else if (permission.shouldShowRequestPermissionRationale) {//
            // 用户拒绝，并且勾选了不在提示，MIUI系统默认勾选。在这里可以做自定义弹窗提示用户去设置里开启权限
            showPermissionDialog();
        } else {// 用户拒绝 ，但是没有勾选不在提示。
            showPermissionDialog();
        }
    }

    /**
     * 切换app登录
     */
    private void getSwitchLoginDataFromServer(final String switchKey, final String mobile) {
        LocationUtils.location(mActivity, onLocationListener);
        CMemoryData.setUserMobile(mobile);
        CPersisData.setUserMobile(mobile);

        new SystemCommand(mActivity).getSession(new ObserverOnNextListener<Session>() {
            @Override
            public void onNext(final Session session) {
                CMemoryData.setSessionId(session.getSession_id());

                SwitchLoginParams params = new SwitchLoginParams();
                params.setLogin_address(address);
                params.setLogin_city_id(city_id);
                params.setMsg_push_token(CPersisData.getPushToken(""));
                params.setSwitch_key(switchKey);
                mApiCommand.switchLogin(params, new ObserverOnNextListener<SwitchLoginResponse>() {
                    @Override
                    public void onNext(SwitchLoginResponse response) {
                        isSwitchLoginFinished = true;
                        CMemoryData.setUserId(response.getUser_id());
                        CPersisData.setUserName(response.getUser_name());
                        CPersisData.setUserId(response.getUser_id());
                        CPersisData.setUserVersion(response.getVersion());
                        turnToMain();
                    }

                    @Override
                    public void onError(String errorCode, String msg) {
                        Toaster.showLongToast(msg);
                        SystemUtils.killProcess();
                    }
                });
            }

            @Override
            public void onError(String errorCode, String msg) {
                Toaster.showLongToast(msg);
                SystemUtils.killProcess();
            }
        });
    }

    /**
     * 跳转首页
     */
    void skip() {
        Logger.d("loadSplashImg skip");
        if ((CPersisData.getIsLogin())) {
            Logger.d("loadSplashImg MainActivity");
            mView.turnToActivityWithFinish(MainActivity.class);// 跳转主页
        } else {
            if (CPersisData.getIsGuide()) {
                mView.turnToActivityWithFinish(GuideActivity.class);// 跳转装机引导页
            } else {
                mView.turnToActivityWithFinish(LoginActivity.class);// 跳转登录
            }
        }
    }

    /**
     * 跳转首页
     */
    private void turnToMain() {
        if (isCountDownFinished && isSwitchLoginFinished) {
            if (CPersisData.getIsGuide()) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isToMain", true);
                mView.turnToActivityWithFinish(GuideActivity.class, bundle);// 跳转装机引导页
            } else {
                CPersisData.setIsLogin(true);
                mView.turnToActivityWithFinish(MainActivity.class);// 跳转主页
            }
        }
    }

    private LocationUtils.OnLocationListener onLocationListener = new LocationUtils.OnLocationListener() {
        @Override
        public void onLocated(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                address = aMapLocation.getAddress();
                city_id = aMapLocation.getAdCode();
            }
        }

        @Override
        public void onLocationPermissionfaild() {

        }
    };


    /**
     * 显示权限提示
     */
    private void showPermissionDialog() {
        AutoDialog dialog = new AutoDialog(mContext);
        dialog.setContent("未去的您的手机信息使用权限，应用无法开启。请前往应用权限设置打开权限");
        dialog.setSingleText("去打开");
        dialog.setOnSingleConfirmListener(new AutoDialog.OnSingleConfirmListener() {
            @Override
            public void onClick() {
                startAppSettings();
            }
        });
        dialog.toggleShow();
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
        mActivity.startActivity(intent);
    }
}
