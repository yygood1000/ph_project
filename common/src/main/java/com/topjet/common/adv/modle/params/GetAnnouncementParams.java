package com.topjet.common.adv.modle.params;

/**
 * Created by yy on 2017/11/13.
 * <p>
 * 首页公告
 */

public class GetAnnouncementParams {
    private String latitude;// 公告id
    private String longitude;// 公告id

    public GetAnnouncementParams(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "GetAnnouncementParams{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
