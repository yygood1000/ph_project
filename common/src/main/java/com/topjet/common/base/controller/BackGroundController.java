package com.topjet.common.base.controller;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import com.topjet.common.config.CMemoryData;
import com.topjet.common.controller.DownloadChangeObserver;
import com.topjet.common.utils.DisplayUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.PathHelper;

import java.io.File;

/**
 * Created by wangshuo on 2015/9/15.
 */
public class BackGroundController {
    public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");

    private Context mContext;

    public BackGroundController(Context context) {
        this.mContext = context;
    }


    @SuppressWarnings("static-access")
    public void downloadApk(String url) {
        DownloadManager.Request request = null;
        try {
            request = new DownloadManager.Request(Uri.parse(url));
        } catch (Exception e) {
            Logger.d("", e);
            return;
        }

        request.setTitle(DisplayUtils.getCNVersionName());
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        /**
         * java.lang.IllegalStateException: Unable to create directory: /storage/4623-58D9/560_Shipper
         * at android.app.DownloadManager$Request.setDestinationInExternalPublicDir(DownloadManager.java:539)
         * at com.topjet.common.logic.BackGroundLogic.downloadApk(BackGroundLogic.java:156)
         *
         * 如果上级目录不存在就会新建文件夹异常。所以下载前我们最好自己调用File的mkdirs方法递归创建子目录
         * 这里创建文件夹有问题
         */

        try {
            //设置外部存储的公共目录
            request.setDestinationInExternalPublicDir(PathHelper.getApkPath(), getApkName(url));
        } catch (Exception e) {

        }

        request.setMimeType("application/vnd.android.package-archive");

        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
        /**
         *  java.lang.IllegalArgumentException:
         *  Unknown URL content://downloads/my_downloads
         *  at android.content.ContentResolver.insert(ContentResolver.java:1256)
         *  at android.app.DownloadManager.enqueue(DownloadManager.java:1336)
         *  at com.topjet.common.logic.BackGroundLogic.downloadApk(BackGroundLogic.java:160)
         *
         *  参数异常，可能是因为上一个异常的影响
         *
         *   java.lang.NullPointerException:
         *   Attempt to invoke virtual method 'java.lang.String android.net.Uri.getLastPathSegment()'
         *   on a null object reference
         *   at android.app.DownloadManager.enqueue(DownloadManager.java:948)
         */
        try {
            long downloadId = downloadManager.enqueue(request);

            CMemoryData.setApkDownloadId(downloadId);
        } catch (Exception e) {
            //这地方错误蛮多的 ,如果try 会终止，无法下载
            e.printStackTrace();
        }
    }

    /**
     * 可以展示下载进度。
     *
     * @param url：下载地址
     * @param downloadObserver：下载进度处理。（内容观察者ContentObserver实现下载进度）
     */
    @SuppressWarnings("static-access")
    public void downloadApk(String url, DownloadChangeObserver downloadObserver) {
        DownloadManager.Request request = null;
        try {
            request = new DownloadManager.Request(Uri.parse(url));
        } catch (Exception e) {
            Logger.d("", e);
            return;
        }

        request.setTitle(DisplayUtils.getCNVersionName());
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        /**
         * java.lang.IllegalStateException: Unable to create directory: /storage/4623-58D9/560_Shipper
         * at android.app.DownloadManager$Request.setDestinationInExternalPublicDir(DownloadManager.java:539)
         * at com.topjet.common.logic.BackGroundLogic.downloadApk(BackGroundLogic.java:156)
         *
         * 如果上级目录不存在就会新建文件夹异常。所以下载前我们最好自己调用File的mkdirs方法递归创建子目录
         * 这里创建文件夹有问题
         */

        try {
            //设置外部存储的公共目录
            request.setDestinationInExternalPublicDir(PathHelper.getApkPath(), getApkName(url));
        } catch (Exception e) {

        }

        request.setMimeType("application/vnd.android.package-archive");

        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
        /**
         *  java.lang.IllegalArgumentException:
         *  Unknown URL content://downloads/my_downloads
         *  at android.content.ContentResolver.insert(ContentResolver.java:1256)
         *  at android.app.DownloadManager.enqueue(DownloadManager.java:1336)
         *  at com.topjet.common.logic.BackGroundLogic.downloadApk(BackGroundLogic.java:160)
         *
         *  参数异常，可能是因为上一个异常的影响
         *
         *   java.lang.NullPointerException:
         *   Attempt to invoke virtual method 'java.lang.String android.net.Uri.getLastPathSegment()'
         *   on a null object reference
         *   at android.app.DownloadManager.enqueue(DownloadManager.java:948)
         */
        try {
            long downloadId = downloadManager.enqueue(request);
            CMemoryData.setApkDownloadId(downloadId);

            if (mContext != null) {
                downloadObserver.setDownloadId(downloadId);
                mContext.getContentResolver().registerContentObserver(CONTENT_URI, true, downloadObserver);
            }
        } catch (Exception e) {
            //这地方错误蛮多的 ,如果try 会终止，无法下载
            e.printStackTrace();
        }
    }

    @SuppressWarnings("static-access")
    public void downloadApk(String title, String url) {
        DownloadManager.Request request = null;
        try {
            request = new DownloadManager.Request(Uri.parse(url));
        } catch (Exception e) {
            Logger.d("", e);
            return;
        }

        request.setTitle(title);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }

        try {
            //设置外部存储的公共目录
            request.setDestinationInExternalPublicDir(PathHelper.getApkPath(), getApkName(url));
        } catch (Exception e) {

        }

        request.setMimeType("application/vnd.android.package-archive");

        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
        try {
            long downloadId = downloadManager.enqueue(request);

            CMemoryData.setApkDownloadId(downloadId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //创建文件夹
    private boolean getDir(String pathName) {
        File folder = new File(pathName);
        return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
    }

    private String getApkName(String apkUrl) {
        try {
            return apkUrl.substring(apkUrl.lastIndexOf("/") + 1);
        } catch (Exception e) {
            return "";
        }
    }


}
