package com.topjet.crediblenumber.user.modle.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.crediblenumber.user.modle.params.UploadUsualCityInCenterParams;
import com.topjet.crediblenumber.user.modle.params.UploadUsualCityParams;
import com.topjet.crediblenumber.user.modle.response.GetUsualCityListResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tsj-004 on 2017/7/25.
 * 常跑城市 相关
 */

public interface UsualCityCommandAPI {

    //首页更新司机常跑城市
    String UPLOAD_USUAL_CITY =  "truck.addbusinessline";
    @POST("truck-service/truck/addbusinessline")
    Observable<BaseResponse<Object>> uploadUsualCity(@Body CommonParams<UploadUsualCityParams> commonParams);


    //获取常跑路线列表
    String GET_USUAL_CITY_LIST =  "truck.businesslinelist";
    @POST("truck-service/truck/businesslinelist")
    Observable<BaseResponse<GetUsualCityListResponse>> getUsualCityList(@Body CommonParams commonParams);

    // 常跑路线-个人中心列表
    String GET_USUAL_CITY_LIST_IN_CENTER = "truck.businesslinecentrelist";
    @POST("truck-service/truck/businesslinecentrelist")
    Observable<BaseResponse<GetUsualCityListResponse>> getUsualCityListInCenter(@Body CommonParams commonParams);

    // 常跑路线-提交
    String UPDATA_USUAL_CITY_LIST_IN_CENTER = "truck.businesslinecentre";
    @POST("truck-service/truck/businesslinecentre")
    Observable<BaseResponse<Object>> updataUsualCityListInCenter(@Body CommonParams<UploadUsualCityInCenterParams> commonParams);
}
