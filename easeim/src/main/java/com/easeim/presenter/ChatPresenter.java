package com.easeim.presenter;

import android.content.Context;

import com.easeim.R;

import com.easeim.ui.activity.view.ChatView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.im.model.params.TalkIdParams;
import com.topjet.common.im.model.params.UserIdParams;
import com.topjet.common.im.model.response.UserStatusResponse;
import com.topjet.common.utils.DelayUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.RxHelper;
import com.topjet.common.utils.Toaster;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;


/**
 * creator: zhulunjun
 * time:    2017/12/4
 * describe: 聊天用的控制类
 */

public class ChatPresenter extends BaseChatPresenter<ChatView> {

    public ChatPresenter(ChatView mView, Context mContext) {
        super(mView, mContext);
    }

    public ChatPresenter(Context mContext) {
        super(null, mContext);
    }


    /**
     * 加入黑名单
     */
    public void addBlack(final String userName) {

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                boolean isSuccess;
                try {
                    // true，则把用户加入到黑名单后双方发消息时对方都收不到；
                    // false，则我能给黑名单的中用户发消息，但是对方发给我时我是收不到的
                    EMClient.getInstance().contactManager().addUserToBlackList(userName, true);
                    isSuccess = true;
                } catch (HyphenateException e1) {
                    e1.printStackTrace();
                    isSuccess = false;
                    Toaster.showShortToast(R.string.Move_into_blacklist_failure);
                }
                e.onNext(isSuccess);
                e.onComplete();
            }
        })
                .compose(RxHelper.<Boolean>rxSchedulerHelper())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            mView.addBlackListSuccess();
                            Toaster.showShortToast(R.string.Move_into_blacklist_success);
                        }
                    }
                });
    }

    /**
     * 清空聊天记录
     */
    public void clearHistoryMessage(final String userName, final boolean clearLocal) {
        mApiCommand.clearHistoryMessage(new TalkIdParams(userName), new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                if(clearLocal) {
                    clearHistoryMessageLocal(userName);
                }
            }
        });
    }

    /**
     * 清空本地的聊天记录
     */
    private void clearHistoryMessageLocal(String userName){

        // 清空本地聊天记录
        EMConversation conversation = EMClient.getInstance().chatManager()
                .getConversation(userName, EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_SINGLE), true);
        emm =conversation.getLastMessage();
        conversation.clearAllMessages();
        conversation.setExtField(System.currentTimeMillis()+"");
        saveEmptyMessage();
        mView.clearHistorySuccess();
        Toaster.showShortToast("清空成功");
    }

    /**
     * 保存空消息，让清空的回话可以在回话列表显示
     */
    EMMessage emm;
    private void saveEmptyMessage(){
        emm.setAttribute(EaseConstant.MESSAGE_ATTR_EMPTY, true);
        EMClient.getInstance().chatManager().saveMessage(emm);
    }

    /**
     * 获取用户状态
     */
    public void getUserStatus(String userName){
        mApiCommand.getUserStatus(new UserIdParams(userName), new ObserverOnResultListener<UserStatusResponse>() {
            @Override
            public void onResult(UserStatusResponse userStatusResponse) {
                if(mView != null) {
                    mView.setUserStatus(userStatusResponse.getIm_status());
                }
            }
        });
    }

}
