package com.topjet.common.common.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.common.modle.bean.LocationInfo;
import com.topjet.common.common.modle.params.FixedCycleLocationParams;
import com.topjet.common.common.modle.serverAPI.SystemCommand;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;

/**
 * Created by tsj-004 on 2017/11/10.
 * <p>
 * 定时上传定位信息服务
 * fixed 固定
 * cycle 周期
 */

public class FixedCycleLocationService extends Service implements AMapLocationListener {
    private String TAG = "LocationService";
    AMapLocationClient mLocationClient = null;
    private final static int INTERVAL_TIME_DRIVER = 3 * 1000 * 60; // 间隔时间 3min
    private final static int INTERVAL_TIME_SHIPPER = 10 * 1000 * 60; // 间隔时间 10min

    @Override
    public void onCreate() {
        super.onCreate();
        startDoLocation();  // 开始定位
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 开始定位
     */
    private void startDoLocation() {
        Logger.d(TAG, "开始定位  FixedCycleLocationService");
        mLocationClient = new AMapLocationClient(this);
        AMapLocationClientOption option = LocationUtils.getDefaultOption();
        if (CMemoryData.isDriver()) {
            option.setInterval(INTERVAL_TIME_DRIVER);
        } else {
            option.setInterval(INTERVAL_TIME_SHIPPER);
        }
        option.setOnceLocation(false);
        option.setOnceLocationLatest(false);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(this);
        mLocationClient.startLocation();
    }

    /**
     * 定位回调
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Logger.d(TAG, "定位回调  FixedCycleLocationService  " + (aMapLocation != null));
        if (aMapLocation != null && !StringUtils.isEmpty(aMapLocation.getCity())) {
            Logger.d(TAG, "定位成功  FixedCycleLocationService");
            LocationInfo locationInfo = new LocationInfo();
            // 在内存中存储定位信息
            String address1 = aMapLocation.getProvince();//定位一级地址
            String address2 = aMapLocation.getCity();//定位二级地址
            String address3 = aMapLocation.getDistrict();//定位三级地址
            double longitude = aMapLocation.getLongitude();//经度
            double latitude = aMapLocation.getLatitude();//纬度
            String detail = aMapLocation.getAddress();//定为详细地址
            String city_id = aMapLocation.getAdCode();//城市ID

            locationInfo.setAddress1(address1);
            locationInfo.setAddress2(address2);
            locationInfo.setAddress3(address3);
            locationInfo.setLatitude(latitude);
            locationInfo.setLongitude(longitude);
            locationInfo.setDetail(detail);
            locationInfo.setCity_id(city_id);

            CMemoryData.setLocationInfo(locationInfo);

            // 是司机版，并且能获取的用户的账号，此时才进行上传
            if (CMemoryData.isDriver() && StringUtils.isNotBlank(CMemoryData.getUserMobile())) {
                FixedCycleLocationParams params = new FixedCycleLocationParams();
                params.setAddress1(address1);
                params.setAddress2(address2);
                params.setAddress3(address3);
                params.setLongitude(longitude + "");
                params.setLatitude(latitude + "");
                params.setDetail(detail);
                params.setCity_id(city_id);
                requestServer(params);
            } else {
                Logger.e(TAG, "用户未登录缺少请求头字段mobile");
            }
        }
    }

    /**
     * 司机上传定位信息
     */
    private void requestServer(final FixedCycleLocationParams params) {
        new SystemCommand().saveUserGpsInfo(params, new ObserverOnNextListener<Object>() {
            @Override
            public void onNext(Object object) {
                Logger.d(TAG, "上传定位信息至服务器成功  FixedCycleLocationService");
            }

            @Override
            public void onError(String errorCode, String msg) {
                Logger.e(TAG, "上传定位信息至服务器失败  FixedCycleLocationService");
            }
        });
    }
}