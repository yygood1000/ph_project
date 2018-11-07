package com.topjet.common.user.modle.serverAPI;

import com.topjet.common.user.modle.params.RealNameAuthenticationParams;
import com.topjet.common.user.modle.params.TypeAuthDriverParams;
import com.topjet.common.user.modle.params.TypeAuthOwnerParams;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.user.modle.response.RealNameAuthenticationResponse;
import com.topjet.common.user.modle.response.UserCenterRealNameAuthenticationResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tsj-004 on 2017/7/25.
 * 认证相关
 */

public interface UserCertificationCommandAPI {
    //实名认证
    String AUTH = "usercertification.auth";
    @POST("user-service/usercertification/auth")
    Observable<BaseResponse<Object>> auth(@Body CommonParams<RealNameAuthenticationParams> commonParams);

    // 个人中心，实名认证
    String USER_CENTRE_AUTH = "userpublic.usercentreauth";
    @POST("user-service/userpublic/usercentreauth")
    Observable<BaseResponse<RealNameAuthenticationResponse>> userCentreAuth(@Body CommonParams<RealNameAuthenticationParams> commonParams);

    // 获取实名认证状态
    String QUERY_REAL_NAME_AUTHEN_STATUS = "userpublic.queryusercentrerealnameauthentication";
    @POST("user-service/userpublic/queryusercentrerealnameauthentication")
    Observable<BaseResponse<UserCenterRealNameAuthenticationResponse>> queryRealNameAuthenStatus(@Body CommonParams commonParams);

    // 获取身份认证状态（司机）
    String QUERY_DRIVER_IDENTITY_AUTHEN_STATUS = "userpublic.getusercentretypeauthdriver";
    @POST("user-service/userpublic/getusercentretypeauthdriver")
    Observable<BaseResponse<TypeAuthDriverParams>> queryDriverIdentityAuthenStatus(@Body CommonParams commonParams);

    // 获取身份认证状态（货主）
    String QUERY_OWNER_IDENTITY_AUTHEN_STATUS = "userpublic.getusercentretypeauthowner";
    @POST("user-service/userpublic/getusercentretypeauthowner")
    Observable<BaseResponse<TypeAuthOwnerParams>> queryOwnerIdentityAuthenStatus(@Body CommonParams commonParams);

    //身份认证（司机）
    String TYPE_AUTH_DRIVER = "userpublic.usercentretypeauthdriver";
    @POST("user-service/userpublic/usercentretypeauthdriver")
    Observable<BaseResponse<Object>> typeauthdriver(@Body CommonParams<TypeAuthDriverParams> commonParams);

    //身份认证（货主）
    String TYPE_AUTH_SHIPPER = "userpublic.usercentretypeauthowner";
    @POST("user-service/userpublic/usercentretypeauthowner")
    Observable<BaseResponse<Object>> typeauthowner(@Body CommonParams<TypeAuthOwnerParams> commonParams);
}
