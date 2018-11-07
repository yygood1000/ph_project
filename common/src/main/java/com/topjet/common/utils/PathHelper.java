package com.topjet.common.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.topjet.common.App;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.resource.dict.CommonDataDict;
import com.topjet.common.resource.dict.TruckDataDict;

import java.io.File;

public class PathHelper {

    public static final String APP_DRIVER_ROOT_NAME = "560_Driver";

    public static final String APP_SHIPPER_ROOT_NAME = "560_Shipper";

    public static String APP_ROOT_NAME = APP_DRIVER_ROOT_NAME;

    /**
     * @return EG: /storage/emulated/0/560Driver
     */
    public static String globalExternal() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APP_ROOT_NAME;
    }

    /**
     * @return EG:/storage/emulated/0/Pictures
     */
    public static String externalPublicPictures() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    /**
     * 缓存文件
     *
     * @return 缓存文件 根地址
     */
    public static String Cache() {
        return globalExternal() + "/Cache";
    }

    /**
     * 图片截取后储存地址
     *
     * @return /storage/emulated/0/560Driver/Cache/Pictures
     */
    public static String externalPictures() {
        return globalExternal() + "/Cache/Pictures";
    }

    /**
     * 网络图片加载缓存地址
     *
     * @return /storage/emulated/0/560Driver/Cache/GlideIamge
     */
    public static String getImageCachePath() {
        return globalExternal() + "/Cache/GlideIamge";
    }


    /**
     * 分享货源图片储存地址
     *
     * @return /storage/emulated/0/560Driver/Cache/shareImage
     */
    public static String getShareImage() {
        return globalExternal() + "/Cache/shareImage";
    }

    /**
     * 更新APK文件下载储存地址
     *
     * @return /storage/emulated/0/560Driver/Cache/apk
     */
    public static String getApkPath() {
        return globalExternal() + "/Cache/apk";
    }

    /**
     * 系统相机拍照储存地址
     *
     * @return /storage/emulated/0/560Driver/Camera
     */
    public static String camera() {
        return globalExternal() + "/Camera";
    }

    /**
     * 奔溃日志地址
     *
     * @return /storage/emulated/0/560Driver/FC
     */
    public static String externalFCLog() {
        return globalExternal() + "/FC";
    }


    public static Uri getRawResourceUri(int resId) {
        String uriString = "android.resource://" + App.getInstance().getPackageName() + "/" + resId;
        return Uri.parse(uriString);
    }

    /*=================================资源文件SD卡位置=================================*/

    /**
     * 资源文件SD卡储存根地址
     *
     * @return /storage/emulated/0/Android/data/com.topjet.crediblenumber/cache/resource/config/
     */
    private static String appResourceConfig(Context mContext) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = mContext.getExternalCacheDir().getAbsolutePath();
        } else {
            cachePath = mContext.getCacheDir().getPath();

        }
        return cachePath + "/resource/config";
//        return mContext.getExternalCacheDir().getAbsolutePath() + "/resource/config";
    }

    //车型文件地址 @return /storage/emulated/0/Android/data/com.topjet.crediblenumber/cache/resource/config/truckType.json
    public static String getTruckTypePath(Context mContext) {
        return PathHelper.appResourceConfig(mContext) + File.separator + TruckDataDict.TRUCK_TYPE;
    }

    //车长文件地址 @return /storage/emulated/0/Android/data/com.topjet.crediblenumber/cache/resource/config/truckLength.json
    public static String getTruckLengthPath(Context mContext) {
        return PathHelper.appResourceConfig(mContext) + File.separator + TruckDataDict.TRUCK_LEN;
    }

    //城市列表文件地址  @return /storage/emulated/0/Android/data/com.topjet.crediblenumber/cache/resource/config/cities.json
    public static String getCitiesPath(Context mContext) {
        Logger.e("oye", "path == " + PathHelper.appResourceConfig(mContext) + File.separator + AreaDataDict.CITIES);
        return PathHelper.appResourceConfig(mContext) + File.separator + AreaDataDict.CITIES;
    }

    //货物类型文件地址 @return /storage/emulated/0/Android/data/com.topjet.crediblenumber/cache/resource/config/goodName.json
    public static String getGoodsNamePath(Context mContext) {
        return PathHelper.appResourceConfig(mContext) + File.separator + CommonDataDict.GOODS_TYPE_FILE_NAME;
    }

    //包装方式文件地址 @return /storage/emulated/0/Android/data/com.topjet.crediblenumber/cache/resource/config/packagetype.json
    public static String getPackgeTypePath(Context mContext) {
        return PathHelper.appResourceConfig(mContext) + File.separator + CommonDataDict.PACKAGE_TYPE_FILE_NAME;
    }

    //装卸方式文件地址 @return /storage/emulated/0/Android/data/com.topjet.crediblenumber/cache/resource/config/loadtype.json
    public static String getLoadTypePath(Context mContext) {
        return PathHelper.appResourceConfig(mContext) + File.separator + CommonDataDict.LOAD_TYPE_FILE_NAME;
    }

    //装卸方式文件地址 @return /storage/emulated/0/Android/data/com.topjet.crediblenumber/cache/resource/config/orderunit.json
    public static String getUnitPath(Context mContext) {
        return PathHelper.appResourceConfig(mContext) + File.separator + CommonDataDict.GOODS_UNIT_FILE_NAME;
    }

    /*=================================资源文件SD卡位置=================================*/

}
