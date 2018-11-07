package com.topjet.common.im.model.response;

import com.topjet.common.db.bean.IMUserBean;
import com.topjet.common.im.model.bean.HistoryUserBean;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/12/11
 * describe: 黑名单中的用户信息
 */

public class BlackListUserResponse {
    private List<HistoryUserBean> chat_user_list; // 聊天用户记录集合

    public List<HistoryUserBean> getChat_user_list() {
        return chat_user_list;
    }

    public void setChat_user_list(List<HistoryUserBean> chat_user_list) {
        this.chat_user_list = chat_user_list;
    }
}
