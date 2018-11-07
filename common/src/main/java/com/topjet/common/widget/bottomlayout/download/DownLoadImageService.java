package com.topjet.common.widget.bottomlayout.download;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * creator: zhulunjun
 * time:    2018/3/6
 * describe: 下载图片的线程，这里借用glide下载图片
 */

public class DownLoadImageService implements Runnable {
    private String url, selectUrl;
    private Context context;
    private ImageDownLoadCallBack callBack;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public DownLoadImageService(Context context, String url, String selectUrl, ImageDownLoadCallBack callBack) {
        this.url = url;
        this.selectUrl = selectUrl;
        this.callBack = callBack;
        this.context = context;
    }

    @Override
    public void run() {
        // 可以传入两个url 同时下载两个图片，返回 两个drawable
        File file = null;
        File selectFile = null;
        try {
            file = Glide.with(context)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            selectFile = Glide.with(context)
                    .load(selectUrl)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            final File finalFile = file;
            final File finalFile2 = selectFile;
            // 切换到主线程
            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                    //已在主线程中，可以更新UI
                    if (finalFile != null && finalFile2 != null) {
                        callBack.onDownLoadSuccess(transformFileToDrawable(finalFile), transformFileToDrawable(finalFile2));

                    } else {
                        callBack.onDownLoadFailed();
                    }
                }
            });

        }
    }


    /**
     * 将file转换成Drawable
     * @param file 文件
     * @return Drawable
     */
    private Drawable transformFileToDrawable(File file) {
        Drawable drawable = null;

        if(file.exists()){
            try {
                drawable = BitmapDrawable.createFromPath(file.getAbsolutePath());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (drawable == null){
            Log.i("BitmapToDrawable", "Fail to transform drawable");
        }
        return drawable;
    }

}
