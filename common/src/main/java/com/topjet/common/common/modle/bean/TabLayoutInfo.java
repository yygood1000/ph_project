package com.topjet.common.common.modle.bean;

import android.graphics.drawable.Drawable;

import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.bottomlayout.TabLayoutBean;

import java.io.Serializable;

/**
 * Created by tsj028 on 2017/12/1 0001.
 */

public class TabLayoutInfo implements Serializable {
    public String title;
    public String title_color_down;
    public String title_color_normal;
    public String icon_down_key;
    public String icon_down_url;
    public String icon_normal_key;
    public String icon_normal_url;
    public String begin_date;
    public String end_date;

    public TabLayoutBean getBean(){
        TabLayoutBean bean = new TabLayoutBean();
        bean.setTitle(title);
        bean.setTitle_color_down(title_color_down);
        bean.setTitle_color_normal(title_color_normal);
        bean.setIcon_down_key(icon_down_key);
        bean.setIcon_down_url(icon_down_url);
        bean.setIcon_normal_key(icon_normal_key);
        bean.setIcon_normal_url(icon_normal_url);
        bean.setBegin_date(begin_date);
        bean.setEnd_date(end_date);

        return bean;
    }

    /**
     * 所有的值都不为空
     * @return 是否为空
     */
    public boolean isFullNotNull(){
        return StringUtils.isNotBlank(title) &&
                StringUtils.isNotBlank(title_color_down) &&
                StringUtils.isNotBlank(title_color_normal) &&
                StringUtils.isNotBlank(icon_down_key) &&
                StringUtils.isNotBlank(icon_down_url) &&
                StringUtils.isNotBlank(icon_normal_key) &&
                StringUtils.isNotBlank(icon_normal_url) &&
                StringUtils.isNotBlank(begin_date) &&
                StringUtils.isNotBlank(end_date);

    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_color_down() {
        return title_color_down;
    }

    public void setTitle_color_down(String title_color_down) {
        this.title_color_down = title_color_down;
    }

    public String getTitle_color_normal() {
        return title_color_normal;
    }

    public void setTitle_color_normal(String title_color_normal) {
        this.title_color_normal = title_color_normal;
    }

    public String getIcon_down_key() {
        return icon_down_key;
    }

    public void setIcon_down_key(String icon_down_key) {
        this.icon_down_key = icon_down_key;
    }

    public String getIcon_down_url() {
        return icon_down_url;
    }

    public void setIcon_down_url(String icon_down_url) {
        this.icon_down_url = icon_down_url;
    }

    public String getIcon_normal_key() {
        return icon_normal_key;
    }

    public void setIcon_normal_key(String icon_normal_key) {
        this.icon_normal_key = icon_normal_key;
    }

    public String getIcon_normal_url() {
        return icon_normal_url;
    }

    public void setIcon_normal_url(String icon_normal_url) {
        this.icon_normal_url = icon_normal_url;
    }

    @Override
    public String toString() {
        return "TabLayoutInfo{" +
                "title='" + title + '\'' +
                ", title_color_down='" + title_color_down + '\'' +
                ", title_color_normal='" + title_color_normal + '\'' +
                ", icon_down_key='" + icon_down_key + '\'' +
                ", icon_down_url='" + icon_down_url + '\'' +
                ", icon_normal_key='" + icon_normal_key + '\'' +
                ", icon_normal_url='" + icon_normal_url + '\'' +
                ", begin_date='" + begin_date + '\'' +
                ", end_date='" + end_date + '\'' +
                '}';
    }
}
