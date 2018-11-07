package com.topjet.common.adv.modle.serverAPI;

import com.topjet.common.adv.modle.bean.BannerBean;
import com.topjet.common.adv.modle.params.GetAnnouncementParams;
import com.topjet.common.adv.modle.params.GetAnnouncementUrlParams;
import com.topjet.common.adv.modle.params.GetRegularActivityParams;
import com.topjet.common.adv.modle.params.GetRegularActivityUrlParams;
import com.topjet.common.adv.modle.params.MarqueeParams;
import com.topjet.common.adv.modle.response.GetActivityUrlResponse;
import com.topjet.common.adv.modle.response.GetAnnouncementResponse;
import com.topjet.common.adv.modle.response.GetBannerDataResponse;
import com.topjet.common.adv.modle.response.GetGoodsListAdvResponse;
import com.topjet.common.adv.modle.response.GetHomeH5UrlResponse;
import com.topjet.common.adv.modle.response.GetMarqueeResponse;
import com.topjet.common.adv.modle.response.GetRegularActivityResponse;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;

/**
 * Created by yy on 2017/7/25.
 * 活动/广告相关
 */
public class AdvCommand extends BaseCommand<AdvCommandAPI> {

    public AdvCommand(Class<AdvCommandAPI> c, MvpActivity activity) {
        super(c, activity);
    }

    public AdvCommand(MvpActivity activity) {
        super(AdvCommandAPI.class, activity);
    }

    /**
     * 找货页面，列表广告
     */
    public void getGoodsListAdv(ObserverOnResultListener<GetGoodsListAdvResponse> listener) {
        mCommonParams = getParams(AdvCommandAPI.GET_GOODS_LIST_ADV);
        handleOnResultObserver(mApiService.getGoodsListAdv(mCommonParams), listener, false);
    }

    /**
     * 首页定时福袋活动请求
     */
    public void getRegularActivityData(GetRegularActivityParams params,
                                       ObserverOnResultListener<GetRegularActivityResponse> listener) {
        mCommonParams = getParams(AdvCommandAPI.GET_REGULAR_ACTIVITY_DATA, params);
        handleOnResultObserver(mApiService.getRegularActivityData(mCommonParams), listener, false);
    }

    /**
     * 点击定时福袋，获取活动链接
     */
    public void getRegularActivityUrl(GetRegularActivityUrlParams params,
                                      ObserverOnResultListener<GetActivityUrlResponse> listener) {
        mCommonParams = getParams(AdvCommandAPI.GET_REGULAR_ACTIVITY_URL, params);
        handleOnResultObserver(mApiService.getRegularActivityUrl(mCommonParams), listener);
    }

    /**
     * 跑马灯广告
     */
    public void getMarqueeAdvertisment(MarqueeParams params,
                                       ObserverOnResultListener<GetMarqueeResponse> listener) {
        mCommonParams = getParams(AdvCommandAPI.GET_MARQUEE_DATA, params);
        handleOnResultObserver(mApiService.getMarqueeAdvertisment(mCommonParams), listener, false);
    }

    /**
     * 首页轮播Banner
     */
    public void getBannerData(ObserverOnResultListener<GetBannerDataResponse> listener) {
        mCommonParams = getParams(AdvCommandAPI.GET_BANNER_DATA);
        handleOnResultObserver(mApiService.getBannerData(mCommonParams), listener, false);
    }

    /**
     * 首页H5上滑
     */
    public void getHomeH5Url(ObserverOnResultListener<GetHomeH5UrlResponse> listener) {
        mCommonParams = getParams(AdvCommandAPI.GET_HOME_H5_URL);
        handleOnResultObserver(mApiService.getHomeH5Url(mCommonParams), listener, false);
    }

    /**
     * 首页弹窗
     */
    public void getHomeDialogData(ObserverOnResultListener<BannerBean> listener) {
        mCommonParams = getParams(AdvCommandAPI.GET_HOME_DIALOG_DATA_URL);
        handleOnResultObserver(mApiService.getHomeDialogData(mCommonParams), listener, false);
    }

    /**
     * 首页公告
     */
    public void getHomeAnnouncement(GetAnnouncementParams params, ObserverOnResultListener<GetAnnouncementResponse>
            listener) {
        mCommonParams = getParams(AdvCommandAPI.GET_ANNOUNCEMENT, params);
        handleOnResultObserver(mApiService.getHomeAnnouncement(mCommonParams), listener, false);
    }

    /**
     * 首页公告活动链接
     */
    public void getHomeAnnouncementUrl(GetAnnouncementUrlParams params, ObserverOnResultListener<GetActivityUrlResponse>
            listener) {
        mCommonParams = getParams(AdvCommandAPI.GET_ANNOUNCEMENT_URL, params);
        handleOnResultObserver(mApiService.getHomeAnnouncementUrl(mCommonParams), listener);
    }

    /**
     * 首页公告活动链接
     */
    public void getSplashActitvityAdvUrl(ObserverOnResultListener<BannerBean> listener) {
        mCommonParams = getParams(AdvCommandAPI.GET_SPLASH_ACTITVITY_ADV_URL);
        handleOnResultObserver(mApiService.getSplashActitvityAdvUrl(mCommonParams), listener, false);
    }
}


