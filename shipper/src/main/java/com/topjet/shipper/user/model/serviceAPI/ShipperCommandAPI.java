package com.topjet.shipper.user.model.serviceAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.user.modle.response.GetAnonymousResponse;
import com.topjet.common.user.modle.response.SignResponse;
import com.topjet.common.user.modle.response.UserCenterParameterResponse;
import com.topjet.shipper.user.model.params.SettingAnonymousParams;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tsj-004 on 2017/7/25.
 * 司机特有
 */

public interface ShipperCommandAPI {
    // 个人中心  签到
    String SIGN = "userpublic.signin";
    @POST("user-service/userpublic/signin")
    Observable<BaseResponse<SignResponse>> signin(@Body CommonParams commonParams);

    // 个人中心-参数
    String USER_CENTRE_PARAMETER = "userpublic.usercentreparameterowner";
    @POST("user-service/userpublic/usercentreparameterowner")
    Observable<BaseResponse<UserCenterParameterResponse>> getUserCentreParameter(@Body CommonParams commonParams);

    // 个人中心-货主-匿名设置
    String SETTING_ANONYMOUS = "userpublic.settinganonymous";
    @POST("user-service/userpublic/settinganonymous")
    Observable<BaseResponse<Object>> settingAnonymous(@Body CommonParams<SettingAnonymousParams> commonParams);

    // 个人中心-货主-获取匿名设置
    String GET_ANONYMOUS = "userpublic.getanonymoussetting";
    @POST("user-service/userpublic/getanonymoussetting")
    Observable<BaseResponse<GetAnonymousResponse>> getAnonymous(@Body CommonParams commonParams);
}