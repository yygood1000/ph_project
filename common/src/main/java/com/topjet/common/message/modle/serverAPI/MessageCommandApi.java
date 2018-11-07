package com.topjet.common.message.modle.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.message.modle.params.MessageListParams;
import com.topjet.common.message.modle.params.ReadFlagParams;
import com.topjet.common.message.modle.response.GetMessageListResponse;
import com.topjet.common.message.modle.response.ReadFlagResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 消息列表相关接口
 * Created by tsj028 on 2017/12/4 0004.
 */

public interface MessageCommandApi {
    /**
     * 获取消息列表
     */
    String MESSAGE_LIST = "messagecenter.messagelist";

    @POST("resource-service/messagecenter/messagelist")
    Observable<BaseResponse<GetMessageListResponse>> getMessageList(@Body CommonParams<MessageListParams> commonParams);

    /**
     * 设置消息为已读
     */
    String SET_READFLAG = "messagecenter.settingreadflag";

    @POST("resource-service/messagecenter/settingreadflag")
    Observable<BaseResponse<ReadFlagResponse>> setReadFlag(@Body CommonParams<ReadFlagParams> commonParams);
}
