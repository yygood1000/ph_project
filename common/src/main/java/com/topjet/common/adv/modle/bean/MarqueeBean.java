package com.topjet.common.adv.modle.bean;

/**
 * Created by yy on 2017/11/13.
 * <p>
 * 跑马灯广告实体类
 */

public class MarqueeBean {
    private String title;//	广告标题
    private String content;//	广告内容

    @Override
    public String toString() {
        return "MarqueeBean{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
