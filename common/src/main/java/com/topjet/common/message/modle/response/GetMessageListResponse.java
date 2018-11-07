package com.topjet.common.message.modle.response;

import com.topjet.common.message.modle.bean.MessageListInfo;

import java.util.List;

/**
 * 消息列表返回数据类
 * Created by tsj028 on 2017/12/7 0007.
 */

public class GetMessageListResponse {
    private List<MessageListInfo> list;

    public List<MessageListInfo> getList() {
        return list;
    }

    public void setList(List<MessageListInfo> list) {
        this.list = list;
    }
}
