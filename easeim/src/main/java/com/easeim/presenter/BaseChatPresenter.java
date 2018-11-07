package com.easeim.presenter;

import android.content.Context;

import com.easeim.R;

import com.easeim.ui.activity.view.BaseChatView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.im.model.ChatCommand;
import com.topjet.common.utils.RxHelper;
import com.topjet.common.utils.Toaster;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * creator: zhulunjun
 * time:    2017/12/4
 * describe: 聊天相关逻辑父类
 */

public abstract class BaseChatPresenter<T extends BaseChatView> extends BaseApiPresenter<T, ChatCommand> {

    public BaseChatPresenter(T mView, Context mContext) {
        super(mView, mContext, new ChatCommand((MvpActivity) mContext));
    }

    /**
     * 移出黑名单
     */
    public void outBlackList(final String userName) {
        //将用户从黑名单中移除
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                boolean isSuccess;
                try {
                    EMClient.getInstance().contactManager().removeUserFromBlackList(userName);
                    isSuccess = true;
                } catch (HyphenateException e1) {
                    e1.printStackTrace();
                    isSuccess = false;
                    Toaster.showShortToast(R.string.Removed_from_the_failure);
                }
                e.onNext(isSuccess);
                e.onComplete();
            }
        })
                .compose(RxHelper.<Boolean>rxSchedulerHelper())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if(aBoolean) {
                            mView.removeBlackListSuccess();
                            Toaster.showShortToast(R.string.remove_black_list_success);
                        }
                    }
                });

    }
}
