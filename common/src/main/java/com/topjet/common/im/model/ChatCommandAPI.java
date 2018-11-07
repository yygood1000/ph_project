package com.topjet.common.im.model;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.im.model.response.BlackListUserResponse;
import com.topjet.common.im.model.response.HistoryMessageResponse;
import com.topjet.common.im.model.response.SensitiveWordResponse;
import com.topjet.common.im.model.response.UserStatusResponse;
import com.topjet.common.user.modle.params.RegisterParams;
import com.topjet.common.user.modle.response.RegisterReponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * creator: zhulunjun
 * time:    2017/12/4
 * describe: 聊天接口
 */

public interface ChatCommandAPI {

    /**
     * 用户状态，离线，还是在线
     */
    String IM_USER_STATUS = "im.imuserstatus";
    @POST("basic-service/im/imuserstatus")
    Observable<BaseResponse<UserStatusResponse>> getUserStatus(@Body CommonParams commonParams);

    /**
     * 获取聊天关键字列表
     */
    String IM_GET_SENSITIVE_WORD = "im.getsensitiveword";
    @POST("basic-service/im/getsensitiveword")
    Observable<BaseResponse<SensitiveWordResponse>> getSensitiveWord(@Body CommonParams commonParams);

    /**
     * 敏感字发送统计
     */
    String IM_SEND_SENSITIVE_WORD_COUNT = "im.sensitivecount";
    @POST("basic-service/im/sensitivecount")
    Observable<BaseResponse<Object>> sendSensitiveWordCount(@Body CommonParams commonParams);


    /**
     * 获取历史聊天记录
     */
    String IM_GET_HISTORY_MESSAGE = "im.downloadchatrecord";
    @POST("basic-service/im/downloadchatrecord")
    Observable<BaseResponse<HistoryMessageResponse>> getHistoryMessage(@Body CommonParams commonParams);

    /**
     * 获取更多历史聊天记录， 聊天界面下拉刷新
     */
    String IM_GET_MORE_HISTORY_MESSAGE = "im.downloadchathistoryrecord";
    @POST("basic-service/im/downloadchathistoryrecord")
    Observable<BaseResponse<HistoryMessageResponse>> getMoreHistoryMessage(@Body CommonParams commonParams);

    /**
     * 获取黑名单中的用户信息
     */
    String IM_GET_CHAT_BLACK_USER = "im.chatBlacklist";
    @POST("basic-service/im/chatBlacklist")
    Observable<BaseResponse<BlackListUserResponse>> getChatBlackUser(@Body CommonParams commonParams);

    /**
     * 清除历史聊天记录
     */
    String IM_DELETE_CONVERSATION = "im.deletefriend";
    @POST("basic-service/im/deletefriend")
    Observable<BaseResponse<Object>> clearHistoryMessage(@Body CommonParams commonParams);
}
