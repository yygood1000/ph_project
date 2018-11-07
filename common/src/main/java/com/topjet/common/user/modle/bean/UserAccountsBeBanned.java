package com.topjet.common.user.modle.bean;

import java.io.Serializable;

/**
 * Created by tsj-004 on 2018/1/8.
 *
 * 帐号被禁止登录
 */

public class UserAccountsBeBanned implements Serializable{
    private String congeal_remark = null;   // 冻结原因
    private String congeal_time = null;     // 冻结时间

    public String getCongeal_remark() {
        return congeal_remark;
    }

    public void setCongeal_remark(String congeal_remark) {
        this.congeal_remark = congeal_remark;
    }

    public String getCongeal_time() {
        return congeal_time;
    }

    public void setCongeal_time(String congeal_time) {
        this.congeal_time = congeal_time;
    }
}