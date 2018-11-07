package com.topjet.common.common.modle.response;

import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/12/7
 * describe: 消息中心，未读数
 */

public class MessageCenterCountResponse {
    private String sys_sum;    // 系统未读消息总数	string
    private String order_sum;    // 订单未读消息总数	string
    private String walle_sum;    // 钱包未读消息总数	string

    public int getSys_sum() {
        return StringUtils.getIntToString(sys_sum);
    }

    public void setSys_sum(String sys_sum) {
        this.sys_sum = sys_sum;
    }

    public int getOrder_sum() {
        return StringUtils.getIntToString(order_sum);
    }

    public void setOrder_sum(String order_sum) {
        this.order_sum = order_sum;
    }

    public int getWalle_sum() {
        return StringUtils.getIntToString(walle_sum);
    }

    public void setWalle_sum(String walle_sum) {
        this.walle_sum = walle_sum;
    }

    public int getAllSum() {
        return getSys_sum() + getOrder_sum() + getWalle_sum();
    }
}
