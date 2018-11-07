package com.topjet.common.adv.modle.params;

/**
 * Created by yy on 2017/11/13.
 * <p>
 * 获取活动链接
 */

public class GetAnnouncementUrlParams {
    private String announcement_id;// 公告id

    public GetAnnouncementUrlParams(String announcement_id) {
        this.announcement_id = announcement_id;
    }

    @Override
    public String toString() {
        return "GetAnnouncementUrlParams{" +
                "announcement_id='" + announcement_id + '\'' +
                '}';
    }
}
