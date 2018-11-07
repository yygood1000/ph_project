package com.topjet.common.im.model;


import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.im.model.params.SendSensitiveCountParams;
import com.topjet.common.im.model.params.TalkIdParams;
import com.topjet.common.im.model.params.UserIdListParams;
import com.topjet.common.im.model.params.UserIdParams;
import com.topjet.common.im.model.response.BlackListUserResponse;
import com.topjet.common.im.model.response.HistoryMessageResponse;
import com.topjet.common.im.model.response.SensitiveWordResponse;
import com.topjet.common.im.model.response.UserStatusResponse;
import com.topjet.common.order_detail.modle.params.GoodsIdParams;
import com.topjet.common.order_detail.modle.response.GoodsInfoResponse;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;

/**
 * creator: zhulunjun
 * time:    2017/12/4
 * describe: 聊天接口实现类
 */

public class ChatCommand extends BaseCommand<ChatCommandAPI> {
    public ChatCommand(MvpActivity mActivity) {
        super(ChatCommandAPI.class, mActivity);
    }


    /**
     * 用户状态，离线，还是在线
     */
    public void getUserStatus(UserIdParams params, ObserverOnResultListener<UserStatusResponse> listener) {
        mCommonParams = getParams(ChatCommandAPI.IM_USER_STATUS, params);
        handleOnResultObserver(mApiService.getUserStatus(mCommonParams), listener, false);

    }

    /**
     * 获取聊天关键字列表
     */
    public void getSensitiveWord(ObserverOnResultListener<SensitiveWordResponse> listener) {
        mCommonParams = getParams(ChatCommandAPI.IM_GET_SENSITIVE_WORD);
        handleOnResultObserverNotActivity(mApiService.getSensitiveWord(mCommonParams), listener);

    }

    /**
     * 敏感字发送统计
     */
    public void sendSensitiveWordCount(SendSensitiveCountParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(ChatCommandAPI.IM_SEND_SENSITIVE_WORD_COUNT, params);
        handleOnResultObserverNotActivity(mApiService.sendSensitiveWordCount(mCommonParams), listener);

    }


    /**
     * 获取历史聊天记录
     */
    public void getHistoryMessage(ObserverOnResultListener<HistoryMessageResponse> listener) {
        mCommonParams = getParams(ChatCommandAPI.IM_GET_HISTORY_MESSAGE);
        handleOnResultObserver(mApiService.getHistoryMessage(mCommonParams), listener);

    }

    /**
     * 获取更多历史聊天记录， 聊天界面下拉刷新
     */
    public void getMoreHistoryMessage(TalkIdParams params, ObserverOnResultListener<HistoryMessageResponse> listener) {
        mCommonParams = getParams(ChatCommandAPI.IM_GET_MORE_HISTORY_MESSAGE, params);
        handleOnResultObserver(mApiService.getMoreHistoryMessage(mCommonParams), listener);

    }

    /**
     * 获取黑名单中的用户信息
     */
    public void getChatBlackUser(UserIdListParams params, ObserverOnResultListener<BlackListUserResponse> listener) {
        mCommonParams = getParams(ChatCommandAPI.IM_GET_CHAT_BLACK_USER, params);
        handleOnResultObserver(mApiService.getChatBlackUser(mCommonParams), listener, false);

    }

    /**
     * 清除聊天记录
     */
    public void clearHistoryMessage(TalkIdParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(ChatCommandAPI.IM_DELETE_CONVERSATION, params);
        handleOnResultObserver(mApiService.clearHistoryMessage(mCommonParams), listener);

    }
}
