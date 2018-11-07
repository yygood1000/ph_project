package com.topjet.common.widget.bottomlayout.download;

import android.graphics.drawable.Drawable;

/**
 * creator: zhulunjun
 * time:    2018/3/6
 * describe: 下载图片的状态回调
 */

public interface ImageDownLoadCallBack {
    void onDownLoadSuccess(Drawable icon, Drawable selectIcon);
    void onDownLoadFailed();
}
