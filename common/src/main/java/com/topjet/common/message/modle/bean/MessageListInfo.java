package com.topjet.common.message.modle.bean;

/**
 * Created by tsj028 on 2017/12/7 0007.
 */

public class MessageListInfo {
    private String message_id;    // 消息ID	string
    private String message_type;   // 消息类型	string	1系统 2 订单 3钱包
    private String read_flag;     // 是否已读	string	0未读 1已读
    private String title;     // 消息标题	string
    private String content;     // 消息内容	string
    private String related_id;     // 关联ID	string	用于查看详情
    private String create_time;     // 消息创建时间	string

    public static final String MESSAGE_TYPE_ORDER = "2";
    public static final String MESSAGE_TYPE_WALLET = "3";

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getRead_flag() {
        return read_flag;
    }

    public void setRead_flag(String read_flag) {
        this.read_flag = read_flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRelated_id() {
        return related_id;
    }

    public void setRelated_id(String related_id) {
        this.related_id = related_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "MessageListInfo{" +
                "message_id='" + message_id + '\'' +
                ", message_type='" + message_type + '\'' +
                ", read_flag='" + read_flag + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", related_id='" + related_id + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
