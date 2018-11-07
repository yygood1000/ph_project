package com.topjet.common.message.modle.params;

/**
 * 消息列表
 * Created by tsj028 on 2017/12/7 0007.
 */

public class MessageListParams {
    private String message_type;     // 消息类型	是	string	1系统 2 订单 3钱包
    private String page;      // 页数	是	string	起始值为 1

    public MessageListParams(String message_type, String page){
        this.message_type = message_type;
        this.page = page;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
