package com.topjet.common.common.modle.bean;

/**
 * Created by yy on 2017/9/14.
 * <p>
 * 首页滑动按钮组 单项配置
 */

public class ScrollBtnOptions {

    private String icon_key;//图标key	string
    private String icon_url;//图标url	string
    private String title;//标题	string
    private String content;//内容	string
    private String url;//链接	string
    private String begin_date;	// 开始日期	String
    private String end_date;	// 结束日期	String

    @Override
    public String toString() {
        return "ScrollBtnOptions{" +
                "icon_key='" + icon_key + '\'' +
                ", icon_url='" + icon_url + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", begin_date='" + begin_date + '\'' +
                ", end_date='" + end_date + '\'' +
                '}';
    }

    public String getBegin_date() {
        return begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getIcon_key() {
        return icon_key;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
