package com.topjet.common.user.modle.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.common.modle.params.CheckVerificationCodeParams;
import com.topjet.common.user.modle.bean.ReferrerInfoBean;
import com.topjet.common.user.modle.params.EditAvatarParams;
import com.topjet.common.user.modle.params.GetRecommendListParams;
import com.topjet.common.user.modle.params.LoginParams;
import com.topjet.common.user.modle.params.PasswordParams;
import com.topjet.common.user.modle.params.RegisterParams;
import com.topjet.common.user.modle.params.SaveCallPhoneInfoParams;
import com.topjet.common.user.modle.params.SendCheckCodeParams;
import com.topjet.common.user.modle.params.SwitchLoginParams;
import com.topjet.common.user.modle.params.UpdateUserPassParams;
import com.topjet.common.user.modle.response.GetAvatarInfoResponse;
import com.topjet.common.user.modle.response.LoginResponse;
import com.topjet.common.user.modle.response.PasswordResponse;
import com.topjet.common.user.modle.response.RecommendListResponse;
import com.topjet.common.user.modle.response.RegisterReponse;
import com.topjet.common.user.modle.response.RetrievePasswordResponse;
import com.topjet.common.user.modle.response.SendCheckCodeResponse;
import com.topjet.common.user.modle.response.SwitchLoginResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tsj-004 on 2017/7/25.
 * 用户相关
 */

public interface UserCommandAPI {

    //登录
    String LOGIN = "user.login";
    @POST("user-service/user/login")
    Observable<BaseResponse<LoginResponse>> login(@Body CommonParams<LoginParams> commonParams);


    //注册
    String REGISTER = "user.register";
    @POST("user-service/user/register")
    Observable<BaseResponse<RegisterReponse>> register(@Body CommonParams<RegisterParams> registerParams);

    //发送验证码
    String CHECK_CODE = "user.sendCheckCode";
    @POST("user-service/user/sendCheckCode")
    Observable<BaseResponse<SendCheckCodeResponse>> sendCheckCode(@Body CommonParams<SendCheckCodeParams>
                                                                          sendCheckCodeParams);

    //发送语音验证码
    String CHECK_VOICE_CODE = "user.sendVoiceCheckCode";
    @POST("user-service/user/sendVoiceCheckCode")
    Observable<BaseResponse<SendCheckCodeResponse>> sendVoiceCheckCode(@Body CommonParams<SendCheckCodeParams>
                                                                               sendCheckCodeParams);

    //找回密码
    String UPDATE_PASSWORD = "user.updaUserPass";
    @POST("user-service/user/updaUserPass")
    Observable<BaseResponse<RetrievePasswordResponse>> updateUserPass(@Body CommonParams<UpdateUserPassParams>
                                                                        updateUserPassParams);

    //校验旧密码
    String CHECK_OLD_PASSWORD = "usercenter.usercentercheckpass";
    @POST("user-service/usercenter/usercentercheckpass")
    Observable<BaseResponse<RetrievePasswordResponse>> checkOldPassword(@Body CommonParams<PasswordParams>
                                                                        updateUserPassParams);
    //修改密码
    String CHANGE_PASSWORD = "usercenter.usercenterupdatepass";
    @POST("user-service/usercenter/usercenterupdatepass")
    Observable<BaseResponse<PasswordResponse>> changePassword(@Body CommonParams<PasswordParams>
                                                                        updateUserPassParams);

    //校验验证码
    String CHECK_OUT_CODE = "user.checkoutCode";
    @POST("user-service/user/checkoutCode")
    Observable<BaseResponse<SendCheckCodeResponse>> checkCode(@Body CommonParams<CheckVerificationCodeParams> sendCheckCodeParams);

    // 保存通话记录
    String SAVE_CALL_PHONE_INFO = "userorder.savecallphoneinfo";
    @POST("user-service/userorder/savecallphoneinfo")
    Observable<BaseResponse<Object>> saveCallPhoneInfo(@Body CommonParams<SaveCallPhoneInfoParams> commonParams);

    // 个人中心  退出
    String LOGOUT = "user.loginout";
    @POST("user-service/user/loginout")
    Observable<BaseResponse<Object>> doLogout(@Body CommonParams commonParams);

    // 个人中心  推荐人信息
    String GET_REFERRER_INFO = "usercenter.usercentermyrecommend";
    @POST("user-service/usercenter/usercentermyrecommend")
    Observable<BaseResponse<ReferrerInfoBean>> getReferrerInfo(@Body CommonParams commonParams);

    // 个人中心  我的推荐记录
    String GET_RECOMMEND_LIST = "usercenter.usercenterrecommendlist";
    @POST("user-service/usercenter/usercenterrecommendlist")
    Observable<BaseResponse<RecommendListResponse>> getRecommendList(@Body CommonParams<GetRecommendListParams> commonParams);

    // 个人中心->修改头像  获取头像信息
    String GET_AVATAR_INFO = "usercenter.usercenterselecticon";
    @POST("user-service/usercenter/usercenterselecticon")
    Observable<BaseResponse<GetAvatarInfoResponse>> getAvatarInfo(@Body CommonParams commonParams);

    // 个人中心->修改头像  修改用户头像
    String EDIT_AVATAR_INFO = "usercenter.usercenterupdateicon";
    @POST("user-service/usercenter/usercenterupdateicon")
    Observable<BaseResponse<Object>> editAvatarInfo(@Body CommonParams<EditAvatarParams> commonParams);

    // 切换后登录
    String SWITCH_LOGIN = "appswitch.switchlogin";
    @POST("user-service/appswitch/switchlogin")
    Observable<BaseResponse<SwitchLoginResponse>> switchLogin(@Body CommonParams<SwitchLoginParams> commonParams);
}
