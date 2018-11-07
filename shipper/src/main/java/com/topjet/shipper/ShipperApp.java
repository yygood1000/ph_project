package com.topjet.shipper;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.easeim.IMHelper;
import com.squareup.leakcanary.LeakCanary;
import com.topjet.common.App;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.UserInfo;
import com.topjet.common.common.modle.extra.SwitchExtra;
import com.topjet.common.common.modle.extra.TabIndex;
import com.topjet.common.common.modle.params.TruckParams;
import com.topjet.common.common.presenter.SkipControllerWallet;
import com.topjet.common.common.view.activity.CommentActivity;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.config.Config;
import com.topjet.common.config.RespectiveData;
import com.topjet.common.contact.model.InviteTruckResponse;
import com.topjet.common.controller.FCHandler;
import com.topjet.common.jpush.ActivityLifecycleListener;
import com.topjet.common.order_detail.modle.extra.OrderExtra;
import com.topjet.common.resource.ResourceManager;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.user.modle.extra.PhoneExtra;
import com.topjet.common.user.view.activity.LoginActivity;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.PathHelper;
import com.topjet.common.utils.Toaster;
import com.topjet.shipper.deliver.view.activity.DeliverGoodsActivity;
import com.topjet.shipper.familiar_car.model.extra.RefindTruckExtra;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommand;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommandAPI;
import com.topjet.shipper.familiar_car.view.activity.FindTruckActivity;
import com.topjet.shipper.order.modle.extra.ShowOfferExtra;
import com.topjet.shipper.order.view.activity.OrderDetailActivity;
import com.topjet.shipper.order.view.activity.ShowOfferActivity;
import com.topjet.shipper.user.view.activity.IdentityAuthenticationActivity;
import com.topjet.shipper.user.view.activity.RealNameAuthenticationActivity;
import com.topjet.shipper.user.view.activity.UserInfoActivity;
import com.topjet.wallet.utils.WalletCallBackUtils;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;


public class ShipperApp extends MultiDexApplication {
    private static ShipperApp sShipperApp;

    public static ShipperApp getInstance() {
        return sShipperApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sShipperApp = this;
        initCommons();
        initBases();
        // 初始化城市选择器、车型车长各种配置
        initResourcesData();
        SkipControllerWallet.initWallet(sShipperApp);
        initUmeng();
        initLeakCanary();
        initProvider();
        initJPush();
        initIM();

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
     * 初始化IM
     */
    private void initIM() {
        IMHelper.getInstance().init(this);
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
     * 提供给common调用shipper的api和页面
     */
    private void initProvider() {
        CommonProvider.getInstance().setFamiliarCarProvider(new CommonProvider.FamiliarCarProvider() {
            @Override
            public void inviteTruck(MvpActivity activity, String mobile,
                                    ObserverOnResultListener<InviteTruckResponse> listener) {
                TruckParams params = new TruckParams();
                params.setMobile(mobile);
                new TruckCommand(TruckCommandAPI.class, activity).inviteTruck(params, listener);
            }

            @Override
            public void addOrDeleteCar(MvpActivity activity, String carId, int flag, ObserverOnResultListener<Object>
                    listener) {
                TruckParams params = new TruckParams();
                params.setTruck_id(carId);
                params.setFlag(flag + "");
                new TruckCommand(TruckCommandAPI.class, activity).addOrDeleteTruck(params, listener);
            }
        });

        CommonProvider.getInstance().setJumpShipperProvider(new CommonProvider.JumpShipperProvider() {
            @Override
            public void jumpMain(MvpActivity activity, int tab, int orderTab) {
                activity.turnToActivity(MainActivity.class, new TabIndex(tab, orderTab));
            }

            @Override
            public void jumpMain(MvpActivity activity, int tab, int orderTab, int orderState) {
                activity.turnToActivity(MainActivity.class, new TabIndex(tab, orderTab, orderState));
            }

            @Override
            public void jumpMain(MvpActivity activity, int tab) {
                activity.turnToActivity(MainActivity.class, new TabIndex(tab));
            }

            @Override
            public void jumpDeliverGoods(MvpActivity activity) {
                activity.turnToActivity(DeliverGoodsActivity.class);
            }

            @Override
            public void jumpToDeliverGoodsForEditAssigend(MvpActivity activity) {
                DeliverGoodsActivity.turnToDeliverGoodsForEditAssigned(activity);
            }

            @Override
            public void jumpToDeliverGoodsForEdit(MvpActivity activity, String goodsId) {
                DeliverGoodsActivity.turnToDeliverGoodsForEdit(activity, goodsId);
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
            public void jumpGoodsDetail(MvpActivity activity, String goodsId) {
                OrderDetailActivity.toGoodsDetail(activity, goodsId);
            }

            @Override
            public void jumpComment(MvpActivity activity, String orderId, String orderVersion) {
                CommentActivity.turnToCheckCommentActivity(activity, orderId, orderVersion);
            }

            @Override
            public void jumpShowOffer(MvpActivity activity, String goodsId, String goodsVersion, String payStyle) {
                ShowOfferExtra extra = new ShowOfferExtra();
                extra.setGoodsId(goodsId);
                extra.setGoodsVersion(goodsVersion);
                extra.setAhead(payStyle.equals("3")); // 是否提付部分运费
                ShowOfferActivity.toShowOffer(activity, extra);
            }

            @Override
            public void jumpRefindTruck(MvpActivity activity, String departCityId, String destinationCityId, String
                    goodsId) {
                //在内存中记录重新找车的货源的id
                CMemoryData.setTempGoodsId(goodsId);
                RefindTruckExtra extra = new RefindTruckExtra();
                extra.setDepartCityId(departCityId);
                extra.setDestinationCityId(destinationCityId);
                extra.setDepartCityName(AreaDataDict.replaceCityAndProvinceString(AreaDataDict.getCityItemByAdcode
                        (departCityId).getCityName()));
                extra.setDestinationCityName(AreaDataDict.replaceCityAndProvinceString(AreaDataDict.getCityItemByAdcode
                        (destinationCityId).getCityName()));
                extra.setRefund(true);
                activity.turnToActivity(FindTruckActivity.class, extra);
            }

            @Override
            public void jumpGuide(MvpActivity activity, SwitchExtra extra) {
                activity.turnToActivity(SplashActivity.class, extra);
                activity.finishPage();
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

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    /**
     * 舒适化友盟统计配置
     * String appkey:官方申请的Appkey
     * String channel: 渠道号
     * EScenarioType eType: 场景模式，包含统计、游戏、统计盒子、游戏盒子
     * Boolean isCrashEnable: 可选初始化. 是否开启crash模式
     */
    private void initUmeng() {
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this,
                BuildConfig.UM_KEY,
                "shipper",
                MobclickAgent.EScenarioType.E_UM_NORMAL,
                true));
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        // 从新统计页面间隔（app 切到后台或其他APP后，到返回app的间隔时间,30秒）
        MobclickAgent.setSessionContinueMillis(30000);
//        MobclickAgent.enableEncrypt(false);//6.0.0版本及以后
        //禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.setDebugMode(true);//6.0.0版本及以后
    }


    /**
     * 设置公共信息
     * 1.全局上下文
     */
    private void initCommons() {
        App.init(sShipperApp);
        saveRespectiveData();//设置司机/货主区别信息
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
     * 设置司机/货主区别信息
     * 1.设置外部存储根文件夹名称
     * 2.设置 APP类型
     * 3.设置请求头的版本信息
     * 4.设置外部版本号
     * 5.首页地址
     * 6.登录页地址
     * 7.启动页地址
     * 8.实名认证页地址
     */
    private void saveRespectiveData() {
        PathHelper.APP_ROOT_NAME = PathHelper.APP_SHIPPER_ROOT_NAME;// 设置外部存储根文件夹名称（560_Shipper）
        CMemoryData.setDriver(false);// 设置 APP类型（货主版）
        RespectiveData.setRequestSource(CConstants.REQUEST_SOURCE_SHIPPER);// 设置请求头的版本信息（1：司机版）
        RespectiveData.setOutVersion(CConstants.OUT_VERSION_SHIPPER);// 设置外部版本号
        RespectiveData.setMainActivityClass(MainActivity.class);
        RespectiveData.setLoginActivityClass(LoginActivity.class);
        RespectiveData.setSplashActivityClass(SplashActivity.class);
        RespectiveData.setRealNameAuthenticationActivityClass(RealNameAuthenticationActivity.class);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 初始化资源文件
     */
    private void initResourcesData() {
        try {
            ResourceManager.initResourcesData(sShipperApp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
