package com.topjet.common.controller;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.utils.Logger;
import com.topjet.common.common.modle.event.ProgressEvent;

/**
 * 下载进度处理。（内容观察者ContentObserver实现下载进度）
 * Created by tsj004 on 2017/8/3.
 */

public class DownloadChangeObserver extends ContentObserver {
    private Context context;
    private long downloadId;
    public DownloadChangeObserver(Context context, Handler handler) {
        super(handler);
        this.context = context;
    }

    /**
     * 观察下载进度核心方法。
     * @param selfChange
     */
    @Override
    public void onChange(boolean selfChange) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final Cursor cursor = dManager.query(query);
        if (cursor != null && cursor.moveToFirst()) {
            final int totalColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            final int currentColumn = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            int totalSize = cursor.getInt(totalColumn);
            int currentSize = cursor.getInt(currentColumn);
            float percent = (float) currentSize / (float) totalSize;
            int progress = Math.round(percent * 100);
            Logger.i("==progress==",""+progress);
            BusManager.getBus().post(new ProgressEvent(true, progress));//把进度发送给前端页面
            if(progress == 100) {
                //下载完成
                Logger.i("==progress==","下载完成");
            }
        }
    }

    public long getDownloadId(){
        return downloadId;
    }

    public void setDownloadId(long downloadId){
        this.downloadId = downloadId;
    }

}
