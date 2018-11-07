package com.topjet.common.common.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * creator: zhulunjun
 * time:    2017/10/31
 * describe: 跳转首页
 */

public class TabIndex extends BaseExtra {
    private int index = -1; // 首页底部tab下标
    private int orderIndex = -1; // 我的订单页下标
    private int orderStateIndex = -1; // 我都订单大标签下小标签的下标

    // 一级
    public static int HOME_PAGE = 0; // 首页
    public static int MY_ORDER_PAGE = 1; // 我的订单
    public static int MESSAGE_PAGE = 2; // 聊天
    public static int PERSON_CENTER_PAGE = 3; // 个人中心
    public static int SMART_SEARCH_GOODS_PAGE = 4; // 找货
    public static int LISTEN_ORDER_PAGE = 5;       // 听单

    // 二级

    // 我的订单 货主版
    public final static int MY_ORDER = 0;
    public static int HISTROY_ORDER = 1;// 历史订单

    // 我的订单 司机版
    public static int WAIT_BEAR = 1;// 待承运
    public static int BEAR = 2;// 承运中
    public static int SING = 3;// 已签收

    // 三级

    // 我的订单 货主版
    public static int ALL = 0;// 全部
    public static int NEW = 1;// 新货源
    public static int WAIT_DEAL = 2;// 待成交
    public static int WAIT_PAY = 3;// 待支付
    public static int TRANSPORT = 4;// 承运中

    // 历史订单
    public static int WAIT_COMMENT = 0;// 待评价
    public static int COMMENT = 1;// 已评价
    public static int INVALID = 2;// 已失效



    public TabIndex(int index) {
        this.index = index;
    }

    public TabIndex(int index, int orderIndex) {
        this.index = index;
        this.orderIndex = orderIndex;
    }

    public TabIndex(int index, int orderIndex, int orderStateIndex) {
        this.index = index;
        this.orderIndex = orderIndex;
        this.orderStateIndex = orderStateIndex;
    }

    public int getOrderStateIndex() {
        return orderStateIndex;
    }

    public void setOrderStateIndex(int orderStateIndex) {
        this.orderStateIndex = orderStateIndex;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
