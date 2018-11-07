package com.topjet.common.common.modle.serverAPI;

import com.topjet.common.base.model.Session;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.common.modle.params.PhoneParams;
import com.topjet.common.common.modle.response.UserInfoGoodsResponse;
import com.topjet.common.common.modle.response.UserInfoResponse;
import com.topjet.common.common.modle.response.UserInfoTruckResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * creator: zhulunjun
 * time:    2017/11/13
 * describe: 诚信查询 ，用户信息
 */

public interface UserInfoCommandAPI {

    //诚信查询-司机 查询货主
    String QUERY_SHIPPER_INFO = "userpublic.queryintegrityofdriver";
    @POST("user-service/userpublic/queryintegrityofdriver")
    Observable<BaseResponse<UserInfoResponse>> queryShipperInfo(@Body CommonParams commonParams);

    //诚信查询-货主 查询 司机
    String QUERY_DRIVER_INFO = "userpublic.queryintegrityofowner";
    @POST("user-service/userpublic/queryintegrityofowner")
    Observable<BaseResponse<UserInfoResponse>> queryDriverInfo(@Body CommonParams commonParams);

    //诚信查询-司机-货源信息
    String QUERY_SHIPPER_INFO_GOODS_LIST = "userpublic.queryintegrityofdriverpaging";
    @POST("user-service/userpublic/queryintegrityofdriverpaging")
    Observable<BaseResponse<UserInfoGoodsResponse>> queryShipperInfoGoodsList(@Body CommonParams commonParams);

    //诚信查询-货主-车辆信息
    String QUERY_DRIVER_INFO_TRUCK_LIST = "userpublic.queryintegrityofownerpaging";
    @POST("user-service/userpublic/queryintegrityofownerpaging")
    Observable<BaseResponse<UserInfoTruckResponse>> queryDriverInfoTruckList(@Body CommonParams commonParams);
}
