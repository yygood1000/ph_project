package com.topjet.common.utils;

import android.content.Context;
import android.os.Environment;


import com.topjet.common.config.CPersisData;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * creator: zhulunjun
 * time:    2017/11/2
 * describe: 清楚缓存 缓存管理
 */

public class CacheUtils {

    private static CacheUtils instance = null;

    public synchronized static CacheUtils getInstance() {
        if (instance == null) {
            instance = new CacheUtils();
        }
        return instance;
    }

    /**
     * 清楚缓存
     * 1. 清除应用缓存
     * 2. 清除glide图片缓存
     * 3. 清除应用文件夹下的缓存
     */
    public Observable<Boolean> clearCache(final Context context) {

        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                // 应用缓存
                FileUtils.deleteDir(context.getCacheDir());
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    FileUtils.deleteDir(context.getExternalCacheDir());
                }

                // 文件缓存
                FileUtils.deleteDir(new File(PathHelper.Cache()));
                // 清空 搜索记录
                CPersisData.setSearchHistroy("");

                e.onNext(true);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    /**
     * 获取缓存文件大小
     *
     * @param context 页面
     * @return 大小
     * @throws Exception 异常
     */
    public String getTotalCacheSize(Context context) throws Exception {
        // 应用缓存
        long cacheSize = FileUtils.getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += FileUtils.getFolderSize(context.getExternalCacheDir());
        }
        // 文件缓存
        cacheSize += FileUtils.getFolderSize(new File(PathHelper.Cache()));

        return FileUtils.getFormatSize(cacheSize);
    }

}
