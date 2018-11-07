package com.topjet.common.config;

import com.google.gson.Gson;
import com.topjet.common.common.modle.bean.AroundMapExtra;
import com.topjet.common.common.modle.bean.DestinationListItem;
import com.topjet.common.common.modle.bean.LocationInfo;
import com.topjet.common.common.modle.bean.UsualCityBean;
import com.topjet.common.common.modle.response.GetUserInfoAtHomeResponse;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;

import java.util.ArrayList;

/**
 * Memory data
 */
public class CMemoryData {

    /**
     * APP版本
     */
    private static boolean isDriver;
    /**
     * Session id
     */
    private static String sessionId = CPersisData.getUserSession();

    /**
     * Session 是否有效
     */
    private static boolean isSessionInvalid = false;

    /**
     * 版本更新APK下载ID
     */
    private static long apkDownloadId;

    /*=================================用户信息相关字段start===================================*/
    /**
     * 用户手机号
     */
    private static String userMobile;
    /**
     * 用户id
     */
    private static String userId;
    /*=================================用户信息相关字段end===================================*/

    /**
     * 定位信息
     */
    private static LocationInfo mLocationInfo;

    /**
     * 临时的goodsId
     * <p>
     * 给他下单，附带goodsId时使用
     */
    private static String tempGoodsId = null;

    /**
     * 常跑城市集合
     */
    private static ArrayList<UsualCityBean> usualCityDatas = new ArrayList<>();
    /**
     * 附近货源 目的地显示数据集
     */
    private static ArrayList<DestinationListItem> destinationCityDatas = new ArrayList<>();

    /**
     * 附近货源 共享字段信息
     */
    private static AroundMapExtra aroundMapExtra;

    /**
     * 听单是否是开启状态
     */
    private static boolean isOrderOpen = false;

    /**
     * 用户参数
     */
    private static GetUserInfoAtHomeResponse userBaseInfo = new GetUserInfoAtHomeResponse();

    /**
     * 已经打开登录页，不要再次打开
     */
    private static long startLoginPageTime = 0;

    /**
     * 已经打开停机维护弹窗
     */
    private static boolean showMaintainBeanDialog = false;

    /**
     * 是否已经显示升级弹窗
     */
    private static boolean isShowedUpdataDialog = false;


    /**
     * =========================================================================
     * ==============================API方法——开始==============================
     * =========================================================================
     */
    public static boolean isDriver() {
        return isDriver;
    }

    public static void setDriver(boolean isDriver) {
        CMemoryData.isDriver = isDriver;
    }

    /* ======================是否已经显示升级弹窗======================*/

    public static boolean isIsShowedUpdataDialog() {
        return isShowedUpdataDialog;
    }

    public static void setIsShowedUpdataDialog() {
        CMemoryData.isShowedUpdataDialog = true;
    }

    /* ======================是否已经显示升级弹窗======================*/
    /* ======================已经打开停机维护弹窗======================*/
    public static boolean isShowMaintainBeanDialog() {
        return showMaintainBeanDialog;
    }

    public static void setShowMaintainBeanDialog(boolean showMaintainBeanDialog) {
        CMemoryData.showMaintainBeanDialog = showMaintainBeanDialog;
    }
    /* ======================已经打开停机维护弹窗======================*/

    // 如果内存里用户Session为空，就从SP里拿
    public static String getSessionId() {
        if (StringUtils.isNotBlank(sessionId)) {
            return sessionId;
        } else {
            sessionId = CPersisData.getUserSession();
            if (StringUtils.isNotBlank(sessionId)) {
                return sessionId;
            }
        }

        if(StringUtils.isEmpty(sessionId)){
            CPersisData.setKey("");
            Logger.d("数据加密 key置空");
        }
        return sessionId;
    }

    /**
     * 将用户Session保存在内存中，且也存一份在SP中
     */
    public static void setSessionId(String sessionId) {
        CMemoryData.sessionId = sessionId;
        CPersisData.setUserSession(sessionId);
    }

    public static boolean isSessionInvalid() {
        return isSessionInvalid;
    }

    public static void setIsSessionInvalid(boolean isSessionInvalid) {
        CMemoryData.isSessionInvalid = isSessionInvalid;
    }

    /* ======================设置用户信息start======================*/

    public static void setUserMobile(String userMobile) {
        CMemoryData.userMobile = userMobile;
    }

    public static void setUserId(String userId) {
        CMemoryData.userId = userId;
    }


    public static String getUserMobile() {
        return StringUtils.isNotBlank(userMobile) ? userMobile : (userMobile = CPersisData.getUserMobile());
    }

    public static String getUserId() {
        return StringUtils.isNotBlank(userId) ? userId : (userId = CPersisData.getUserID());
    }

    /* ======================设置用户信息end======================*/
    /* ======================听单开启状态======================*/

    public static boolean isOrderOpen() {
        return isOrderOpen;
    }

    public static void setIsOrderOpen(boolean isOrderOpen) {
        CMemoryData.isOrderOpen = isOrderOpen;
    }

    /* ======================听单开启状态======================*/
    /* ======================Apk下载Id======================*/

    public static long getApkDownloadId() {
        return apkDownloadId;
    }

    public static void setApkDownloadId(long apkDownloadId) {
        CMemoryData.apkDownloadId = apkDownloadId;
    }

    /* ======================Apk下载Id======================*/
    /* ======================常跑城市列表数据======================*/

    public static ArrayList<UsualCityBean> getUsualCityDatas() {
        return usualCityDatas;
    }

    public static void setUsualCityDatas(ArrayList<UsualCityBean> usualCityDatas) {
        CMemoryData.usualCityDatas = usualCityDatas;
    }

    /* ======================常跑城市列表数据======================*/
    /* ======================附近货源/车源 共享字段信息======================*/

    public static AroundMapExtra getAroundMapExtra() {
        return aroundMapExtra;
    }

    public static void setAroundMapExtra(AroundMapExtra aroundMapExtra) {
        CMemoryData.aroundMapExtra = aroundMapExtra;
    }

    /* ======================附近货源/车源 共享字段信息======================*/
    /* ======================附近货源 目的地弹窗显示数据======================*/

    public static ArrayList<DestinationListItem> getDestinationCityDatas() {
        return destinationCityDatas;
    }

    public static void setDestinationCityDatas(ArrayList<DestinationListItem> destinationCityDatas) {
        CMemoryData.destinationCityDatas = destinationCityDatas;
    }

    /* ======================附近货源 目的地弹窗显示数据======================*/
    /* ======================用户参数======================*/

    public static GetUserInfoAtHomeResponse getUserBaseInfo() {
        if (userBaseInfo == null) {
            if (StringUtils.isNotBlank(CPersisData.getUserBaseInfo())) {
                userBaseInfo = new Gson().fromJson(CPersisData.getUserBaseInfo(), GetUserInfoAtHomeResponse.class);
            }
        }
        return userBaseInfo;
    }

    public static void setUserBaseInfo(GetUserInfoAtHomeResponse userBaseInfo) {
        CPersisData.setUserBaseInfo(new Gson().toJson(userBaseInfo));
        CMemoryData.userBaseInfo = userBaseInfo;
    }

    /**
     * 设置实名认证状态
     */
    public static void setUserState(String userState) {
        if (userBaseInfo == null) {
            if (StringUtils.isNotBlank(CPersisData.getUserBaseInfo())) {
                userBaseInfo = new Gson().fromJson(CPersisData.getUserBaseInfo(), GetUserInfoAtHomeResponse.class);
            }
        }
        userBaseInfo.setUser_status(userState);
        CPersisData.setUserBaseInfo(new Gson().toJson(userBaseInfo));
    }

    /**
     * 设置车辆认证状态
     */
    public static void setCarState(String carState) {
        if (userBaseInfo == null) {
            if (StringUtils.isNotBlank(CPersisData.getUserBaseInfo())) {
                userBaseInfo = new Gson().fromJson(CPersisData.getUserBaseInfo(), GetUserInfoAtHomeResponse.class);
            }
        }
        userBaseInfo.setTruck_status(carState);
        CPersisData.setUserBaseInfo(new Gson().toJson(userBaseInfo));
    }

    /* ======================用户参数======================*/
    /* ======================开启登录页面时间======================*/

    public static long getStartLoginPageTime() {
        return startLoginPageTime;
    }

    public static void setStartLoginPageTime(long startLoginPageTime) {
        CMemoryData.startLoginPageTime = startLoginPageTime;
    }
    /* ======================开启登录页面时间======================*/
    /* ======================重新找车货源id======================*/

    public static String getTempGoodsId() {
        return tempGoodsId;
    }

    public static void setTempGoodsId(String tempGoodsId) {
        CMemoryData.tempGoodsId = tempGoodsId;
    }

    /* ======================重新找车货源id======================*/
    /* ======================定位信息======================*/

    public static LocationInfo getLocationInfo() {
        return mLocationInfo;
    }

    public static void setLocationInfo(LocationInfo mLocationInfo) {
        CMemoryData.mLocationInfo = mLocationInfo;
    }

    /* ======================定位信息======================*/

    public static void clear() {
        sessionId = null;
        apkDownloadId = 0L;
        userMobile = null;
        userId = null;
        usualCityDatas = null;
        destinationCityDatas = null;
        aroundMapExtra = null;
        isOrderOpen = false;
        userBaseInfo = null;
        startLoginPageTime = 0;
        mLocationInfo = null;
    }
}
