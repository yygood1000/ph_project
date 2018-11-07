package com.topjet.common.adv.modle.bean;

/**
 * Created by yy on 2017/11/14.
 * 首页轮播广告实体类/首页弹窗信息请求时返回体是该实体类
 */

public class BannerBean {
    private String web_title;//跳转后WEB标题	string
    private String content;//广告内容	string
    private String picture_key;//	广告图片key	string
    private String picture_url;//广告图片url	string
    private String redirect_url;//	广告跳转链接	string

    @Override
    public String toString() {
        return "BannerBean{" +
                "web_title='" + web_title + '\'' +
                ", content='" + content + '\'' +
                ", picture_key='" + picture_key + '\'' +
                ", picture_url='" + picture_url + '\'' +
                ", redirect_url='" + redirect_url + '\'' +
                '}';
    }

    public String getWeb_title() {
        return web_title;
    }

    public String getContent() {
        return content;
    }

    public String getPicture_key() {
        return picture_key;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public String getRedirect_url() {
        return redirect_url;
    }
}
