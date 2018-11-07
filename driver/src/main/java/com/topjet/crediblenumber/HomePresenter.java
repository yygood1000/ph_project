package com.topjet.crediblenumber;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.topjet.common.adv.ProcessAnnouncementData;
import com.topjet.common.adv.modle.params.GetAnnouncementParams;
import com.topjet.common.adv.modle.params.GetAnnouncementUrlParams;
import com.topjet.common.adv.modle.response.GetActivityUrlResponse;
import com.topjet.common.adv.modle.response.GetAnnouncementResponse;
import com.topjet.common.adv.modle.response.GetBannerDataResponse;
import com.topjet.common.adv.modle.response.GetHomeH5UrlResponse;
import com.topjet.common.adv.modle.serverAPI.AdvCommand;
import com.topjet.common.adv.modle.serverAPI.AdvCommandAPI;
import com.topjet.common.base.controller.BackGroundController;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseFragmentApiPresenter;
import com.topjet.common.base.view.fragment.RxFragment;
import com.topjet.common.common.modle.params.GetScrollBtnsParams;
import com.topjet.common.common.modle.response.GetScrollBtnsResponse;
import com.topjet.common.common.modle.response.GetSwitchKeyResponse;
import com.topjet.common.common.modle.serverAPI.HomeCommand;
import com.topjet.common.common.view.activity.SimpleWebViewActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.controller.DownloadChangeObserver;
import com.topjet.common.jpush.JPushHelper;
import com.topjet.common.resource.dict.ScrollButtonsDataDict;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.SystemUtils;

import static com.topjet.common.config.CConstants.SHIPPER_PACKAGE_NAME;

/**
 * Created by yy on 2017/8/14.
 * 首页Presenter
 */

class HomePresenter extends BaseFragmentApiPresenter<HomeView, HomeCommand> {
    private ProcessAnnouncementData.OnAnnouncementClikcListener announcementClickListener;
    private AutoDialog dialog = null;

    HomePresenter(HomeView mView, Context mContext, RxFragment mFragment, HomeCommand mApiCommand) {
        super(mView, mContext, mFragment, mApiCommand);
    }
    /*===============================滑动按钮组相关代码块=================================*/

    /**
     * 请求服务端 获取按钮组信息
     */
    void getScrollBtns() {
        GetScrollBtnsParams params = new GetScrollBtnsParams(CPersisData.getScrollButtonsReourceVersion(CPersisData
                .SP_KEY_SCROLL_BUTTONS_VERSION));
        mApiCommand.getScrollBtns(params, new ObserverOnResultListener<GetScrollBtnsResponse>() {
            @Override
            public void onResult(GetScrollBtnsResponse response) {
                if (response != null && response.getList() != null && response.getList().size() > 0) {
                    // 有新版本号，并且不等于现有版本号 并且在活动时间内才显示
                    if (StringUtils.isNotBlank(response.getVersion())) {
                        // 更新SP 中的滑动按钮组版本号
                        CPersisData.setScrollButtonsReourceVersion(CPersisData.SP_KEY_SCROLL_BUTTONS_VERSION, response.getVersion());
                        // 更新SP 中的滑动按钮组Json串
                        CPersisData.setScrollBtnsJsons(new Gson().toJson(response.getList()));
                        // 更新内存中的按钮组数据集合
                        ScrollButtonsDataDict.updataScrollBtnsListData(response.getList());
                        // 更新本地的刷新显示UI
                        mView.showScrollBtns();
                    }
                }
            }
        });

    }
    /*===============================滑动按钮组相关代码块===============================*/
    /*===============================广告相关===============================*/

    /**
     * 获取Banner轮播广告数据
     */

    void getBannerData() {
        new AdvCommand(AdvCommandAPI.class, mActivity).getBannerData(new ObserverOnResultListener<GetBannerDataResponse>() {
            @Override
            public void onResult(GetBannerDataResponse response) {
                mView.setBannerData(response);
            }
        });
    }

    /**
     * 获取首页H5上滑
     */
    void getHomeH5Url() {
        new AdvCommand(AdvCommandAPI.class, mActivity).getHomeH5Url(new ObserverOnResultListener<GetHomeH5UrlResponse>() {
            @Override
            public void onResult(GetHomeH5UrlResponse response) {
                mView.loadHomeWebView(response.getH5_url());
            }
        });
    }

    /**
     * 进行定位，定位后获取公告
     */
    void doLocatAndGetHomeAnnouncement() {
        // 进行定位，定位后获取经纬度
        getLocation();
    }

    /**
     * 定位获取当前位置
     */
    private void getLocation() {
        LocationUtils.location(mContext, new LocationUtils.OnLocationListener() {
            @Override
            public void onLocated(AMapLocation aMapLocation) {
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {// 定位成功
                    // 获取首页公告
                    getHomeAnnouncement(aMapLocation.getLatitude() + "", aMapLocation.getLongitude() + "");
                } else {
                    getHomeAnnouncement("", "");
                }
            }

            @Override
            public void onLocationPermissionfaild() {
            }
        });
    }

    /**
     * 获取首页公告
     */
    private void getHomeAnnouncement(String lat, String lng) {
        // 进行定位，定位后获取经纬度
        GetAnnouncementParams params = new GetAnnouncementParams(lat, lng);
        // 请求获取公告信息
        new AdvCommand(AdvCommandAPI.class, mActivity)
                .getHomeAnnouncement(params, new ObserverOnResultListener<GetAnnouncementResponse>() {
                    @Override
                    public void onResult(GetAnnouncementResponse response) {
                        // 对返回的公告数据进行2次处理
                        if (mActivity != null && mContext != null) {
                            mView.showAnnouncement(ProcessAnnouncementData.getAnnouncementViews(mContext,
                                    response, announcementClickListener));
                        }
                    }
                });
    }

    /**
     * 获取公告连接
     */
    void getHomeAnnouncementUrl(String id) {
        GetAnnouncementUrlParams params = new GetAnnouncementUrlParams(id);

        new AdvCommand(AdvCommandAPI.class, mActivity).getHomeAnnouncementUrl(params, new
                ObserverOnResultListener<GetActivityUrlResponse>() {
                    @Override
                    public void onResult(GetActivityUrlResponse response) {
                        if (StringUtils.isNotBlank(response.getRedirect_url())) {
                            SimpleWebViewActivity.turnToSimpleWebViewActivity(mActivity, response.getRedirect_url(),
                                    response.getTitle());
                        }
                    }
                });
    }

    /**
     * 设置公告栏点击事件
     */
    void setAnnouncementClickListener(ProcessAnnouncementData.OnAnnouncementClikcListener
                                              announcementClickListener) {
        this.announcementClickListener = announcementClickListener;
    }

    /*===============================广告相关===============================*/
    /*===============================APP切换===============================*/

    /**
     * APP切换
     */
    void getSwitchKey() {
        // 切换到货主版
        final String otherOnePackageName = SHIPPER_PACKAGE_NAME;
        Logger.d("oye", "getSwitchKey 切换app " + SHIPPER_PACKAGE_NAME);
        mApiCommand.getSwitchKey(otherOnePackageName, new ObserverOnResultListener<GetSwitchKeyResponse>() {
            @Override
            public void onResult(final GetSwitchKeyResponse response) {
                // app已安装，跳转到该app
                if (SystemUtils.isPackageExisted(otherOnePackageName)) {
                    String uri = "shipperapp://openpage?actionAndroid="
                            + otherOnePackageName + ".MainActivity&param2=" + response.getSwitch_key()
                            + "&param3=" + CMemoryData.getUserMobile();
                    JPushHelper.getInstance().jumpUri(mActivity, uri);
                } else {
                    // app未安装，提示是否下载
                    dialog = new AutoDialog(mContext);
                    dialog.setTitle("560交运配货");
                    dialog.setTitleBold();
                    dialog.setTitleBottomPadding(0);
                    dialog.setContent("您还未安装560交运配货-发货版APP，建议您立即下载安装");
                    dialog.setRightText("立即下载");
                    dialog.setLeftText("稍后下载");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
                        @Override
                        public void onLeftClick() {
                            dialog.toggleShow();
                        }

                        @Override
                        public void onRightClick() {
                            dialog.toggleShow();
                            // 开始下载
                            DownloadChangeObserver downloadChangeObserver = new DownloadChangeObserver
                                    (mContext, null);
                            new BackGroundController(mContext).downloadApk(response.getLoad_url(),
                                    downloadChangeObserver);
                        }
                    });
                    dialog.toggleShow();
                }
            }
        });
    }
   /*===============================APP切换===============================*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
