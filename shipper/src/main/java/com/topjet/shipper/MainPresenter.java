package com.topjet.shipper;

import android.content.Context;
import android.content.Intent;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.topjet.common.adv.modle.bean.BannerBean;
import com.topjet.common.adv.modle.params.GetRegularActivityParams;
import com.topjet.common.adv.modle.params.GetRegularActivityUrlParams;
import com.topjet.common.adv.modle.response.GetActivityUrlResponse;
import com.topjet.common.adv.modle.response.GetRegularActivityResponse;
import com.topjet.common.adv.modle.serverAPI.AdvCommand;
import com.topjet.common.adv.modle.serverAPI.AdvCommandAPI;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.event.NoviceBootDialogCloseEvent;
import com.topjet.common.common.modle.params.GetTabLayoutBtnsParams;
import com.topjet.common.common.modle.response.GetTabLayoutBtnsResponse;
import com.topjet.common.common.modle.response.GetUserInfoAtHomeResponse;
import com.topjet.common.common.modle.response.GoodsRefreshInfoResponse;
import com.topjet.common.common.modle.serverAPI.HomeCommand;
import com.topjet.common.common.service.FixedCycleLocationService;
import com.topjet.common.common.view.activity.NoviceBootDialogActivity;
import com.topjet.common.common.view.activity.SimpleWebViewActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.RxHelper;
import com.topjet.common.utils.StringUtils;

import java.util.Calendar;

import io.reactivex.functions.Consumer;

/**
 * Created by yy on 2017/7/29.
 * <p>
 */

class MainPresenter extends BaseApiPresenter<MainView, HomeCommand> {
    private GetRegularActivityResponse regularActivityResponse;

    MainPresenter(MainView mView, Context mContext, HomeCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 获取用户信息
     */
    void getTheUserParameter() {
        mApiCommand.getTheUserParameter(false, new ObserverOnResultListener<GetUserInfoAtHomeResponse>() {
            @Override
            public void onResult(GetUserInfoAtHomeResponse getUserInfoAtHomeResponse) {
                CMemoryData.setUserBaseInfo(getUserInfoAtHomeResponse);
                mView.showTabLayoutMessageCorner(getUserInfoAtHomeResponse.getUnread_sum());
            }
        });
    }

    /**
     * 展示所有需要展示的弹窗，弹出顺序逻辑在这里进行判断.
     * 1.新手引导
     * 2.首页弹窗广告
     */
    void showAllDialog() {
        // 未展示过新手引导
        if (!CPersisData.getIsShowedNoviceBoot()) {
            Logger.i("oye", "展示新手引导页");
            // 显示新手引导页
            mActivity.turnToActivity(NoviceBootDialogActivity.class);
            // 设置展示标记
            CPersisData.setIsShowedNoviceBoot();
        } else {
            Logger.i("oye", "新手引导页面已经展示，请求首页弹窗广告信息 ");
            // 获取首页弹窗广告信息
            getHomeDialogData();
        }
    }

    /**
     * 获取定时活动信息
     */
    void getRegularActivityData() {
        new AdvCommand(AdvCommandAPI.class, mActivity).getRegularActivityData(new GetRegularActivityParams(),
                new ObserverOnResultListener<GetRegularActivityResponse>() {
                    @Override
                    public void onResult(GetRegularActivityResponse response) {
                        Logger.d("福袋信息 " + response.getPicture_url() + " " + response.toString());
                        regularActivityResponse = response;
                        mView.showRegularActivityIcon(response.getPicture_url(), response.getPicture_key());
                    }
                });
    }

    /**
     * 点击定时福袋，获取活动链接
     */
    void getRegularActivityUrl() {
        new AdvCommand(AdvCommandAPI.class, mActivity).getRegularActivityUrl(
                new GetRegularActivityUrlParams(regularActivityResponse.getActivity_id()),
                new ObserverOnResultListener<GetActivityUrlResponse>() {
                    @Override
                    public void onResult(GetActivityUrlResponse response) {
                        // 该字段是活动是否失效，失效不显示图标，有效进行跳转
                        if (response.getIs_effective()) {
                            mView.hideRegularActivityIcon();
                        } else {
                            SimpleWebViewActivity.turnToSimpleWebViewActivity(mActivity, response.getRedirect_url(),
                                    regularActivityResponse.getWeb_title());
                        }
                    }
                });
    }

    /**
     * 获取启动页广告信息
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

    /*===================================后台定位相关代码====================================*/

    /**
     * 开启后台定位服务
     */
    void startDoFixedCycleLocation() {
        RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions.request(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                .compose(RxHelper.<Boolean>rxSchedulerHelper())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Logger.i("oye","start location service");
                            mContext.startService(new Intent(mContext, FixedCycleLocationService.class));
                        }
                    }
                });
    }

    /*===================================后台定位相关代码====================================*/

    /**
     * 新手引导页关闭事件，在这里进行常跑城市弹窗
     */
    @Subscribe
    public void onEvent(NoviceBootDialogCloseEvent event) {
        if (event.isSuccess()) {
            Logger.i("oye", "新手引导页面关闭，请求首页弹窗广告信息 ");
            // 获取首页弹窗广告信息
            getHomeDialogData();
        }
    }

    /**
     * 获取首页弹窗信息
     */
    private void getHomeDialogData() {
        //获得保存的天数，如果没有记录就赋值为-1表示第一次执行
        /*
         * -1表明是第一次执行
         * day != curDay表示不是同一天
         * 保存当前天数
         */
        int day = CPersisData.getHomeDialogFlay();
        final int curDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        // 是不同一天不执行操作
        if ((day == -1 || day != curDay)) {
            new AdvCommand(AdvCommandAPI.class, mActivity).getHomeDialogData(new ObserverOnResultListener<BannerBean>
                    () {
                @Override
                public void onResult(BannerBean bannerBean) {
                    if (StringUtils.isNotBlank(bannerBean.getPicture_url())) {
                        mView.showHomeAdvDialog(bannerBean);
                        CPersisData.setHomeDialogFlay(curDay);
                    }
                }
            });

        }
    }

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

    /**
     * 请求服务端
     * 货主-获取货源刷新信息
     */
    void getGoodsRefreshInfo() {
        mApiCommand.getGoodsRefreshInfo(new ObserverOnResultListener<GoodsRefreshInfoResponse>() {
            @Override
            public void onResult(GoodsRefreshInfoResponse response) {
                if (response != null) {
                    String count = response.getRefresh_count();
                    String minute = response.getRefresh_minute();
                    if (StringUtils.isNotBlank(count)) {
                        CPersisData.setSpKeyGoodsrefreshinfoRefreshCount(count);
                    }
                    if (StringUtils.isNotBlank(minute)) {
                        CPersisData.setSpKeyGoodsrefreshinfoRefreshMinute(minute);
                    }
                }
            }
        });
    }
}
