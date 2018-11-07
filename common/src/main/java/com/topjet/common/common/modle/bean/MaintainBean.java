package com.topjet.common.common.modle.bean;

/**
 * Created by tsj-004 on 2018/1/8.
 *
 * 停机维护
 */

public class MaintainBean {
    /**
     * valid : 1
     * title : 系统维护
     * text : 维护时间2017.10.20——2017-10.21
     */

    private String valid;      // 有效， 0否 1 是
    private String title;      // 标题
    private String text;        // 内容

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}