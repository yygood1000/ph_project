package com.topjet.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.topjet.common.App;
import com.topjet.common.config.CMemoryData;
import com.umeng.analytics.MobclickAgent;

import java.util.UUID;

//import com.umeng.analytics.MobclickAgent;

public class SystemUtils {
    /**
     * killing app from process level
     *
     * @param context
     */
    public static void killProcess(Context context) {
        /**
         //调用android.os.Process.killProcess(android.os.Process.myPid());和System.exit(0);
         //系统判定程序异常关闭，会尝试重新启动
         //如果退出进程前先关闭所有的activity
         //这样系统就不会重新启动入口activity
         //不会出现多次报错的问题
         */

        MobclickAgent.onKillProcess(context);//用来保存统计数据
        AppManager.getInstance().finishAllActivity();

        android.os.Process.killProcess(android.os.Process.myPid());

        System.exit(0);
    }

    public static void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    @SuppressLint("MissingPermission")
    public static String getIMSI() {
        TelephonyManager telephonyManager = (TelephonyManager) App.getInstance().getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSubscriberId();
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = tm.getDeviceId() + "";
        tmSerial = tm.getSimSerialNumber() + "";
        androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider
                .Settings.Secure.ANDROID_ID) + "";

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    private static String bytes2Hex(byte[] bytes) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bytes.length; i++) {
            tmp = (Integer.toHexString(bytes[i] & 0xFF));
            if (tmp.length() == 1)
                des += "0";
            des += tmp;
        }
        return des;
    }

    /**
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageInfo packInfo = getPackageInfo(context);
        if (packInfo != null) {
            return packInfo.versionCode;
        } else {
            return -1;
        }
    }

    /**
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageInfo packInfo = getPackageInfo(context);
        if (packInfo != null) {
            return packInfo.versionName;
        } else {
            return "未知";
        }
    }

    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getApkName() {
        String apkName;
        if (CMemoryData.isDriver()) {
            apkName = "560Driver_v";
        } else {
            apkName = "560Shipper_v";
        }
        String suffixName = SystemUtils.getVersionName(App.getInstance());
        apkName += suffixName.replace(".", "") + ".apk";
        return apkName;
    }

    public static String getAppName() {
        String appName;
        if (CMemoryData.isDriver()) {
            appName = "560接货版";
        } else {
            appName = "560发货版";
        }
        return appName;
    }

    /**
     * 判断app是否已安装
     */
    public static boolean isPackageExisted(String targetPackage) {
        PackageManager pm = App.getInstance().getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    private static int sExtraNameIndex = 10086;
    public static String nextExtraName() {
        return "sExtraNameIndex_ext_" + (++sExtraNameIndex);
    }
}
