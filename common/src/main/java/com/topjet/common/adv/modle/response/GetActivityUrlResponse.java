package com.topjet.common.adv.modle.response;

import com.topjet.common.utils.StringUtils;

/**
 * Created by yy on 2017/11/13.
 * 点击定时福袋，获取活动链接
 * 首页公告活动链接
 */

public class GetActivityUrlResponse {
    private String redirect_url;//	活动链接

    // 定时活动特有字段
    private String is_effective;//	是否失效		0：活动有效、1：活动过期

    // 公告特有字段
    private String title;//公告标题
    private String content;//	公告内容

    @Override
    public String toString() {
        return "GetActivityUrlResponse{" +
                "redirect_url='" + redirect_url + '\'' +
                ", is_effective='" + is_effective + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public boolean getIs_effective() {
        return StringUtils.isBlank(is_effective) || is_effective.equals("1");
    }
}
