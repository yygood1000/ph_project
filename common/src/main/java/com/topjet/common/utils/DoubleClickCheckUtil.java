package com.topjet.common.utils;

import android.view.View;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by tsj-004 on 2017/11/29.
 *
 * 防止过快点击
 */

public class DoubleClickCheckUtil {
    static int INTERVAL_TIME = 1500;        // 间隔时间
    static int MAX_INFO_SIZE = 20;          // 点击信息存储个数
    static List<ClickInfo> clickInfoList = null;

    static {
        clickInfoList = new LinkedList<>();
    }

    /**
     * 推荐这种方式使用
     *
     * @return true 点击无效
     * false 点击有效，并记录当前时间
     *
     * @OnClick({R.id.tv_yes, R.id.tv_no})  传入 view.getId即可
     * @OnClick(R.id.tv_yes) 请传入R.id.tv_yes
     */
    public static boolean check(String className, int viewId) {
        String classNameAndViewId = className + viewId;
        int index = -1;
        for (int i = 0; i < clickInfoList.size(); i++) {
            if (clickInfoList.get(i).getClassNameAndViewId().equals(classNameAndViewId)) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            ClickInfo tempInfo = clickInfoList.get(index);
            long tempTime = tempInfo.getClickTime();
            if (System.currentTimeMillis() - tempTime < INTERVAL_TIME) {
                return true;
            } else {
                tempInfo.setClickTime(System.currentTimeMillis());
                clickInfoList.set(index, tempInfo);
            }
        } else {
            ClickInfo tempInfo = new ClickInfo(classNameAndViewId, System.currentTimeMillis());
            clickInfoList.add(tempInfo);
        }

        if (clickInfoList.size() > MAX_INFO_SIZE) {
            clickInfoList.remove(0);
        }
        return false;
    }

    public static boolean check(String className, View view) {
        return check(className, view);
    }

    public static boolean check(Class clazz, int viewId) {
        return check(clazz.getName(), viewId);
    }

    public static boolean check(Class clazz, View view) {
        return check(clazz.getName(), view.getId());
    }

    /**
     * 点击信息
     */
    static class ClickInfo {
        private String classNameAndViewId = null;
        private long clickTime = 0;

        public ClickInfo(String classNameAndViewId, long clickTime) {
            this.classNameAndViewId = classNameAndViewId;
            this.clickTime = clickTime;
        }

        public String getClassNameAndViewId() {
            return classNameAndViewId;
        }

        public void setClassNameAndViewId(String classNameAndViewId) {
            this.classNameAndViewId = classNameAndViewId;
        }

        public long getClickTime() {
            return clickTime;
        }

        public void setClickTime(long clickTime) {
            this.clickTime = clickTime;
        }
    }
}