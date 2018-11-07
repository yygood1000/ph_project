package com.topjet.crediblenumber.user.modle.serverAPI;

import com.topjet.common.user.modle.response.UserCenterRealNameAuthenticationResponse;
import com.topjet.crediblenumber.user.modle.params.SaveTruckParams;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.crediblenumber.user.modle.params.UploadUsualCityParams;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tsj-004 on 2017/7/25.
 * 认证相关
 */

public interface AuthenticationCommandAPI {
    // 车辆认证（司机）
    String SAVE_TRUCK = "truck.usercentresavetruck";
    @POST("truck-service/truck/usercentresavetruck")
    Observable<BaseResponse<Object>> savetruck(@Body CommonParams<SaveTruckParams> commonParams);

    // 更新司机常跑城市
    @POST("truck-service/truck/addbusinessline")
    Observable<BaseResponse<Object>> uploadUsualCity(@Body CommonParams<UploadUsualCityParams> commonParams);

    // 获取车辆认证状态
    String QUERY_CAR_AUTHEN_STATUS = "truck.getusercentresavetruck";
    @POST("truck-service/truck/getusercentresavetruck")
    Observable<BaseResponse<SaveTruckParams>> queryCarAuthenStatus(@Body CommonParams commonParams);
}
