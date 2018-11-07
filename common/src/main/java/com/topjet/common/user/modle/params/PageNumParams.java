package com.topjet.common.user.modle.params;

/**
 * 列表页数公共类（提交时）
 * Created by tsj004 on 2017/8/24.
 */

public class PageNumParams {
    public PageNumParams(String page) {
        this.page = page;
    }

    private String page;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
