package com.topjet.common.common.view.activity;

import android.graphics.Bitmap;

import com.topjet.common.base.view.activity.IView;

/**
 * creator: zhulunjun
 * time:    2017/10/23
 * describe: 分享下载
 */

public interface ShareDownloadView extends IView {
    void setQrCode(Bitmap bitmap);
}
