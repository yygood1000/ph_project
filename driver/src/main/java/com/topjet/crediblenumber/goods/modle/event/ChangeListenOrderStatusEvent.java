package com.topjet.crediblenumber.goods.modle.event;

import com.topjet.common.base.model.BaseEvent;

/**
 * Created by tsj-004 on 2017/9/28.
 * 修改听单语音播报状态，只是播报的声音哦，没有关闭网络请求
 */

public class ChangeListenOrderStatusEvent extends BaseEvent {
//    public static final String OPEN_LISTEN_ORDER = "OPEN_LISTEN_ORDER";      // 开启听单（声音和网络）
    public static final String CLOSE_LISTEN_ORDER = "CLOSE_LISTEN_ORDER";    // 关闭听单（声音和网络）
    public static final String CHANGE_CHECKBOX_STATUS = "CHANGE_CHECKBOX_STATUS";      // 改变按钮状态
    public static final String CHANGE_FLOATVIEW_STATUS = "CHANGE_FLOATVIEW_STATUS";      // 改变悬浮窗图标状态

    private String type;        // 类型，取值是上面三个

    public ChangeListenOrderStatusEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
