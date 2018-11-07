package com.topjet.common.adv.modle.response;

/**
 * Created by yy on 2017/11/14.
 * <p>
 * 首页公告返回体
 */

public class GetAnnouncementUrlResponse {
    private String announcement_truck;// 车源公告 货主版使用
    private String announcement_goods;// 货源公告 司机版使用
    private String announcement_id;//公告id	用于获取跳转链接
    private String title;//公告标题
    private String content;//公告内容

    @Override
    public String toString() {
        return "GetAnnouncementResponse{" +
                "announcement_truck='" + announcement_truck + '\'' +
                ", announcement_goods='" + announcement_goods + '\'' +
                ", announcement_id='" + announcement_id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getAnnouncement_truck() {
        return announcement_truck;
    }

    public String getAnnouncement_goods() {
        return announcement_goods;
    }

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
