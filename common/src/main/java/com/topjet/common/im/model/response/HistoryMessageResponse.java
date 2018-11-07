package com.topjet.common.im.model.response;

import com.topjet.common.im.model.bean.HistoryMessageBean;
import com.topjet.common.im.model.bean.HistoryUserBean;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/12/7
 * describe: 从服务器获取历史聊天记录
 */

public class HistoryMessageResponse {

    private List<HistoryMessageBean> chat_record_list;	// 是	List	聊天记录集合 【具体结构见下表】
    private List<HistoryUserBean> chat_user_list; 	// 是	List	聊天用户记录集合【具体结构见下表】

    public List<HistoryMessageBean> getChat_record_list() {
        return chat_record_list;
    }

    public void setChat_record_list(List<HistoryMessageBean> chat_record_list) {
        this.chat_record_list = chat_record_list;
    }

    public List<HistoryUserBean> getChat_user_list() {
        return chat_user_list;
    }

    public void setChat_user_list(List<HistoryUserBean> chat_user_list) {
        this.chat_user_list = chat_user_list;
    }
}
