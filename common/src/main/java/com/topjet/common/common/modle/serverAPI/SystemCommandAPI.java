package com.topjet.common.common.modle.serverAPI;

import com.topjet.common.base.model.Session;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.common.modle.bean.KeyBean;
import com.topjet.common.common.modle.params.FixedCycleLocationParams;
import com.topjet.common.common.modle.response.AppUpgradeResponse;
import com.topjet.common.common.modle.response.MessageCenterCountResponse;
import com.topjet.common.common.modle.response.ShareResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tsj-004 on 2017/7/25.
 * 基础服务
 */

public interface SystemCommandAPI {


    /**
     * 获取加密解密key偏移量
     */
    String GET_KEY = "system.getAesKey";
    @POST("system/getAesKey")
    Observable<BaseResponse<KeyBean>> getKey(@Body CommonParams commonParams);

    //获取session
    String GET_SESSION = "system.getSession";
    @POST("system/getSession")
    Observable<BaseResponse<Session>> getSession(@Body CommonParams commonParams);

    //检测APP更新
    String APP_UPGRADE = "system.appUpgrade";
    @POST("system/appUpgrade")
    Observable<BaseResponse<AppUpgradeResponse>> appUpgrade(@Body CommonParams commonParams);

    //分享APP-二维码
    String SHARE_APP_OF_QRCODE = "userpublic.shareappofqrcode";
    @POST("user-service/userpublic/shareappofqrcode")
    Observable<BaseResponse<ShareResponse>> shareAppOfQrcode(@Body CommonParams commonParams);

    //分享APP-二短信码
    String SHARE_APPOF_SMS = "userpublic.shareappofsms";
    @POST("user-service/userpublic/shareappofsms")
    Observable<BaseResponse<ShareResponse>> shareAppOfSms(@Body CommonParams commonParams);

    //定时上传定位信息
    String SAVE_USER_GPS_INFO = "user.saveusergpsinfo";
    @POST("user-service/user/saveusergpsinfo")
    Observable<BaseResponse<Object>> saveUserGpsInfo(@Body CommonParams<FixedCycleLocationParams> commonParams);

    //获取消息中心未读数
    String MESSAGE_CENTER_LIST_COUNT = "messagecenter.messagecenterlist";
    @POST("resource-service/messagecenter/messagecenterlist")
    Observable<BaseResponse<MessageCenterCountResponse>> getMessageCenterCount(@Body CommonParams commonParams);
}
