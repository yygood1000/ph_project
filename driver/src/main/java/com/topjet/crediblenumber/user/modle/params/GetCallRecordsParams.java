package com.topjet.crediblenumber.user.modle.params;

/**
 * Created by tsj-004 on 2017/11/3.
 *
 * 获取通话记录请求参数
 */

public class GetCallRecordsParams {
    private String page = null; // 分页参数 初始页为1

    public GetCallRecordsParams(String page) {
        this.page = page;
    }

    public GetCallRecordsParams() {
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}