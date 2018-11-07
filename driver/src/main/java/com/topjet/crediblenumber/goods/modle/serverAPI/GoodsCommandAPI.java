package com.topjet.crediblenumber.goods.modle.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.crediblenumber.goods.modle.params.AlterQuotedPriceParams;
import com.topjet.crediblenumber.goods.modle.params.AroundGoodsListParams;
import com.topjet.crediblenumber.goods.modle.params.AroundGoodsMapParams;
import com.topjet.crediblenumber.goods.modle.params.BidGoodsParams;
import com.topjet.crediblenumber.goods.modle.params.GetBidedPersonCountParams;
import com.topjet.crediblenumber.goods.modle.params.GetEconomicParams;
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

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by yy on 2017/7/25.
 * 货源相关
 */

public interface GoodsCommandAPI {
    // 附近货源（地图列表 弹出列表）
    String GET_AROUND_GOODS_MAP_LIST = "drivergoods.getneargoodstolist";
    @POST("order-service/drivergoods/getneargoodstolist")
    Observable<BaseResponse<AroundGoodsListResponse>> getAroundGoodsMapList(@Body CommonParams<AroundGoodsListParams>
                                                                                 commonParams);

    // 司机-附近货源(列表)
    String GET_AROUND_GOODS_LIST = "drivergoods.getneargoodstotable";
    @POST("order-service/drivergoods/getneargoodstotable")
    Observable<BaseResponse<AroundGoodsListResponse>> getAroundGoodsList(@Body CommonParams<AroundGoodsListParams>
                                                                                 commonParams);
    // 附近货源（地图）
    String GET_AROUNDGOODS_MAP = "drivergoods.getneargoodstomap";
    @POST("order-service/drivergoods/getneargoodstomap")
    Observable<BaseResponse<AroundGoodsMapResponse>> getAroundGoodsMap(@Body CommonParams<AroundGoodsMapParams>
                                                                               commonParams);

    // 智能找货
    String SMART_SEARCH_GOODS = "drivergoods.searchsmartfindgoods";
    @POST("order-service/drivergoods/searchsmartfindgoods")
    Observable<BaseResponse<SmartSearchGoodsResponse>> smartSearchGoods(@Body CommonParams<SmartSearchGoodsListParams>
                                                                                commonParams);

    // 查找货源
    String SEARCH_GOODS = "drivergoods.findgoodslistbydriver";
    @POST("order-service/drivergoods/findgoodslistbydriver")
    Observable<BaseResponse<SearchGoodsResponse>> searchGoods(@Body CommonParams<SearchGoodsListParams>
                                                                      commonParams);

    // 报价接口
    String BID_GOODS = "pregoods.receivinggoods";
    @POST("order-service/pregoods/receivinggoods")
    Observable<BaseResponse<BidOrderAlterResponse>> bidGoods(@Body CommonParams<BidGoodsParams>
                                                                     commonParams);

    // 修改报价接口
    String ALEAT_QUOTED_PRICE = "pregoods.updatepregoods";
    @POST("order-service/pregoods/updatepregoods")
    Observable<BaseResponse<BidOrderAlterResponse>> aleatQuotedPrice(@Body CommonParams<AlterQuotedPriceParams>
                                                                             commonParams);

    // 司机-获取听单设置，返回出发地、目的地-----获取、获取、获取
    String GET_LISTEN_SETTING = "listengoods.getsetting";
    @POST("order-service/listengoods/getsetting")
    Observable<BaseResponse<GetListenSettingResponse>> getListenSetting(@Body CommonParams commonParams);

    // 司机-保存听单设置，设置出发地、目的地-----设置、设置、设置
    String SET_LISTEN_SETTING = "listengoods.savesetting";
    @POST("order-service/listengoods/savesetting")
    Observable<BaseResponse<Object>> setListenSetting(@Body CommonParams<GetListenSettingResponse> commonParams);

    // 司机-获取听单开关状态-----获取、获取、获取
    String GET_CHECKBOX_STATUS = "listengoods.getstatus";
    @POST("order-service/listengoods/getstatus")
    Observable<BaseResponse<ListenOrderCheckBoxStatus>> getCheckBoxStatus(@Body CommonParams commonParams);

    // 司机-获取听单开关状态-----设置、设置、设置
    String SET_CHECKBOX_STATUS = "listengoods.updatestatus";
    @POST("order-service/listengoods/updatestatus")
    Observable<BaseResponse<Object>> setCheckBoxStatus(@Body CommonParams<ListenOrderCheckBoxStatus> commonParams);

    // 听单列表
    String GET_LISTEN_LIST = "listengoods.list";
    @POST("order-service/listengoods/list")
    Observable<BaseResponse<GoodsListData>> getListenList(@Body CommonParams commonParams);

    //TODO 附近货源（地图 弹出列表）

    // 订阅路线新增货源总数
    String GET_SUBSCRIBE_GOODS_COUNT = "subscribeline.selectnewsubscribelinsum";
    @POST("order-service/subscribeline/selectnewsubscribelinsum")
    Observable<BaseResponse<GetSubscribeRouteCountResponse>> getSubscribeGoodsCount(@Body CommonParams commonParams);

    // 司机-查询该货源的有效报价数
    String GET_BIDED_PERSON_COUNT = "drivergoods.selectcountpregoodsvalid";
    @POST("order-service/drivergoods/selectcountpregoodsvalid")
    Observable<BaseResponse<GetBidedPersonCountResponse>> getBidedPersonCount(@Body CommonParams<GetBidedPersonCountParams> commonParams);

    // 司机-是否有货运经济人
    String GET_IS_ECONOMIC = "transport.iseconomic";
    @POST("order-service/transport/iseconomic")
    Observable<BaseResponse<GetIsEconomicResponse>> getIsEconomic(@Body CommonParams<GetEconomicParams> commonParams);

    // 司机-查询货运经济人列表
    String GET_ECONOMIC_LIST = "transport.economiclist";
    @POST("order-service/transport/economiclist")
    Observable<BaseResponse<GetEconomicListResponse>> getEconomicList(@Body CommonParams<GetEconomicParams> commonParams);


}
