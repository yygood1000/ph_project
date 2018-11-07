package com.topjet.common.common.modle.event;

import com.topjet.common.base.model.BaseEvent;

/**
 * creator: zhulunjun
 * time:    2017/12/12
 * describe: 消息中心未读消息的显示数
 */

public class MessageUnReadCountEvent extends BaseEvent {
    private int unReadCount = 0;

    public MessageUnReadCountEvent(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }
}
