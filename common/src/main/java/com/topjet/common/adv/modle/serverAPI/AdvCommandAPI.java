package com.topjet.common.adv.modle.serverAPI;

import com.topjet.common.adv.modle.bean.BannerBean;
import com.topjet.common.adv.modle.params.GetAnnouncementUrlParams;
import com.topjet.common.adv.modle.params.GetRegularActivityParams;
import com.topjet.common.adv.modle.params.GetRegularActivityUrlParams;
import com.topjet.common.adv.modle.params.MarqueeParams;
import com.topjet.common.adv.modle.response.GetAnnouncementResponse;
import com.topjet.common.adv.modle.response.GetBannerDataResponse;
import com.topjet.common.adv.modle.response.GetGoodsListAdvResponse;
import com.topjet.common.adv.modle.response.GetHomeH5UrlResponse;
import com.topjet.common.adv.modle.response.GetMarqueeResponse;
import com.topjet.common.adv.modle.response.GetRegularActivityResponse;
import com.topjet.common.adv.modle.response.GetActivityUrlResponse;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by yy on 2017/7/25.
 * 广告
 */

public interface AdvCommandAPI {

    //货源列表广告
    String GET_GOODS_LIST_ADV = "advertising.goodslistadvertisng";
    @POST("resource-service/advertising/goodslistadvertisng")
    Observable<BaseResponse<GetGoodsListAdvResponse>> getGoodsListAdv(@Body CommonParams commonParams);

    //首页定时福袋活动请求
    String GET_REGULAR_ACTIVITY_DATA = "advertising.regularactivitiesicon";
    @POST("resource-service/advertising/regularactivitiesicon")
    Observable<BaseResponse<GetRegularActivityResponse>> getRegularActivityData(@Body CommonParams<GetRegularActivityParams> commonParams);

    //首页定时福袋活动请求
    String GET_REGULAR_ACTIVITY_URL = "advertising.regularactivities";
    @POST("resource-service/advertising/regularactivities")
    Observable<BaseResponse<GetActivityUrlResponse>> getRegularActivityUrl(@Body CommonParams<GetRegularActivityUrlParams> commonParams);

    //跑马灯广告
    String GET_MARQUEE_DATA = "advertising.marqueeadvertising";
    @POST("resource-service/advertising/marqueeadvertising")
    Observable<BaseResponse<GetMarqueeResponse>> getMarqueeAdvertisment(@Body CommonParams<MarqueeParams> commonParams);

    //首页轮播Banner
    String GET_BANNER_DATA = "advertising.shufflingofhome";
    @POST("resource-service/advertising/shufflingofhome")
    Observable<BaseResponse<GetBannerDataResponse>> getBannerData(@Body CommonParams commonParams);

    //首页H5上滑
    String GET_HOME_H5_URL = "advertising.slideofhome";
    @POST("resource-service/advertising/slideofhome")
    Observable<BaseResponse<GetHomeH5UrlResponse>> getHomeH5Url(@Body CommonParams commonParams);

    //首页弹窗
    String GET_HOME_DIALOG_DATA_URL = "advertising.popupwindowofhome";
    @POST("resource-service/advertising/popupwindowofhome")
    Observable<BaseResponse<BannerBean>> getHomeDialogData(@Body CommonParams commonParams);

    //首页公告
    String GET_ANNOUNCEMENT = "advertising.announcementofhome";
    @POST("resource-service/advertising/announcementofhome")
    Observable<BaseResponse<GetAnnouncementResponse>> getHomeAnnouncement(@Body CommonParams commonParams);

    //首页公告活动链接
    String GET_ANNOUNCEMENT_URL = "advertising.getannouncementurl";
    @POST("resource-service/advertising/getannouncementurl")
    Observable<BaseResponse<GetActivityUrlResponse>> getHomeAnnouncementUrl(@Body CommonParams<GetAnnouncementUrlParams> commonParams);

    //启动页广告
    String GET_SPLASH_ACTITVITY_ADV_URL = "advertising.announcementofstartpage";
    @POST("resource-service/advertising/announcementofstartpage")
    Observable<BaseResponse<BannerBean>> getSplashActitvityAdvUrl(@Body CommonParams commonParams);

}
