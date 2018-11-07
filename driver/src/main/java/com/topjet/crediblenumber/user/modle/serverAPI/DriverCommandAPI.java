package com.topjet.crediblenumber.user.modle.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.user.modle.response.SignResponse;
import com.topjet.crediblenumber.user.modle.params.GetCallRecordsParams;
import com.topjet.crediblenumber.user.modle.response.GetCollLogListResponse;
import com.topjet.common.user.modle.response.UserCenterParameterResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tsj-004 on 2017/7/25.
 * 司机特有
 */

public interface DriverCommandAPI {
    // 司机-通话记录列表获取
    String GET_CALL_PHONE_INFO = "usercenter.callrecords";
    @POST("order-service/usercenter/callrecords")
    Observable<BaseResponse<GetCollLogListResponse>> getCallPhoneInfo(@Body CommonParams<GetCallRecordsParams> commonParams);

    // 司机-清空通话记录
    String CLEAR_CALL_PHONE_INFO = "userorder.callempty";
    @POST("user-service/userorder/callempty")
    Observable<BaseResponse<Object>> clearCallPhoneInfo(@Body CommonParams commonParams);

    // 个人中心  签到
    String SIGN = "userpublic.signin";
    @POST("user-service/userpublic/signin")
    Observable<BaseResponse<SignResponse>> signin(@Body CommonParams commonParams);

    // 个人中心-参数
    String USER_CENTRE_PARAMETER = "userpublic.usercentreparameterdriver";
    @POST("user-service/userpublic/usercentreparameterdriver")
    Observable<BaseResponse<UserCenterParameterResponse>> getUserCentreParameter(@Body CommonParams commonParams);
}