package com.topjet.crediblenumber;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.topjet.common.adv.modle.bean.BannerBean;
import com.topjet.common.adv.modle.params.GetRegularActivityParams;
import com.topjet.common.adv.modle.params.GetRegularActivityUrlParams;
import com.topjet.common.adv.modle.response.GetActivityUrlResponse;
import com.topjet.common.adv.modle.response.GetRegularActivityResponse;
import com.topjet.common.adv.modle.serverAPI.AdvCommand;
import com.topjet.common.adv.modle.serverAPI.AdvCommandAPI;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.bean.UsualCityBean;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.common.modle.event.NoviceBootDialogCloseEvent;
import com.topjet.common.common.modle.params.GetTabLayoutBtnsParams;
import com.topjet.common.common.modle.response.GetTabLayoutBtnsResponse;
import com.topjet.common.common.modle.response.GetUserInfoAtHomeResponse;
import com.topjet.common.common.modle.serverAPI.HomeCommand;
import com.topjet.common.common.service.FixedCycleLocationService;
import com.topjet.common.common.view.activity.NoviceBootDialogActivity;
import com.topjet.common.common.view.activity.SimpleWebViewActivity;
import com.topjet.common.common.view.dialog.CitySelectPopupWindow;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.resource.bean.AreaInfo;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.RxHelper;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.crediblenumber.goods.modle.event.ChangeListenOrderStatusEvent;
import com.topjet.crediblenumber.goods.presenter.ListenOrderController;
import com.topjet.crediblenumber.goods.service.FloatViewService;
import com.topjet.crediblenumber.goods.service.ListenOrderService;
import com.topjet.crediblenumber.order.modle.bean.ListenOrderCheckBoxStatus;
import com.topjet.crediblenumber.user.modle.params.UploadUsualCityParams;
import com.topjet.crediblenumber.user.modle.serverAPI.UsualCityCommand;
import com.topjet.crediblenumber.user.modle.serverAPI.UsualCityCommandAPI;
import com.topjet.crediblenumber.user.view.dialog.UsualCityDialog;

import java.util.ArrayList;
import java.util.Calendar;

import ezy.assist.compat.SettingsCompat;
import io.reactivex.functions.Consumer;

/**
 * Created by yy on 2017/7/29.
 * <p>
 * ManActivity 的Presenter
 */

public class MainPresenter extends BaseApiPresenter<MainView, HomeCommand> {
    private ArrayList<UsualCityBean> usualCityList = new ArrayList<>();// 常跑城市集合，该集合初始应该只有一个默认常跑城市（不是定位城市就是默认的上海）
    private UsualCityDialog usualCityDialog;
    // 添加新的常跑城市
    private int ADD_NEW_USUAL_CITY = 1;
    // 修改已有的常跑城市
    private int ALTER_USUAL_CITY = 2;
    // 城市选择框标记 ADD_NEW_USUAL_CITY：添加新的常跑城市 ALTER_USUAL_CITY：修改已有的常跑城市
    private int flag;

    private String adCode;// 司机当前位置

    private int flagLocat;//定位类型标签

    private int FLAG_LOCAT_ACTIVITY = 1;// 定时福袋引起的定位
    private int FLAG_LOCAT_USUAL_CITY = 2;// 设置常跑城市引起的定位

    private GetRegularActivityResponse regularActResponse;

    MainPresenter(MainView mView, Context mContext, HomeCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 展示所有需要展示的弹窗，弹出顺序逻辑在这里进行判断.
     * 1.新手引导
     * 2.常跑城市
     * 3.首页弹窗广告
     */
    void showAllDialog() {
        // 未展示过新手引导
        if (!CPersisData.getIsShowedNoviceBoot()) {
            // 显示新手引导页
            mActivity.turnToActivity(NoviceBootDialogActivity.class);
            // 设置展示标记
            CPersisData.setIsShowedNoviceBoot();
        } else {
            // 新手引导页面已经展示，请求用户信息，进行常跑城市弹窗展示
            getTheUserParameter();
        }
    }

    /**
     * 获取司机用户信息
     * 注：该方法有两次调用，并且互斥。且该方法在进入首页是必定会被调用。
     * 1.新手引导未展示过，在新手引导页关闭时调用。
     * 2.新手引导已经展示，在showAllDialog()方法中直接判断执行。
     */
    private void getTheUserParameter() {
        mApiCommand.getTheUserParameter(false, new ObserverOnResultListener<GetUserInfoAtHomeResponse>() {
            @Override
            public void onResult(GetUserInfoAtHomeResponse getUserInfoAtHomeResponse) {
                CMemoryData.setUserBaseInfo(getUserInfoAtHomeResponse);
                // 常跑城市未设置，展示常跑城市弹窗
                if (!getUserInfoAtHomeResponse.getIs_exist()) {
                    initUsualCityDialog();// 初始化常跑城市框
                    flagLocat = FLAG_LOCAT_USUAL_CITY;
                    // 进行单次定位
                    getLocation();
                } else {
                    // 常跑城市已设置，请求首页弹窗广告信息
                    getHomeDialogData();
                }
                // 设置首页tab未读消息状态
                mView.showTabLayoutMessageCorner(getUserInfoAtHomeResponse.getUnread_sum());
            }
        });
    }

    /*===================================常跑城市设置相关代码====================================*/

    /**
     * 定位获取当前位置
     */
    private void getLocation() {
        if (flagLocat == FLAG_LOCAT_USUAL_CITY) {
            LocationUtils.location(mContext, new LocationUtils.OnLocationListener() {
                @Override
                public void onLocated(AMapLocation aMapLocation) {
                    if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {// 定位成功
                        adCode = aMapLocation.getAdCode();
                        usualCityList.add(new UsualCityBean(adCode, AreaDataDict.getFullCityNameByLocation
                                (aMapLocation)));
                    } else {
                        usualCityList.add(new UsualCityBean(AreaDataDict.DEFAULT_CITY_CODE, AreaDataDict
                                .DEFAULT_CITY_NAME));
                    }
                    // 通知界面显示常跑城市Dialog
                    usualCityDialog.toggleShow();
                }

                @Override
                public void onLocationPermissionfaild() {
                }
            });
        } else if (flagLocat == FLAG_LOCAT_ACTIVITY) {
            LocationUtils.location(mContext, new LocationUtils.OnLocationListener() {
                @Override
                public void onLocated(AMapLocation aMapLocation) {
                    if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {// 定位成功
                        // 请求定时福袋数据
                        getRegularActivityData(aMapLocation.getAdCode());
                    }
                }

                @Override
                public void onLocationPermissionfaild() {
                }
            });
        }
    }

    /**
     * 初始化长跑城市Dialog
     */
    private void initUsualCityDialog() {
        usualCityDialog = new UsualCityDialog(mContext);
        usualCityDialog.setListDatas(usualCityList);
        // 设置常跑城市Dialog 中所有的点击事件
        usualCityDialog.setOnUsualCityDialogListener(new UsualCityDialog.OnUsualCityDialogListener() {
            // 修改常跑城市
            @Override
            public void onItemClick(View view) {
                // 弹出城市选择列表
                flag = ALTER_USUAL_CITY;
                new CitySelectPopupWindow(TAG).showCitySelectPopupWindow(mActivity, view, false, true, true);
            }

            // 删除常跑城市
            @Override
            public void onIconDeleteClick(int position) {
                usualCityDialog.deleteUsualCityDatas(position);
            }

            // 确认提交常跑城市
            @Override
            public void onClickConfirm(ArrayList<UsualCityBean> list) {
                // 点击确认 上传集合
                uploadUsualCity();
            }

            // 添加常跑城市
            @Override
            public void onClickAddCity(View view) {
                // 最多添加8个常跑城市
                if (usualCityList.size() < 8) {
                    // 弹出城市选择列表
                    flag = ADD_NEW_USUAL_CITY;
                    new CitySelectPopupWindow(TAG).showCitySelectPopupWindow(mActivity, view, false, true, true);
                } else {
                    // 最多允许添加8个城市
                    mView.showToast(ResourceUtils.getString(R.string.most_add_eigth_cities));
                }
            }
        });
    }

    /**
     * 添加常跑城市
     */
    @Subscribe
    public void onEvent(AreaSelectedData data) {
        if (data.getTag().equals(TAG)) {
            AreaInfo areaInfo = data.getAreaInfo();
            if (flag == ADD_NEW_USUAL_CITY) {
                // 新增常跑城市
                usualCityDialog.appendNewUsualCityDatas(new UsualCityBean(areaInfo.getLastCityId(), areaInfo
                        .getFullCityName()));
            } else {
                // 修改常跑城市
                usualCityDialog.updataNewUsualCityDatas(new UsualCityBean(areaInfo.getLastCityId(), areaInfo
                        .getFullCityName()));
            }
        }
    }

    /**
     * 新手引导页关闭事件，在这里进行常跑城市弹窗
     */
    @Subscribe
    public void onEvent(NoviceBootDialogCloseEvent event) {
        if (event.isSuccess()) {
            // 新手引导页面关闭，获取用户信息，进行常跑城市弹窗展示
            getTheUserParameter();
        }
    }

    /**
     * 上传常跑城市数据
     */
    private void uploadUsualCity() {
        UploadUsualCityParams usualCityParams = new UploadUsualCityParams();
        String cityCode;
        for (int i = 0; i < usualCityList.size(); i++) {
            cityCode = usualCityList.get(i).getBusinessLineCitycode();
            switch (i) {
                case 0:
                    usualCityParams.setBusinessLineCityCode1(cityCode);
                    break;
                case 1:
                    usualCityParams.setBusinessLineCityCode2(cityCode);
                    break;
                case 2:
                    usualCityParams.setBusinessLineCityCode3(cityCode);
                    break;
                case 3:
                    usualCityParams.setBusinessLineCityCode4(cityCode);
                    break;
                case 4:
                    usualCityParams.setBusinessLineCityCode5(cityCode);
                    break;
                case 5:
                    usualCityParams.setBusinessLineCityCode6(cityCode);
                    break;
                case 6:
                    usualCityParams.setBusinessLineCityCode7(cityCode);
                    break;
                case 7:
                    usualCityParams.setBusinessLineCityCode8(cityCode);
                    break;
            }
        }
        new UsualCityCommand(UsualCityCommandAPI.class, mActivity).uploadUsualCity(usualCityParams, new
                ObserverOnResultListener<Object>() {
                    @Override
                    public void onResult(Object o) {
                        usualCityDialog.toggleShow();
                        Toaster.showShortToast(ResourceUtils.getString(R.string.upload_succeed));
                        // 上传常跑城市成功，请求首页弹窗广告信息，进行首页弹窗广告展示
                        getHomeDialogData();
                    }
                });

    }

    /*===================================常跑城市设置相关代码====================================*/
    /*===================================后台定位相关代码====================================*/

    /**
     * 开启定位服务，定时上传定位信息
     */
    void startDoFixedCycleLocation() {
        RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .compose(RxHelper.<Boolean>rxSchedulerHelper())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            mContext.startService(new Intent(mContext, FixedCycleLocationService.class));
                        } else {
                        }
                    }
                });
    }

    /*===================================后台定位相关代码====================================*/
    /*===================================听单相关代码====================================*/

    /**
     * 获取听单播报开关状态
     */
    void getListenOrderStatus() {
        // 获取听单播报开关状态
        new ListenOrderController().getCheckBoxStatus(mActivity, TAG);
    }

    /**
     * 获取听单播报状态
     */
    @Subscribe
    public void onEvent(ListenOrderCheckBoxStatus status) {
        if (status.getTag().equals(TAG)) {
            startTwoService(status);
        }
    }

    /**
     * 开启听单相关两个服务
     */
    private void startTwoService(ListenOrderCheckBoxStatus status) {
        String sta = status.getStatus();
        // 0:关闭听单 1:开启听单
        if (sta.equals("0")) {
            // 发出通知关闭听单播报
            BusManager.getBus().post(new ChangeListenOrderStatusEvent(ChangeListenOrderStatusEvent
                    .CLOSE_LISTEN_ORDER));
            CMemoryData.setIsOrderOpen(false);
        } else if (sta.equals("1")) {
            // 开启听单服务（包含网络请求和播报）
            mContext.startService(new Intent(mContext, ListenOrderService.class));
        }
        // 发出通知修改checkbox勾选状态
        BusManager.getBus().post(new ChangeListenOrderStatusEvent(ChangeListenOrderStatusEvent
                .CHANGE_CHECKBOX_STATUS));
        // 发出通知修改floatView图标状态
        BusManager.getBus().post(new ChangeListenOrderStatusEvent(ChangeListenOrderStatusEvent
                .CHANGE_FLOATVIEW_STATUS));

        String isShow = CPersisData.getFloatViewStatus();
        if ("false".equals(isShow)) {
            // 开启悬浮窗服务（可控制语音是否播报）
            mContext.stopService(new Intent(mContext, FloatViewService.class));
        } else {
            // 检测悬浮窗是否授权
            boolean canDrawOverlays = SettingsCompat.canDrawOverlays(mContext);
            if (canDrawOverlays) {
                // 开启悬浮窗服务（可控制语音是否播报）
                mContext.startService(new Intent(mContext, FloatViewService.class));
            }
        }
    }

    /*===================================听单相关代码====================================*/
    /*===================================活动/广告相关代码====================================*/

    /**
     * 获取定时活动信息
     */
    void locatToGetRegularData() {
        flagLocat = FLAG_LOCAT_ACTIVITY;
        getLocation();
    }

    /**
     * 获取定时活动信息
     */
    private void getRegularActivityData(String adCode) {
        new AdvCommand(AdvCommandAPI.class, mActivity).getRegularActivityData(new GetRegularActivityParams(adCode),
                new ObserverOnResultListener<GetRegularActivityResponse>() {
                    @Override
                    public void onResult(GetRegularActivityResponse response) {
                        regularActResponse = response;
                        if (StringUtils.isNotBlank(response.getPicture_url()) &&
                                StringUtils.isNotBlank(response.getPicture_key())) {
                            mView.showRegularActivityIcon(response.getPicture_url(), response.getPicture_key());
                        }
                    }
                });
    }

    /**
     * 点击定时福袋，获取活动链接
     */
    void getRegularActivityUrl() {
        if (regularActResponse != null && StringUtils.isNotBlank(regularActResponse.getActivity_id())) {
            GetRegularActivityUrlParams params = new GetRegularActivityUrlParams(regularActResponse.getActivity_id());
            new AdvCommand(AdvCommandAPI.class, mActivity).getRegularActivityUrl(params,
                    new ObserverOnResultListener<GetActivityUrlResponse>() {
                        @Override
                        public void onResult(GetActivityUrlResponse response) {
                            // 该字段是活动是否失效，失效不显示图标，有效进行跳转
                            if (response.getIs_effective()) {
                                mView.hideRegularActivityIcon();
                            } else {
                                SimpleWebViewActivity.turnToSimpleWebViewActivity(mActivity, response.getRedirect_url(),
                                        regularActResponse.getWeb_title());
                            }
                        }
                    });
        }
    }

    /**
     * 获取首页弹窗信息,每日一次
     * 注：该方法在两处会被调用
     * 1.常跑城市已经设置，在getTheUserParameter()方法中直接判断执行。
     * 2.常跑城市未设置，在常跑城市Dialog设置成功关闭时执行。
     */
    private void getHomeDialogData() {
        //获得保存的天数，如果没有记录就赋值为-1表示第一次执行
        /*
         * -1表明是第一次执行
         * day != curDay表示不是同一天
         * 保存当前天数
         */
        int day = CPersisData.getHomeDialogFlay();
        int curDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        // 是不同一天不执行操作
        if ((day == -1 || day != curDay)) {
            new AdvCommand(AdvCommandAPI.class, mActivity).getHomeDialogData(new ObserverOnResultListener<BannerBean>
                    () {
                @Override
                public void onResult(BannerBean bannerBean) {
                    if (StringUtils.isNotBlank(bannerBean.getPicture_url())) {
                        mView.showHomeAdvDialog(bannerBean);
                    }
                }
            });
            CPersisData.setHomeDialogFlay(curDay);
        }
    }

    /**
     * 获取下次启动页展示时的广告链接
     */
    void getSplashActitvityAdvUrl() {
        new AdvCommand(AdvCommandAPI.class, mActivity).getSplashActitvityAdvUrl(new ObserverOnResultListener<BannerBean>() {
            @Override
            public void onResult(BannerBean bannerBean) {
                if (StringUtils.isNotBlank(bannerBean.getPicture_url()) && StringUtils.isNotBlank(bannerBean
                        .getPicture_key())) {
                    Logger.i("oye", "save splash adv info" + bannerBean.toString());
                    CPersisData.setSplashAdvInfo(bannerBean);
                } else {
                    Logger.i("oye", "clear splash adv info" + bannerBean.toString());
                    CPersisData.setSplashAdvInfo(new BannerBean());
                }
            }
        });
    }

    /*===================================活动/广告相关代码====================================*/

    /**
     * 请求服务端 获取首页-下方导航
     */
    void getTabLayoutBtns() {
        Logger.d("mTabLayoutBtnsResponse  00 ");
        GetTabLayoutBtnsParams params = new GetTabLayoutBtnsParams(CPersisData.getTablayoutButtonsReourceVersion(CPersisData
                .SP_KEY_TABLAYOUT_BUTTONS_VERSION));
        mApiCommand.getTabLayoutBtns(params, new ObserverOnResultListener<GetTabLayoutBtnsResponse>() {
            @Override
            public void onResult(GetTabLayoutBtnsResponse response) {
                // 有更新
                Logger.d("mTabLayoutBtnsResponse  11 "+response.toString());
                if (StringUtils.isNotBlank(response.getVersion())) {
                    Logger.d("mTabLayoutBtnsResponse 22 "+response.toString());
                    // 更新SP 中的滑动按钮组版本号
                    CPersisData.setTablayoutButtonsReourceVersion(CPersisData.SP_KEY_TABLAYOUT_BUTTONS_VERSION, response.getVersion());
                    // 更新SP 中的滑动按钮组Json串
                    CPersisData.setTabLayoutBtnsInfo(response);
                    // 更新本地的刷新显示UI
                    mView.showTabLayoutBtns();
                }
            }
        });
    }
}

