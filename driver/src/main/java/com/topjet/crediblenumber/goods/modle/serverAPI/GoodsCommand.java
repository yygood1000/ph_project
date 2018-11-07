package com.topjet.crediblenumber.goods.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.crediblenumber.goods.modle.params.AlterQuotedPriceParams;
import com.topjet.crediblenumber.goods.modle.params.AroundGoodsListParams;
import com.topjet.crediblenumber.goods.modle.params.AroundGoodsMapParams;
import com.topjet.crediblenumber.goods.modle.params.BidGoodsParams;
import com.topjet.crediblenumber.goods.modle.params.GetBidedPersonCountParams;
import com.topjet.crediblenumber.goods.modle.params.GetEconomicParams;
import com.topjet.crediblenumber.goods.modle.params.GetListenOrderParams;
import com.topjet.crediblenumber.goods.modle.params.SearchGoodsListParams;
import com.topjet.crediblenumber.goods.modle.params.SmartSearchGoodsListParams;
import com.topjet.crediblenumber.goods.modle.response.AroundGoodsListResponse;
import com.topjet.crediblenumber.goods.modle.response.AroundGoodsMapResponse;
import com.topjet.crediblenumber.goods.modle.response.BidOrderAlterResponse;
import com.topjet.crediblenumber.goods.modle.response.GetBidedPersonCountResponse;
import com.topjet.crediblenumber.goods.modle.response.GetEconomicListResponse;
import com.topjet.crediblenumber.goods.modle.response.GetIsEconomicResponse;
import com.topjet.crediblenumber.goods.modle.response.GetListenSettingResponse;
import com.topjet.crediblenumber.goods.modle.response.GetSubscribeRouteCountResponse;
import com.topjet.crediblenumber.goods.modle.response.SearchGoodsResponse;
import com.topjet.crediblenumber.goods.modle.response.SmartSearchGoodsResponse;
import com.topjet.crediblenumber.order.modle.bean.ListenOrderCheckBoxStatus;

/**
 * Created by tsj-004 on 2017/7/25.
 * 货源相关
 */

public class GoodsCommand extends BaseCommand<GoodsCommandAPI> {

    public GoodsCommand(Class<GoodsCommandAPI> c, MvpActivity activity) {
        super(c, activity);
    }

    public GoodsCommand(Class<GoodsCommandAPI> c) {
        super(c);
    }

    /**
     * 司机-附近货源列表页面
     */
    public void getAroundGoodsList(AroundGoodsListParams params,
                                   ObserverOnNextListener<AroundGoodsListResponse> listener) {
        mCommonParams = getParams(GoodsCommandAPI.GET_AROUND_GOODS_LIST, params);
        handleOnNextObserver(mApiService.getAroundGoodsList(mCommonParams), listener,false);
    }

    /**
     * 司机-附近货源 地图列表 页面
     */
    public void getAroundGoodsMapList(AroundGoodsListParams params,
                                      ObserverOnNextListener<AroundGoodsListResponse> listener) {
        mCommonParams = getParams(GoodsCommandAPI.GET_AROUND_GOODS_MAP_LIST, params);
        handleOnNextObserver(mApiService.getAroundGoodsMapList(mCommonParams), listener, false);
    }

    /**
     * 司机-附近货源(地图)
     */
    public void getAroundGoodsMap(AroundGoodsMapParams params,
                                  ObserverOnResultListener<AroundGoodsMapResponse> listener) {
        mCommonParams = getParams(GoodsCommandAPI.GET_AROUNDGOODS_MAP, params);
        handleOnResultObserver(mApiService.getAroundGoodsMap(mCommonParams), listener);
    }

    /**
     * 司机-接单
     */
    public void bidGoods(BidGoodsParams params,
                         ObserverOnResultListener<BidOrderAlterResponse> listener) {
        mCommonParams = getParams(GoodsCommandAPI.BID_GOODS, params);
        handleOnResultObserverNotActivity(mApiService.bidGoods(mCommonParams), listener);
    }

    /**
     * 司机-修改报价
     */
    public void aleatQuotedPrice(AlterQuotedPriceParams params,
                                 ObserverOnResultListener<BidOrderAlterResponse> listener) {
        mCommonParams = getParams(GoodsCommandAPI.ALEAT_QUOTED_PRICE, params);
        handleOnResultObserverNotActivity(mApiService.aleatQuotedPrice(mCommonParams), listener);
    }

    /**
     * 司机-智能找货
     */
    public void smartSearchGoods(SmartSearchGoodsListParams params,
                                 ObserverOnNextListener<SmartSearchGoodsResponse> listener) {

        mCommonParams = getParams(GoodsCommandAPI.SMART_SEARCH_GOODS, params);
        handleOnNextObserver(mApiService.smartSearchGoods(mCommonParams), listener, false);
    }

    /**
     * 司机-查找货源
     */
    public void searchGoods(SearchGoodsListParams params,
                            ObserverOnNextListener<SearchGoodsResponse> listener) {
        mCommonParams = getParams(GoodsCommandAPI.SEARCH_GOODS, params);
        handleOnNextObserver(mApiService.searchGoods(mCommonParams), listener, false);
    }

    /**
     * 获取听单设置
     */
    public void getListenSetting(ObserverOnNextListener<GetListenSettingResponse> listener) {
        mCommonParams = getParams(GoodsCommandAPI.GET_LISTEN_SETTING);
        handleOnNextObserver(mApiService.getListenSetting(mCommonParams), listener);
    }

    /**
     * 设置听单设置
     */
    public void setListenSetting(GetListenSettingResponse params,
                                 ObserverOnResultListener<Object> listener) {

        mCommonParams = getParams(GoodsCommandAPI.SET_LISTEN_SETTING, params);
        handleOnResultObserver(mApiService.setListenSetting(mCommonParams), listener);
    }

    /**
     * 司机-获取听单开关状态
     */
    public void getCheckBoxStatus(ObserverOnResultListener<ListenOrderCheckBoxStatus> listener) {
        mCommonParams = getParams(GoodsCommandAPI.GET_CHECKBOX_STATUS);
        handleOnResultObserver(mApiService.getCheckBoxStatus(mCommonParams), listener);
    }

    /**
     * 司机-设置听单开关状态
     */
    public void setCheckBoxStatus(ListenOrderCheckBoxStatus params,
                                  ObserverOnNextListener<Object> listener) {
        mCommonParams = getParams(GoodsCommandAPI.SET_CHECKBOX_STATUS, params);
        handleOnNextObserverNotActivity(mApiService.setCheckBoxStatus(mCommonParams), listener);
    }


    /**
     * 听单列表
     */
    public void getListenList(GetListenOrderParams commonParams,
                              ObserverOnResultListener<GoodsListData> listener) {
        mCommonParams = getParams(GoodsCommandAPI.GET_LISTEN_LIST, commonParams);
        handleOnResultObserverNotActivity(mApiService.getListenList(mCommonParams), listener);
    }


    /**
     * 订阅路线新增货源总数
     */
    public void getSubscribeGoodsCount(ObserverOnResultListener<GetSubscribeRouteCountResponse> listener) {
        mCommonParams = getParams(GoodsCommandAPI.GET_SUBSCRIBE_GOODS_COUNT);
        handleOnResultObserver(mApiService.getSubscribeGoodsCount(mCommonParams), listener, false);
    }


    /**
     * 司机-查询该货源的有效报价数，报价/修改报价页面使用
     */
    public void getBidedPersonCount(GetBidedPersonCountParams params,
                                    ObserverOnResultListener<GetBidedPersonCountResponse> listener) {
        mCommonParams = getParams(GoodsCommandAPI.GET_BIDED_PERSON_COUNT, params);
        handleOnResultObserverNotActivity(mApiService.getBidedPersonCount(mCommonParams), listener);
    }

    /**
     * 司机-是否有货运经济人
     */
    public void getIsEconomic(GetEconomicParams params,
                              ObserverOnResultListener<GetIsEconomicResponse> listener) {
        mCommonParams = getParams(GoodsCommandAPI.GET_IS_ECONOMIC, params);
        handleOnResultObserverNotActivity(mApiService.getIsEconomic(mCommonParams), listener);
    }

    /**
     * 司机-查询货运经济人列表
     */
    public void getEconomicList(GetEconomicParams params,
                                ObserverOnNextListener<GetEconomicListResponse> listener) {
        mCommonParams = getParams(GoodsCommandAPI.GET_IS_ECONOMIC, params);
        handleOnNextObserverNotActivity(mApiService.getEconomicList(mCommonParams), listener);
    }


}
