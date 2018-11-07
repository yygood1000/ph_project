package com.topjet.crediblenumber;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.easeim.IMHelper;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.topjet.common.App;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.UserInfo;
import com.topjet.common.common.modle.extra.RouteExtra;
import com.topjet.common.common.modle.extra.SwitchExtra;
import com.topjet.common.common.modle.extra.TabIndex;
import com.topjet.common.common.presenter.SkipControllerWallet;
import com.topjet.common.common.service.FixedCycleLocationService;
import com.topjet.common.common.view.activity.CommentActivity;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.config.Config;
import com.topjet.common.config.RespectiveData;
import com.topjet.common.controller.FCHandler;
import com.topjet.common.jpush.ActivityLifecycleListener;
import com.topjet.common.order_detail.modle.extra.OrderExtra;
import com.topjet.common.resource.ResourceManager;
import com.topjet.common.user.modle.extra.PhoneExtra;
import com.topjet.common.user.view.activity.LoginActivity;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.PathHelper;
import com.topjet.common.utils.Toaster;
import com.topjet.crediblenumber.goods.service.FloatViewService;
import com.topjet.crediblenumber.goods.service.ListenOrderService;
import com.topjet.crediblenumber.goods.view.activity.ChangeTextSizeActivity;
import com.topjet.crediblenumber.order.view.activity.OrderDetailActivity;
import com.topjet.crediblenumber.order.view.map.RouteActivity;
import com.topjet.crediblenumber.user.view.activity.CarAuthenticationActivity;
import com.topjet.crediblenumber.user.view.activity.IdentityAuthenticationActivity;
import com.topjet.crediblenumber.user.view.activity.RealNameAuthenticationActivity;
import com.topjet.crediblenumber.user.view.activity.UserInfoActivity;
import com.topjet.wallet.utils.WalletCallBackUtils;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

public class

DriverApp extends MultiDexApplication {
    private static DriverApp sDriverApp;

    public static DriverApp getInstance() {
        return sDriverApp;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sDriverApp = this;
        initCommons();
        initBases();
        initResourcesData();
        SkipControllerWallet.initWallet(sDriverApp);
        initIM();
        initUmeng();
        initProvider();
        initJPush();

        // 讯飞语音初始化
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=533a8a57");

        // 注册activity生命周期的回调
        registerActivityLifecycleCallbacks(new ActivityLifecycleListener());

        // android 7.0系统解决拍照的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }

    }

    /**
     * 基础配置初始化
     */
    private void initBases() {
        Config.init();
        Toaster.init();
        FCHandler.init();
    }

    /**
     * 初始化资源文件
     */
    private void initResourcesData() {
        try {
            ResourceManager.initResourcesData(sDriverApp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置公共信息
     * 1.全局上下文
     * 2.设置司机/货主区别信息
     */
    private void initCommons() {
        App.init(sDriverApp);
        saveRespectiveData();
    }

    /**
     * 设置司机/货主区别信息
     * * 1.设置外部存储根文件夹名称
     * 2.设置 APP类型
     * 3.设置请求头的版本信息
     * 4.设置外部版本号
     * 5.首页地址
     * 6.登录页地址
     * 7.启动页地址
     * 8.实名认证页地址
     */
    private void saveRespectiveData() {
        PathHelper.APP_ROOT_NAME = PathHelper.APP_DRIVER_ROOT_NAME;// 设置外部存储根文件夹名称（560_Driver）
        CMemoryData.setDriver(true);// 设置 APP类型（司机版）
        RespectiveData.setRequestSource(CConstants.REQUEST_SOURCE_DRIVER);// 设置请求头的版本信息（1：司机版）
        RespectiveData.setOutVersion(CConstants.OUT_VERSION_DRIVER);// 设置外部版本号
        RespectiveData.setMainActivityClass(MainActivity.class);
        RespectiveData.setLoginActivityClass(LoginActivity.class);
        RespectiveData.setSplashActivityClass(SplashActivity.class);
        RespectiveData.setRealNameAuthenticationActivityClass(RealNameAuthenticationActivity.class);
    }

    /**
     * 初始极光推送
     */
    private void initJPush() {
        //初始化sdk
        JPushInterface.setDebugMode(true);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
        String token = JPushInterface.getRegistrationID(this);
        Logger.d("极光token " + token);
        CPersisData.setPushToken(token);
    }

    /**
     * 舒适化友盟统计配置
     * String appkey:官方申请的Appkeyd
     * String channel: 渠道号
     * EScenarioType eType: 场景模式，包含统计、游戏、统计盒子、游戏盒子
     * Boolean isCrashEnable: 可选初始化. 是否开启crash模式
     */
    private void initUmeng() {
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this,
                BuildConfig.UM_KEY,
                "driver",
                MobclickAgent.EScenarioType.E_UM_NORMAL,
                true));
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        // 从新统计页面间隔（app 切到后台或其他APP后，到返回app的间隔时间,30秒）
        MobclickAgent.setSessionContinueMillis(30000);
        //禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.enableEncrypt(false);//6.0.0版本及以后
        MobclickAgent.setDebugMode(true);//6.0.0版本及以后
    }

    /**
     * 初始化IM
     */
    private void initIM() {
        IMHelper.getInstance().init(this);
    }

    /**
     * 提供给common调用driver的api和页面
     */
    private void initProvider() {
        CommonProvider.getInstance().setJumpDriverProvider(new CommonProvider.JumpDriverProvider() {

            @Override
            public void jumpMain(MvpActivity activity, int tab, int orderTab) {
                activity.turnToActivity(MainActivity.class, new TabIndex(tab, orderTab));
            }

            @Override
            public void jumpMain(MvpActivity activity, int tab) {
                activity.turnToActivity(MainActivity.class, new TabIndex(tab));
            }

            @Override
            public void jumpCarAuthentication(MvpActivity activity, int type) {
                CarAuthenticationActivity.turnToRealNameActivity(activity, type);
            }

            @Override
            public void jumpIdentityAuthentication(MvpActivity activity) {
                activity.turnToActivity(IdentityAuthenticationActivity.class);
            }

            @Override
            public void jumpRealNameAuthentication(MvpActivity activity, int type) {
                RealNameAuthenticationActivity.turnToRealNameActivity(activity, type);
            }

            @Override
            public void jumpUserInfo(MvpActivity activity, UserInfo userInfo) {
                activity.turnToActivity(UserInfoActivity.class, userInfo);
            }

            @Override
            public void jumpUserInfo(MvpActivity activity, String mobile) {
                activity.turnToActivity(UserInfoActivity.class, new PhoneExtra(mobile));
            }

            @Override
            public void jumpOrderDetail(MvpActivity activity, String orderId) {
                OrderDetailActivity.toOrderDetail(activity, orderId);
            }

            @Override
            public void jumpOrderDetail(MvpActivity activity, String orderId, String showPickUp) {
                OrderDetailActivity.toOrderDetail(activity, orderId, showPickUp);
            }

            @Override
            public void jumpGoodsDetail(MvpActivity activity, String goodsId) {
                OrderDetailActivity.toGoodsDetail(activity, goodsId);
            }

            @Override
            public void jumpComment(MvpActivity activity, String orderId, String orderVersion) {
                CommentActivity.turnToCheckCommentActivity(activity, orderId, orderVersion);
            }

            @Override
            public void jumpGuide(MvpActivity activity, SwitchExtra extra) {
                activity.turnToActivity(SplashActivity.class, extra);
                activity.finishPage();
            }

            @Override
            public void jumpRoute(MvpActivity activity, RouteExtra extra) {
                RouteActivity.toRoute(activity, extra);
            }

            @Override
            public void jumpChangeSize(MvpActivity activity) {
                activity.turnToActivity(ChangeTextSizeActivity.class);
            }
        });

        CommonProvider.getInstance().setListenerOrderProvider(new CommonProvider.ListenerOrderProvider() {
            @Override
            public void stopFloatViewService(Context context) {
                CPersisData.setFloatViewStatus("false");
                context.stopService(new Intent(context, FloatViewService.class));    // 开启悬浮窗服务（可控制语音是否播报）
            }

            @Override
            public void stopListenOrderService(Context context) {
                context.stopService(new Intent(context, ListenOrderService.class));  // 停止听单服务（包含网络请求和播报）
            }

            @Override
            public void stopLocationService(Context context) {
                context.stopService(new Intent(context, FixedCycleLocationService.class));  // 停止定位
            }
        });

        WalletCallBackUtils.getInstance().setJumpWalletActivity(new WalletCallBackUtils.JumpWalletActivity() {
            @Override
            public void JumpActivity(Activity activity, String orderId) {
                OrderExtra extra = new OrderExtra(orderId, OrderExtra.ORDER_DETAIL);
                Bundle bundle = new Bundle();
                bundle.putSerializable(OrderExtra.getExtraName(), extra);
                Intent intent = new Intent(activity, OrderDetailActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
    }
}
