package com.topjet.common.resource.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.resource.params.UpdataResourceParams;
import com.topjet.common.resource.response.CheckReourceResponse;
import com.topjet.common.resource.response.UpdataResourceResponse;
import com.topjet.common.resource.response.UpdataResourceUrlResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tsj-004 on 2017/7/25.
 * 首页相关
 */

public interface ResourceCommandAPI {

    // 校验资源文件更新
    String CHECK_RESOURCE = "resourceurl.check";
    @POST("resource-service/resourceurl/check") //resource-service/resourceurl/check
    Observable<BaseResponse<CheckReourceResponse>> checkResource(@Body CommonParams commonParams);

    // 获取城市文件
    String UPDATA_CITIES = "resourceurl.getcity";
    @POST("resource-service/resourceurl/getcity")
    Observable<BaseResponse<UpdataResourceUrlResponse>> updataCities(@Body CommonParams<UpdataResourceParams> commonParams);

    // 获取车型文件
    String UPDATA_TRUCK_TYPE = "resourceurl.gettrucktype";
    @POST("resource-service/resourceurl/gettrucktype")
    Observable<BaseResponse<UpdataResourceUrlResponse>> updataTruckType(@Body CommonParams<UpdataResourceParams> commonParams);

    // 获取车长文件
    String UPDATA_TRUCK_LENGTH = "resourceurl.gettrucklength";
    @POST("resource-service/resourceurl/gettrucklength")
    Observable<BaseResponse<UpdataResourceUrlResponse>> updataTruckLength(@Body CommonParams<UpdataResourceParams> commonParams);

    // 获取包装方式文件
    String UPDATA_PACKING_TYPE = "resourceurl.getpacktype";
    @POST("resource-service/resourceurl/getpacktype")
    Observable<BaseResponse<UpdataResourceUrlResponse>> updataPackingType(@Body CommonParams<UpdataResourceParams> commonParams);

    // 获取装卸方式文件
    String UPDATA_LOAD_TYPE = "resourceurl.getloadtype";
    @POST("resource-service/resourceurl/getloadtype")
    Observable<BaseResponse<UpdataResourceUrlResponse>> updataLoadType(@Body CommonParams<UpdataResourceParams> commonParams);

    // 获取货物类型文件
    String UPDATA_GOODS_TYPE = "resourceurl.getgoodsname";
    @POST("resource-service/resourceurl/getgoodsname")
    Observable<BaseResponse<UpdataResourceUrlResponse>> updataGoodsType(@Body CommonParams<UpdataResourceParams> commonParams);

    // 获取货物单位文件
    String UPDATA_GOODS_UNIT = "resourceurl.getgoodsunit";
    @POST("resource-service/resourceurl/getgoodsunit")
    Observable<BaseResponse<UpdataResourceUrlResponse>> updataGoodsUnit(@Body CommonParams<UpdataResourceParams> commonParams);

}
