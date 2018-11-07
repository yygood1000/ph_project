package com.topjet.common.common.modle.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.common.modle.params.GetPayForInfoParams;
import com.topjet.common.common.modle.response.GetPayForInfoResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tsj-004 on 2017/7/25.
 * 用户相关
 */

public interface PayForCommandAPI {

    // 货主支付信息
    String GET_PAY_FOR_INFO = "wallet.getamountofinformationofandroid";
    @POST("order-service/wallet/getamountofinformationofandroid")
    Observable<BaseResponse<GetPayForInfoResponse>> getPayForInfo(@Body CommonParams<GetPayForInfoParams> commonParams);

}
