package com.topjet.common.config;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.topjet.common.utils.PathHelper;

/**
 * Created by yy on 2017/7/28.
 * GlideUtils 配置
 * 需要在Manifest 中配置
 * 如果以后要做混淆需要在混淆文件中配置
 */

public class CustomGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        setDiskCacheOptions(builder); // 配置SD卡缓存
        setMemoryCacheOptions(context, builder);// 配置内存缓存
        setDecodeFormat(builder);// 配置图片加载质量
    }

    private GlideBuilder setDecodeFormat(GlideBuilder builder) {
        return builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }

    /**
     * 配置磁盘缓存
     */
    private void setDiskCacheOptions(GlideBuilder builder) {
        // 最大缓存大小 100M
        int cacheSize100MegaBytes = 104857600;
        // storage/emulated/0/560Driver/Cache/GlideIamge
        String customDiskCachePath = PathHelper.getImageCachePath();
        //设置磁盘缓存目录
        builder.setDiskCache(new DiskLruCacheFactory(customDiskCachePath, cacheSize100MegaBytes));
    }

    /**
     * 配置内存缓存
     */
    private void setMemoryCacheOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();// 默认内存缓存大小
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();//默认内存缓存bitmap池大小

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);// 以默认值的1.2倍进行设置
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);// 以默认值的1.2倍进行设置

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
    }
}
