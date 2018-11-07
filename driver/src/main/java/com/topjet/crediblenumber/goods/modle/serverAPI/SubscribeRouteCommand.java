package com.topjet.crediblenumber.goods.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.crediblenumber.goods.modle.params.AddSubscribeRouteParams;
import com.topjet.crediblenumber.goods.modle.params.DeleteSubscribeRouteParams;
import com.topjet.crediblenumber.goods.modle.params.SubscribeRouteGoodsParams;
import com.topjet.crediblenumber.goods.modle.params.UpdataGoodsQueryTimeParams;
import com.topjet.crediblenumber.goods.modle.response.SubscribeRouteGoodsListResponse;
import com.topjet.crediblenumber.goods.modle.response.SubscribeRouteListResponse;

/**
 * Created by tsj-004 on 2017/7/25.
 * 订阅路线相关
 */

public class SubscribeRouteCommand extends BaseCommand<SubscribeRouteCommandAPI> {

    public SubscribeRouteCommand(Class c, MvpActivity activity) {
        super(c, activity);
    }

    /**
     * 订阅路线列表
     */
    public void getSubscribeRouteList(ObserverOnNextListener<SubscribeRouteListResponse> listener) {
        mCommonParams = getParams(SubscribeRouteCommandAPI.GET_SUBSCRIBE_ROUTE_LIST);
        handleOnNextObserver(mApiService.getSubscribeRouteList(mCommonParams), listener,false);

    }

    /**
     * 删除订阅路线列表
     */
    public void deleteSubscribeRouteList(DeleteSubscribeRouteParams params,
                                         ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(SubscribeRouteCommandAPI.DELETE_SUBSCRIBE_ROUTE_LIST,params);
        handleOnResultObserver(mApiService.deleteSubscribeRouteList(mCommonParams), listener);
    }

    /**
     * 添加订阅路线列表
     */
    public void addSubscribeRouteList(AddSubscribeRouteParams params,
                                      ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(SubscribeRouteCommandAPI.ADD_SUBSCRIBE_ROUTE_LIST, params);
        handleOnResultObserver(mApiService.addSubscribeRouteList(mCommonParams), listener);
    }

    /**
     * 订阅路线货源列表
     */
    public void getSubscribeRouteGoodsList(SubscribeRouteGoodsParams params,
                                           ObserverOnResultListener<SubscribeRouteGoodsListResponse> listener) {

        mCommonParams = getParams(SubscribeRouteCommandAPI.GET_SUBSCRIBE_ROUTE_GOODS_LIST, params);
        handleOnResultObserver(mApiService.getSubscribeRouteGoodsList(mCommonParams), listener);
    }

    /**
     * 订阅全部路线货源列表
     */
    public void getAllSubscribeRouteGoodsList(SubscribeRouteGoodsParams params,
                                              ObserverOnResultListener<SubscribeRouteGoodsListResponse> listener) {
        mCommonParams = getParams(SubscribeRouteCommandAPI.GET_ALL_SUBSCRIBE_ROUTE_GOODS_LIST, params);
        handleOnResultObserver(mApiService.getAllSubscribeRouteGoodsList(mCommonParams), listener);
    }

    /**
     * 修改订阅路线的货源查询时间
     */
    public void updataSubscribeLineQueryTime(UpdataGoodsQueryTimeParams params,
                                             ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(SubscribeRouteCommandAPI.UPDATA_SUBSCRIBE_LINE_QUERY_TIME, params);
        handleOnResultObserver(mApiService.updataSubscribeLineQueryTime(mCommonParams), listener);
    }

}
