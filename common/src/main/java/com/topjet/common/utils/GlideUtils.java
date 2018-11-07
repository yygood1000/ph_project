package com.topjet.common.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.topjet.common.App;
import com.topjet.common.config.MyGlideUrl;
import com.topjet.common.controller.GlideCircleTransform;
import com.topjet.common.controller.GlideRoundTransform;

import java.io.File;

/**
 * Created by yy on 2017/7/28.
 * <p>
 * 一些没有设置的方法列举
 * thumbnail(0.1f)动态占位图，10%的缩略图
 * <p>
 * crossFade(500)渐显动画，单位毫秒，默认是300
 * <p>
 * dontAnimate()直接显示图片
 * <p>
 * override(600,200)指定图片加载大小 单位px
 * <p>
 * skipMemoryCache( true )禁用内存缓存
 * <p>
 * diskCacheStrategy()磁盘缓存设置，由枚举控制
 * DiskCacheStrategy.NONE 啥也不缓存，禁用磁盘缓存时可用
 * DiskCacheStrategy.SOURCE 只缓存全尺寸图.
 * DiskCacheStrategy.RESULT 只缓存最终降低分辨后用到的图片
 * DiskCacheStrategy.ALL 缓存所有类型的图片 (默认行为)
 * <p>
 * priority() 控制图片请求优先级，参数Priority.LOW/NORMAL/HIGH/IMMEDIATE
 * <p>
 * animate()可以定制动画，参数可是android.R.anim.slide_in_left或者ViewPropertyAnimation.Animator类
 * <p>
 * transform()变换图片方法，如圆形，圆角，模糊，旋转等等
 * <p>
 * 加载String类型的资源
 * SD卡资源："file://"+ Environment.getExternalStorageDirectory().getPath()+"/test.jpg"<p/>
 * assets资源："file:///android_asset/f003.gif"<p/>
 * raw资源："Android.resource://com.frank.glide/raw/raw_1"或"android.resource://com.frank.glide/raw/"+R.raw.raw_1<p/>
 * drawable资源："android.resource://com.frank.glide/drawable/news"
 * 或load"android.resource://com.frank.glide/drawable/"+R.drawable.news<p/>
 * ContentProvider资源："content://media/external/images/media/139469"<p/>
 * http资源："http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg"<p/>
 * https资源："https://img.alicdn.com/tps/TB1uyhoMpXXXXcLXVXXXXXXXXXX-476-538.jpg_240x5000q50.jpg_.webp"<p/>
 * <p>
 * 资料来自：
 * http://www.jianshu.com/p/7610bdbbad17
 * http://blog.csdn.net/guolin_blog/article/details/53759439?utm_source=tuicool&utm_medium=referral
 */

public class GlideUtils {

    /**
     * 创建gradle
     */
    private static RequestManager CreatedGlide() {
        return Glide.with(App.getInstance().getBaseContext());
    }

    /**
     * 取消gradle请求,由于上下文使用的是全局的，所以此方法会暂停整个APP 的图片请求。
     */
    public static void pauseRequests() {
        Glide.with(App.getInstance().getBaseContext()).pauseRequests();
    }

    /**
     * 重新开始加载图片
     */
    public static void resumeRequests() {
        Glide.with(App.getInstance().getBaseContext()).pauseRequests();
    }


    /**
     * 默认的加载方法
     */
    public static void loaderImage(String url, String key, int errorImg, int loadingImg, ImageView
            iv, GlideDrawableImageViewTarget target) {
        if (checkUrlAndKey(url, key)) {
            if (target == null) {
                CreatedGlide()
                        .load(new MyGlideUrl(url, key))
                        .placeholder(loadingImg)
                        .error(errorImg)
                        .into(iv);
            } else {
                CreatedGlide()
                        .load(new MyGlideUrl(url, key))
                        .placeholder(loadingImg)
                        .error(errorImg)
                        .into(target);
            }
        }

    }

    /**
     * 默认的加载方法
     */
    public static void loaderImage(String url, String key, int errorImg, int loadingImg, ImageView iv) {
        loaderImage(url, key, errorImg, loadingImg, iv, null);
    }

    /**
     * 无占位图加载方法
     */
    public static void loaderImage(String url, String key, ImageView iv) {
        loaderImage(url, key, -100, -100, iv, null);
    }

    /**
     * 加载圆形图片
     */
    public static void loaderImageCircle(Context context, String url, String key, int errorImg, int loadingImg,
                                         ImageView iv) {
        if (checkUrlAndKey(url, key)) {
            CreatedGlide()
                    .load(new MyGlideUrl(url, key))
                    .placeholder(loadingImg)
                    .error(errorImg)
                    .transform(new CenterCrop(App.getInstance()), new GlideCircleTransform(context))
                    .into(iv);
        }
    }

    /**
     * 加载圆形图片，没有key，直接用url加载
     * 1.滑动按钮组使用
     */
    public static void loaderImageCircle(Context context, String url, int errorImg, int loadingImg,
                                         ImageView iv) {
        CreatedGlide()
                .load(url)
                .placeholder(loadingImg)
                .error(errorImg)
                .transform(new GlideCircleTransform(context))
                .into(iv);
    }

    /**
     * 加载圆角图片
     *
     * @param roundSize 圆角值 单位dp
     */
    public static void loaderImageRound(Context context, String url, String key, int errorImg, int loadingImg,
                                        ImageView iv, int roundSize) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(url)) {
            return;
        }
        CreatedGlide()
                .load(new MyGlideUrl(url, key))
                .placeholder(loadingImg)
                .error(errorImg)
                .transform(new CenterCrop(context), new GlideRoundTransform(context, roundSize))
                .into(iv);
    }

    /**
     * 加载圆角图片，并设置图片尺寸
     *
     * @param roundSize 圆角值 单位dp,并且指定图片显示尺寸
     */
    public static void loaderImageRoundWithSize(Context context, String url, String key, int errorImg, int loadingImg,
                                                ImageView iv, int roundSize, int width, int height) {
        if (checkUrlAndKey(url, key)) {
            CreatedGlide()
                    .load(new MyGlideUrl(url, key))
                    .placeholder(loadingImg)
                    .error(errorImg)
                    .fallback(errorImg)
                    .override(width, height)
                    .transform(new CenterCrop(context), new GlideRoundTransform(context, roundSize))
                    .into(iv);
        } else {
            CreatedGlide().load(loadingImg).into(iv);
        }
    }

    /**
     * 加载圆角图片，并设置尺，不设置加占位图
     *
     * @param roundSize 圆角值 单位dp,并且指定图片显示尺寸
     */
    public static void loaderImageRoundWithSize(Context context, String url, String key,
                                                ImageView iv, int roundSize, int width, int height) {
        if (checkUrlAndKey(url, key)) {
            CreatedGlide()
                    .load(new MyGlideUrl(url, key))
                    .override(width, height)
                    .transform(new CenterCrop(context), new GlideRoundTransform(context, roundSize))
                    .into(iv);
        }
    }

    /**
     * 加载圆角图片，并设置尺寸，不需要key，不设置加载中图片，以及加载失败图片
     *
     * @param roundSize 圆角值 单位dp,并且指定图片显示尺寸
     */
    public static void loaderImageRoundWithSize(Context context, String url,
                                                ImageView iv, int roundSize, int width, int height) {
        if (StringUtils.isEmpty(url)) {
            return;
        }
        CreatedGlide()
                .load(url)
                .override(width, height)
                .transform(new CenterCrop(context), new GlideRoundTransform(context, roundSize))
                .into(iv);
    }

    /**
     * 加载Gif图
     */
    public static void loadGifImage(Context context, String url, String key, int errorImg, int loadingImg, ImageView
            iv) {
        CreatedGlide()
                .load(url)
                .asGif()// 只允许加载动态图，如果传入静态图的url，会显示加载失败占位图
                .placeholder(loadingImg)// 添加加载中占位图
                .error(errorImg)// 失败占位图
                .skipMemoryCache(true)// 禁用内存缓存
                .into(iv);
    }

    /**
     * 加载启动页面图片
     */
    /**
     * 通过回调进行图片设置
     * Target的详细用法请阅读http://www.jianshu.com/p/ffc89f091682
     */
    public static void loadSplashImage(String url, String key, SimpleTarget<GlideDrawable> target) {
        CreatedGlide()
                .load(new MyGlideUrl(url, key))
                .transform(new CenterCrop(App.getInstance()))
                .into(target);
    }

    /**
     * 加载启动页面图片
     * 通过回调进行图片设置
     * http://blog.csdn.net/lv_fq/article/details/69487709
     */
    public static void loadSplashGif(String url, String key, GlideDrawableImageViewTarget target) {
        CreatedGlide()
                .load(new MyGlideUrl(url, key))
                .into(target);
    }

    /**
     * 加载本地资源
     */
    public static void loaderImageResource(Integer resourceId, ImageView iv) {
        CreatedGlide()
                .load(resourceId)
                .into(iv);
    }


    /**
     * 播放本地视频
     */
    public static void playVideo(Context context, String filePath, ImageView imageView) {
        Glide
                .with(context)
                .load(Uri.fromFile(new File(filePath)))
                .into(imageView);
    }

    /**
     * 清除磁盘缓存
     * <p>
     * 清除SD卡缓存必须在子线程中
     */
    private static void clearDiskCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }

    /**
     * 检查图片地址与key
     */
    private static boolean checkUrlAndKey(String url, String key) {
        return !(StringUtils.isEmpty(key) || StringUtils.isEmpty(url));
    }

    /**
     * 清除所有缓存
     */
    public static void cleanAll(Context context) {
        clearDiskCache(context);
        Glide.get(context).clearMemory();
    }

    /**
     * 暂停所有正在下载或等待下载的任务。
     */
    public static void cancelAllTasks() {
        CreatedGlide().pauseRequests();
    }

    /**
     * 恢复所有任务
     */
    public static void resumeAllTasks() {
        CreatedGlide().resumeRequests();
    }

    /**
     * 获取缓存大小
     */
    public static synchronized long getDiskCacheSize(Context context) {
        long size = 0L;
        File cacheDir = new File(PathHelper.getImageCachePath());

        if (cacheDir.exists()) {
            File[] files = cacheDir.listFiles();
            if (files != null) {
                int len$ = files.length;

                for (int i$ = 0; i$ < len$; ++i$) {
                    File imageCache = files[i$];
                    if (imageCache.isFile()) {
                        size += imageCache.length();
                    }
                }
            }
        }
        return size;
    }
}
