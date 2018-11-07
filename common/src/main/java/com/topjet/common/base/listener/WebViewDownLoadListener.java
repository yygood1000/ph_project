package com.topjet.common.base.listener;

import android.webkit.DownloadListener;

import com.topjet.common.App;
import com.topjet.common.base.controller.BackGroundController;

/**
 * webview中下载文件监听
 * Created by tsj004 on 2016/11/15.
 */
public class WebViewDownLoadListener implements DownloadListener {

        private String title;
        public void setTitle(String title){
            this.title = title;
        }

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            new BackGroundController(App.getInstance()).downloadApk(title,url);
        }
}