package com.topjet.common.common.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * creator: yy
 * time:    2017/10/31
 * describe: 跳转只有WebView的Activity
 */

public class SimpleWebViewExtra extends BaseExtra {
    private String title;
    private String url;
    private boolean isHideTitle;

    public SimpleWebViewExtra(String url) {
        this.url = url;
    }

    public SimpleWebViewExtra(String url, String title) {
        this.title = title;
        this.url = url;
    }

    public SimpleWebViewExtra(String url, String title, boolean isHideTitle) {
        this.title = title;
        this.url = url;
        this.isHideTitle = isHideTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public boolean isHideTitle() {
        return isHideTitle;
    }
}
