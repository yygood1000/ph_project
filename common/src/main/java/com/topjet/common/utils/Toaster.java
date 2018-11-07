package com.topjet.common.utils;

import android.content.Context;
import android.widget.Toast;

import com.topjet.common.App;

public class Toaster {

    private static Context sContext;

    //    private static MyToast sToast;
    private static ToasterManager sToast;

//    private static ToastHandler sToastHandler = new ToastHandler();

    public static void init() {
        if (sToast == null) {
            sContext = App.getInstance();
            sToast = new ToasterManager(sContext);
        }
//        if (sToast == null) {
//            sContext = App.getInstance();
//            sToast = new MyToast(sContext);
//        }

    }

//    private static class ToastHandler extends Handler {
//
//        private static final int DELAYED_TOAST = 100;
//
//        private static final int DELAYED_TIME_MILLS = 150;
//
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == DELAYED_TOAST) {
//                showSuperToast((CharSequence) msg.obj, msg.arg1);
//            }
//        }
//
//        public void showDelayedSuperToast(CharSequence text, int duration) {
//            Message msg = obtainMessage();
//            msg.what = DELAYED_TOAST;
//            msg.obj = text;
//            msg.arg1 = duration;
//            sendMessageDelayed(msg, DELAYED_TIME_MILLS);
//        }
//    }

    /**
     * 显示Toast的地方
     *
     * @param text
     * @param duration
     */
    public static void showSuperToast(CharSequence text, int duration) {
//        if (sToast == null) {
//            sToast = new MyToast(sContext);
//        }
//        sToast.setGravity(Gravity.CENTER, 0, 0);//中间显示
//        sToast.setText(text);
//        sToast.setDuration(duration);
//        sToast.show();
    }


    public static void showToast(CharSequence text, int duration, boolean immediateShow) {
//        if (immediateShow) {
//            showSuperToast(text, duration);
//        }
//        else {
//            sToastHandler.showDelayedSuperToast(text, duration);
//        }
        if (sToast == null) {
            sToast = new ToasterManager(sContext);
        }
        if(StringUtils.isEmpty(text)){
            return;
        }
        sToast.showToast(text, duration);
    }

    /**
     * default to show short toast delayed
     */
    public static void showShortToast(CharSequence text) {
        showShortToast(text, false);
    }

    /**
     * default to show short toast delayed
     */
    public static void showShortToast(int resId) {
        showShortToast(sContext.getResources().getString(resId));
    }

    /**
     * choose whether to show the short toast delayed
     */
    public static void showShortToast(CharSequence text, boolean immediateShow) {
        showToast(text, Toast.LENGTH_SHORT, immediateShow);
    }

    /**
     * choose whether to show the short toast delayed
     */
    public static void showShortToast(int resId, boolean immediateShow) {
        showShortToast(sContext.getResources().getString(resId), immediateShow);
    }


    /**
     * default to show long toast delayed
     */
    public static void showLongToast(CharSequence text) {
        showLongToast(text, false);
    }

    /**
     * default to show long toast delayed
     */
    public static void showLongToast(int resId) {
        showLongToast(sContext.getResources().getString(resId));
    }


    /**
     * choose whether to show the long toast delayed
     */
    public static void showLongToast(CharSequence text, boolean immediateShow) {
        showToast(text, Toast.LENGTH_LONG, immediateShow);
    }

    /**
     * choose whether to show the long toast delayed
     */
    public static void showLongToast(int resId, boolean immediateShow) {
        showLongToast(sContext.getResources().getString(resId), immediateShow);
    }


}
