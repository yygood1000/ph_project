package com.topjet.common.common.presenter;

import android.content.Context;

import com.hyphenate.chat.EMClient;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.presenter.BaseFragmentApiPresenter;
import com.topjet.common.base.view.fragment.RxFragment;
import com.topjet.common.common.modle.event.MessageUnReadCountEvent;
import com.topjet.common.common.modle.response.MessageCenterCountResponse;
import com.topjet.common.common.modle.serverAPI.SystemCommand;
import com.topjet.common.common.view.fragment.MessageCenterView;
import com.topjet.common.im.presenter.IMPresenter;

/**
 * Created by yy on 2017/8/14.
 * 首页Presenter 消息中心
 */

public class MessageCenterPresenter extends BaseFragmentApiPresenter<MessageCenterView, BaseCommand> {
    public MessageCenterPresenter(MessageCenterView mView, Context mContext, RxFragment mFragment) {
        super(mView, mContext, mFragment);
    }

    /**
     * 获取未读消息
     */
    public void getMessageCenterCount(){
        new SystemCommand(mActivity)
                .getMessageCenterCount(new ObserverOnResultListener<MessageCenterCountResponse>() {
            @Override
            public void onResult(MessageCenterCountResponse messageCenterCountResponse) {
                mView.setUnReadCount(messageCenterCountResponse);

                // 判断消息是否已读完
                // 环信的未读消息数
                int chatCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
                // 总的消息未读数，系统消息+环信消息
                int allCount = chatCount + messageCenterCountResponse.getAllSum();
                // 发送消息到首页
                BusManager.getBus().post(new MessageUnReadCountEvent(allCount));
            }
        });
    }

    /**
     * 获取聊天记录
     */
    public void getHistoryMessage(){
        new IMPresenter(mActivity).getHistoryMessage();
    }
}
