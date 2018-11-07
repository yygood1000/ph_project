package com.easeim.presenter;

import android.content.Context;

import com.easeim.HistoryMessageHelper;
import com.easeim.IMHelper;
import com.easeim.ui.activity.view.BlackListView;
import com.hyphenate.chat.EMClient;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.db.DBManager;
import com.topjet.common.im.model.bean.HistoryUserBean;
import com.topjet.common.im.model.params.UserIdListParams;
import com.topjet.common.im.model.response.BlackListUserResponse;
import com.topjet.common.im.presenter.IMPresenter;
import com.topjet.common.utils.RxHelper;

import java.util.Collections;
import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/12/4
 * describe: 黑名单
 */

public class BlackListPresenter extends BaseChatPresenter<BlackListView<String>> {
    public BlackListPresenter(BlackListView<String> mView, Context mContext) {
        super(mView, mContext);
    }

    /**
     * 获取黑名单列表
     */
    public void getBlackList(){
        List<String> blackIds = EMClient.getInstance().contactManager().getBlackListUsernames();
        getBlackListUsers(blackIds);
        // 倒叙
        Collections.reverse(blackIds);
        mView.loadSuccess(blackIds);
    }


    /**
     * 获取黑名单用户的用户信息
     */
    public void getBlackListUsers(final List<String> blackIds) {
        if (blackIds != null && blackIds.size() > 0) {
            mApiCommand.getChatBlackUser(new UserIdListParams(blackIds), new ObserverOnResultListener<BlackListUserResponse>() {
                @Override
                public void onResult(final BlackListUserResponse blackListUserResponse) {
                    if (blackListUserResponse != null && blackListUserResponse.getChat_user_list() != null) {
                        saveBlackUser(blackListUserResponse.getChat_user_list(), blackIds);
                    }
                }
            });
        }
    }

    /**
     * 保存黑名单用户
     */
    private void saveBlackUser(final List<HistoryUserBean> users, final List<String> blackIds){
            RxHelper.runIOThread(new RxHelper.OnIOThreadListener() {
                @Override
                public Object onIOThread() {
                    for (HistoryUserBean userBean : users) {
                        IMHelper.getInstance().saveContact(HistoryMessageHelper.getInstance().getHistoryUser(userBean));
                    }
                    return new Object();
                }
                @Override
                public void onMainThread(Object o) {
                    mView.loadSuccess(blackIds);
                }
            });
        }


}
