package com.topjet.shipper.deliver.modle.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.user.modle.params.PageNumParams;
import com.topjet.shipper.deliver.modle.params.OwnerGoodsParams;
import com.topjet.shipper.order.modle.response.AddGoodsReponse;
import com.topjet.shipper.order.modle.response.OftenGoodsListReponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by  yy on 2017/7/25.
 * 发货相关
 */

public interface DeliverCommandAPI {
    //货主-常发货源列表
    String OFTEN_GOODS_LIST = "ownergoods.oftengoodslist";
    @POST("order-service/ownergoods/oftengoodslist")
    Observable<BaseResponse<OftenGoodsListReponse>> oftenGoodsList(@Body CommonParams<PageNumParams> params);

    //货主-修改货源
    String UPDATE_GOODS = "ownergoods.updategoods";
    @POST("order-service/ownergoods/updategoods")
    Observable<BaseResponse<AddGoodsReponse>> updateGoods(@Body CommonParams<OwnerGoodsParams> params);

    //货主-修改定向货源
    String UPDATE_GOODS_ASSIGNED = "ownergoods.updategoodsassigned";
    @POST("order-service/ownergoods/updategoodsassigned")
    Observable<BaseResponse<AddGoodsReponse>> updateGoodsAssigned(@Body CommonParams<OwnerGoodsParams> params);

    //货主-发布货源
    String ADD_GOODS = "ownergoods.addgoods";
    @POST("order-service/ownergoods/addgoods")
    Observable<BaseResponse<AddGoodsReponse>> addGoods(@Body CommonParams<OwnerGoodsParams> params);

    // 获取 货主-货源信息
    String GET_PARAMS_FROM_SERVER_BY_ID = "publicgoods.getgoodsinfo";
    @POST("order-service/publicgoods/getgoodsinfo")
    Observable<BaseResponse<OwnerGoodsParams>> getParamsFromServerById(@Body CommonParams<OwnerGoodsParams> params);

    //货主-发布定向货源
    String ADD_GOODS_ASSIGNED = "ownergoods.addgoodsassigned";
    @POST("order-service/ownergoods/addgoodsassigned")
    Observable<BaseResponse<AddGoodsReponse>> addGoodsAssigned(@Body CommonParams<OwnerGoodsParams> params);
}
