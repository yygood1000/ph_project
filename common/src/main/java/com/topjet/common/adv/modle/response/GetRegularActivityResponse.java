package com.topjet.common.adv.modle.response;

/**
 * Created by yy on 2017/11/13.
 * 定时福袋广告返回参数
 */

public class GetRegularActivityResponse {
    private String picture_key;//	活动图标Key
    private String picture_url;//活动图标URL
    private String activity_id;//	活动id
    private String web_title;//	活动标题

    public String getPicture_key() {
        return picture_key;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public String getWeb_title() {
        return web_title;
    }

    @Override
    public String toString() {
        return "GetRegularActivityResponse{" +
                "picture_key='" + picture_key + '\'' +
                ", picture_url='" + picture_url + '\'' +
                ", activity_id='" + activity_id + '\'' +
                ", web_title='" + web_title + '\'' +
                '}';
    }
}
