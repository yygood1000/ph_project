package com.topjet.common.base.model;

import com.topjet.common.base.busManger.IEvent;

public class BaseEvent implements IEvent {
    protected boolean success;
    protected String msgId;
    protected String msg;
    protected String tag;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "BaseEvent{" +
                "success=" + success +
                ", msgId='" + msgId + '\'' +
                ", msg='" + msg + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
