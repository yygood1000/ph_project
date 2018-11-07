package com.topjet.common.widget.bottomlayout;

import android.graphics.drawable.Drawable;

import com.topjet.common.common.modle.bean.TabLayoutInfo;

/**
 * creator: zhulunjun
 * time:    2018/3/12
 * describe: 扩展字段，下载图片的
 */

public class TabLayoutBean extends TabLayoutInfo {



    private Drawable icon_down;
    private Drawable icon_normal;

    public Drawable getIcon_down() {
        return icon_down;
    }

    public void setIcon_down(Drawable icon_down) {
        this.icon_down = icon_down;
    }

    public Drawable getIcon_normal() {
        return icon_normal;
    }

    public void setIcon_normal(Drawable icon_normal) {
        this.icon_normal = icon_normal;
    }

}
