package com.topjet.common.message.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.message.modle.params.MessageListParams;
import com.topjet.common.message.modle.params.ReadFlagParams;
import com.topjet.common.message.modle.response.GetMessageListResponse;
import com.topjet.common.message.modle.response.ReadFlagResponse;

/**
 * 消息列表
 * Created by tsj028 on 2017/12/4 0004.
 */

public class MessageCommand extends BaseCommand<MessageCommandApi> {
    public MessageCommand(Class<MessageCommandApi> c, MvpActivity mActivity) {
        super(c, mActivity);
    }

    /**
     * 获取消息列表请求
     */
    public void getMessageList(MessageListParams params, ObserverOnNextListener<GetMessageListResponse> listener) {
        mCommonParams = getParams(MessageCommandApi.MESSAGE_LIST, params);
        handleOnNextObserver(mApiService.getMessageList(mCommonParams), listener,false);
    }

    /**
     * 设置消息为已读
     */
    public void setReadFlag(ReadFlagParams params, ObserverOnResultListener<ReadFlagResponse> listener) {
        mCommonParams = getParams(MessageCommandApi.SET_READFLAG, params);
        handleOnResultObserverNotActivity(mApiService.setReadFlag(mCommonParams), listener);
    }
}
