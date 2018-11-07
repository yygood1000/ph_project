package com.topjet.common.im.model.bean;

/**
 * creator: zhulunjun
 * time:    2017/12/7
 * describe: 历史消息
 */

public class HistoryMessageBean {

    private String msg_id;	// 否	string	消息id
    private String msg_timestamp;	// 否	string	消息时间
    private String msg_direction;	// 否	string	消息描述
    private String msg_to;	// 否	string	接收人的username
    private String msg_from;	 // 否	string	发送人的username
    private String msg_chat_type;	// 否	string	聊天类型	用来判断单聊还是群聊。chat: 单聊；groupchat: 群聊
    private String msg_pay_load; // 扩展字段

    public String getMsg_pay_load() {
        return msg_pay_load;
    }

    public void setMsg_pay_load(String msg_pay_load) {
        this.msg_pay_load = msg_pay_load;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getMsg_timestamp() {
        return msg_timestamp;
    }

    public void setMsg_timestamp(String msg_timestamp) {
        this.msg_timestamp = msg_timestamp;
    }

    public String getMsg_direction() {
        return msg_direction;
    }

    public void setMsg_direction(String msg_direction) {
        this.msg_direction = msg_direction;
    }

    public String getMsg_to() {
        return msg_to;
    }

    public void setMsg_to(String msg_to) {
        this.msg_to = msg_to;
    }

    public String getMsg_from() {
        return msg_from;
    }

    public void setMsg_from(String msg_from) {
        this.msg_from = msg_from;
    }

    public String getMsg_chat_type() {
        return msg_chat_type;
    }

    public void setMsg_chat_type(String msg_chat_type) {
        this.msg_chat_type = msg_chat_type;
    }
}
