package com.easeim.ui.activity.view;

import com.topjet.common.base.view.activity.IView;

/**
 * creator: zhulunjun
 * time:    2017/12/4
 * describe: 聊天
 */

public interface ChatView extends IView, BaseChatView{

    void addBlackListSuccess();
    void clearHistorySuccess();
    void setUserStatus(String statusText);
}
