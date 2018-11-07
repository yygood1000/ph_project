package com.topjet.crediblenumber.goods.modle.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.crediblenumber.goods.modle.response.SubscribeRouteGoodsListResponse;
import com.topjet.crediblenumber.goods.modle.response.SubscribeRouteListResponse;
import com.topjet.crediblenumber.goods.modle.params.AddSubscribeRouteParams;
import com.topjet.crediblenumber.goods.modle.params.DeleteSubscribeRouteParams;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tsj-004 on 2017/7/25.
 * 订阅路线 相关
 */

public interface SubscribeRouteCommandAPI {
    //订阅路线列表
    String GET_SUBSCRIBE_ROUTE_LIST = "subscribeline.selectsubscribelinelist";
    @POST("order-service/subscribeline/selectsubscribelinelist")
    Observable<BaseResponse<SubscribeRouteListResponse>> getSubscribeRouteList(@Body CommonParams commonParams);

    //删除路线列表
    String DELETE_SUBSCRIBE_ROUTE_LIST = "subscribeline.deletesubscribeline";
    @POST("order-service/subscribeline/deletesubscribeline")
    Observable<BaseResponse<Object>> deleteSubscribeRouteList(@Body CommonParams<DeleteSubscribeRouteParams>
                                                                      commonParams);

    //添加路线列表
    String ADD_SUBSCRIBE_ROUTE_LIST = "subscribeline.insertsubscribeline";
    @POST("order-service/subscribeline/insertsubscribeline")
    Observable<BaseResponse<Object>> addSubscribeRouteList(@Body CommonParams<AddSubscribeRouteParams> commonParams);

    //订阅路线货源列表
    String GET_SUBSCRIBE_ROUTE_GOODS_LIST = "subscribeline.selectsubscribelineinfo";
    @POST("order-service/subscribeline/selectsubscribelineinfo")
    Observable<BaseResponse<SubscribeRouteGoodsListResponse>> getSubscribeRouteGoodsList(@Body CommonParams
                                                                                                 commonParams);

    // 全部订阅路线货源列表
    String GET_ALL_SUBSCRIBE_ROUTE_GOODS_LIST = "subscribeline.selectsubscribelineallgoods";
    @POST("order-service/subscribeline/selectsubscribelineallgoods")
    Observable<BaseResponse<SubscribeRouteGoodsListResponse>> getAllSubscribeRouteGoodsList(@Body CommonParams
                                                                                                    commonParams);

    // 修改订阅路线的货源查询时间
    String UPDATA_SUBSCRIBE_LINE_QUERY_TIME = "subscribeline.updatesubscribelinequerytime";
    @POST("order-service/subscribeline/updatesubscribelinequerytime")
    Observable<BaseResponse<Object>> updataSubscribeLineQueryTime(@Body CommonParams commonParams);
}
