package com.topjet.common.message.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * 消息列表跳转类型
 * Created by tsj028 on 2017/12/7 0007.
 */

public class MessageListExtra extends BaseExtra{
    public static final String MESSAGE_LIST_SYSTEM = "1";     // 系统消息常量
    public static final String MESSAGE_LIST_ORDER = "2";     // 订单消息常量
    public static final String MESSAGE_LIST_WALLET = "3";     // 钱包消息常量

    private String type = "1";     // 消息类型	是	string	1系统 2 订单 3钱包

    public MessageListExtra(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
