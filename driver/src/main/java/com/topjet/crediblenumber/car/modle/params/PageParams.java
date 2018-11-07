package com.topjet.crediblenumber.car.modle.params;

/**
 * Created by tsj-004 on 2017/10/20.
 *
 * 分页查询
 */

public class PageParams {
    private String page = null;        // 分页参数	初始值为1

    public PageParams() {
    }

    public PageParams(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
