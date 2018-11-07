package com.topjet.common.controller;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.topjet.common.App;

/**
 * 实例化通话监听用的类
 * Created by tsj004 on 2017/2/14.
 */
public class PhoneManager {
    private static TelephonyManager phoneManager;

    public static TelephonyManager getTelephonyManager() {
        if (phoneManager == null) {
            synchronized (PhoneManager.class) {
                if (phoneManager == null) {
                    phoneManager = (TelephonyManager) App.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
                }
            }
        }
        return phoneManager;
    }
}
