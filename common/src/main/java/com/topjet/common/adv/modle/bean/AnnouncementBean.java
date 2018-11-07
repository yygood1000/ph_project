package com.topjet.common.adv.modle.bean;

/**
 * Created by yy on 2017/11/15.
 * <p>
 * 公告实体类
 */

public class AnnouncementBean {
    private String announcement_id;//	公告id		用于获取跳转链接
    private String title;//	公告标题
    private String content;//	公告内容

    public String getAnnouncement_id() {
        return announcement_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
