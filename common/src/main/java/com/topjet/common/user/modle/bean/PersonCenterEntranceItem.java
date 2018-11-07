package com.topjet.common.user.modle.bean;

import com.topjet.common.base.model.BaseExtra;

/**
 * Created by tsj-004 on 2017/10/23.
 * <p>
 * 个人中心功能入口
 */

public class PersonCenterEntranceItem {
    private String statusStr = null;        // 状态
    private int drawableResourceId = 0;     // 图标
    private String contentStr = null;       // 文字
    private Class activityClass = null;     // 跳转类
    private BaseExtra extra = null;          // 传值

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public int getDrawableResourceId() {
        return drawableResourceId;
    }

    public void setDrawableResourceId(int drawableResourceId) {
        this.drawableResourceId = drawableResourceId;
    }

    public String getContentStr() {
        return contentStr;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;
    }

    public Class getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class activityClass) {
        this.activityClass = activityClass;
    }

    public BaseExtra getExtra() {
        return extra;
    }

    public void setExtra(BaseExtra extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "PersonCenterEntranceItem{" +
                "statusStr='" + statusStr + '\'' +
                ", drawableResourceId=" + drawableResourceId +
                ", contentStr='" + contentStr + '\'' +
                ", activityClass=" + activityClass +
                ", extra=" + extra +
                '}';
    }
}