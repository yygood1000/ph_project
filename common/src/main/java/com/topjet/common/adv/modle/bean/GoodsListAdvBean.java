package com.topjet.common.adv.modle.bean;

/**
 * Created by yy on 2017/11/10.
 * <p>
 * 货源列表广告 实体类
 */

public class GoodsListAdvBean {
    private String web_title;//	广告标题
    private String text;//广告内容
    private String index_number;//	广告位置
    private String picture_key;//	广告图片key
    private String picture_url;//广告图片url
    private String turn_url;//广告跳转链接
    private boolean isInserted;// 是否已经插入过

    public String getWeb_title() {
        return web_title;
    }

    public String getText() {
        return text;
    }

    public String getIndex_number() {
        return index_number;
    }

    public String getPicture_key() {
        return picture_key;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public String getTurn_url() {
        return turn_url;
    }

    public void setInserted(boolean inserted) {
        isInserted = inserted;
    }

    public boolean isInserted() {
        return isInserted;
    }

    @Override
    public String toString() {
        return "GoodsListAdvBean{" +
                "web_title='" + web_title + '\'' +
                ", text='" + text + '\'' +
                ", index_number='" + index_number + '\'' +
                ", picture_key='" + picture_key + '\'' +
                ", picture_url='" + picture_url + '\'' +
                ", turn_url='" + turn_url + '\'' +
                '}';
    }
}
