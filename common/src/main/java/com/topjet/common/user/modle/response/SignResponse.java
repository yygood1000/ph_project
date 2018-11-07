package com.topjet.common.user.modle.response;

/**
 * Created by tsj-004 on 2017/10/26.
 *
 * 个人中心 签到 返回参数
 */

public class SignResponse {
    private String next_score = null;       // 下次签到可获取的积分

    public String getNext_score() {
        return next_score;
    }

    public void setNext_score(String next_score) {
        this.next_score = next_score;
    }
}
