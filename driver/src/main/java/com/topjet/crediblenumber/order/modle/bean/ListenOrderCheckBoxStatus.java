package com.topjet.crediblenumber.order.modle.bean;

import com.topjet.common.base.model.BaseEvent;

/**
 * Created by tsj-004 on 2017/9/11.
 * 司机-听单开关状态
 */

public class ListenOrderCheckBoxStatus extends BaseEvent{
    private String status = null;  // 开关状态  0:关闭听单 1:开启听单

    public ListenOrderCheckBoxStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}