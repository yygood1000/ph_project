package com.topjet.common.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 定位、地址操作 相关Utils
 */
public class LocationUtils {
    /**
     * 定位成功码
     */
    public static final int LOCATE_SUCCEED = 0;

    /**
     * 聚合范围的大小（指xx像素单位距离内的点会聚合到一个点显示）
     */
    public static final int CLUSTER_RADIUS = 100;

    /**
     * 自定义的定位回调接口
     */
    public interface OnLocationListener {
        void onLocated(AMapLocation aMapLocation);

        void onLocationPermissionfaild();
    }

    /**
     * 自定义的获取路线距离回调接口
     */
    public interface OnGetDistanceListener {
        void onGetDistance(int distance);
    }

    /**
     * 自定义的地理编码回调接口
     */
    public interface onGeocodeSearchedListener {
        void onGeocodeSearched(GeocodeAddress result);

        void onGeocodeSearchedFail();
    }

    /**
     * 自定义的反地理编码会叫接口
     */
    public interface onReGeocodeSearchedListener {
        void onReGeocodeSearched(RegeocodeAddress result);
    }

    /**
     * 自定义的输入框自动提示集合数据回调接口
     */
    public interface onGetInputTipsListener {
        void onGetInputTips(List<Tip> result);

        void onGetInputTipsFail();
    }

    /**
     * 直接单次定位
     *
     * @param context  使用定位页面的上下文
     * @param listener 定位回调
     */
    public static void location(final Context context, final OnLocationListener listener) {
        RxPermissions rxPermissions = new RxPermissions((Activity) context);

        rxPermissions.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .compose(RxHelper.<Boolean>rxSchedulerHelper()).subscribe
                (new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            final AMapLocationClient mLocationClient = new AMapLocationClient(context);
                            //设置定位参数
                            mLocationClient.setLocationOption(getDefaultOption());
                            // 设置定位监听
                            mLocationClient.setLocationListener(new AMapLocationListener() {
                                @Override
                                public void onLocationChanged(AMapLocation aMapLocation) {
                                    listener.onLocated(aMapLocation);
                                    mLocationClient.stopLocation();
                                    mLocationClient.onDestroy();
                                }
                            });
                            mLocationClient.startLocation();
                        } else {
                            Logger.i("oye", "定位权限没开");
                            listener.onLocationPermissionfaild();
                        }
                    }
                });
    }

    /**
     * 单次定位的参数
     */
    public static AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        //设置定位模式为AMapLocationMode.Hight_Accuracy，精准定位。耗时好久
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。不会使用GPS和其他传感器,只会使用网络定位（Wi-Fi和基站定位）
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setGpsFirst(true);

        //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setHttpTimeOut(15000);

        //可选，设置定位间隔。默认为2秒
//        mOption.setInterval(2000);

        //可选，设置是否单次定位。默认是false
        mOption.setOnceLocation(true);

        //可选，设置是否返回逆地理地址信息。默认是true
        mOption.setNeedAddress(true);

        //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        mOption.setOnceLocationLatest(true);

        // 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);

        //可选，设置是否使用传感器。默认是false
        mOption.setSensorEnable(false);

        //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setWifiScan(true);

        //可选，设置是否使用缓存定位，默认为true
        mOption.setLocationCacheEnable(true);

        //设置是否强制刷新WIFI，默认为true，强制刷新。
//        mOption.setWifiActiveScan(false);
        return mOption;
    }

    /**
     * 计算两点间距离
     * Latlng(double latitude,double longitude)//latitude 维度 longitude 经度
     */
    public static float getDistance(LatLng latLng1, LatLng latLng2) {
        return AMapUtils.calculateLineDistance(latLng1, latLng2);
    }


    /**
     * 计算两点驾车距离
     *
     * @param mStartPoint 起点 new LatLonPoint(39.942295,116.335891)
     * @param mEndPoint   重点
     */
    public static void getLineDistance(Context context, LatLonPoint mStartPoint, LatLonPoint mEndPoint,
                                       final OnGetDistanceListener mListener) {
        final RouteSearch mRouteSearch = new RouteSearch(context);
        mRouteSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int rCode) {
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (driveRouteResult != null && !ListUtils.isEmpty(driveRouteResult.getPaths())) {
                        final DrivePath drivePath = driveRouteResult.getPaths().get(0);
                        mListener.onGetDistance((int) drivePath.getDistance());
                    } else {
                        mListener.onGetDistance(0);
                    }
                }
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
            }
        });
        // 构造起点终点参数
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);

        // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        // 目前选的模式是距离最短，其他模式查看高德API RouteSearch类的 字段概要
        RouteSearch.DriveRouteQuery query =
                new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");

        // 异步路径规划驾车模式查询
        mRouteSearch.calculateDriveRouteAsyn(query);
    }

    /**
     * 地理编码，指的是从已知的地址描述到对应的经纬度坐标的转换（地址 -> 经纬度）
     *
     * @param locationName 地理编码的名称；例：北京市朝阳区方恒国际中心A座
     * @param city         编码地址所在的城市；例：中文或者中文全拼，citycode、adcode
     */
    public static void geocodeSearch(Context context, String locationName, String city, final onGeocodeSearchedListener
            mListener) {
        // 构造查询对象
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        // 设置查询回调监听
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            }

            /**
             * 地理编码查询回调
             */
            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (geocodeResult != null && !ListUtils.isEmpty(geocodeResult.getGeocodeAddressList())) {
                        GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
                        mListener.onGeocodeSearched(address);
                    } else {
                        mListener.onGeocodeSearchedFail();
                    }
                }
            }
        });

        // 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        GeocodeQuery query = new GeocodeQuery(locationName, city);
        // 设置同步地理编码请求
        geocoderSearch.getFromLocationNameAsyn(query);
    }

    /**
     * 逆地理编码，从已知的经纬度坐标到对 应的地址描述（如省市、街区、楼层、房间等）的转换（经纬度 -> 地址信息）
     */
    public static void reGeocodeSearch(Context context, LatLonPoint latLonPoint, final onReGeocodeSearchedListener
            mListener) {
        // 构造查询对象
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        // 设置查询回调监听
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            /**
             * 逆地理编码回调
             */
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null
                            && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                        mListener.onReGeocodeSearched(regeocodeResult.getRegeocodeAddress());
                    } else {
                        mListener.onReGeocodeSearched(null);
                    }
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            }
        });

        // 第一个参数表示一个Latlng，第二参数表示范围多少米(默认1000m)，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        // 设置异步逆地理编码请求
        geocoderSearch.getFromLocationAsyn(query);
    }

    /**
     * 输入框  输入提示列表数据获取
     *
     * @param newText 输入框的内容
     * @param city    查询的城市编码 citycode、城市名称（中文或中文全拼）、行政区划代码adcode。设置null或“”为全国
     */
    public static void getInputTipsList(Context context, String newText, String city, final onGetInputTipsListener
            mListener) {
        InputtipsQuery inputquery = new InputtipsQuery(newText, city);
        inputquery.setCityLimit(true);//对获取结果进行严格城市限制。
        Inputtips inputTips = new Inputtips(context, inputquery);
        inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> list, int rCode) {
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    mListener.onGetInputTips(list);
                } else {
                    mListener.onGetInputTipsFail();
                }
            }
        });
        inputTips.requestInputtipsAsyn();
    }

    /**
     * 输入框  输入提示列表数据获取
     *
     * @param newText 输入框的内容
     * @param city    查询的城市编码 citycode、城市名称（中文或中文全拼）、行政区划代码adcode。设置null或“”为全国
     */
    public static void getInputTipsList(Context context, String newText, String city, final onGetInputTipsListener
            mListener, LatLonPoint llp) {
        InputtipsQuery inputquery = new InputtipsQuery(newText, city);
        inputquery.setCityLimit(true);//对获取结果进行严格城市限制。
        inputquery.setLocation(llp);
        Inputtips inputTips = new Inputtips(context, inputquery);
        inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> list, int rCode) {
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    mListener.onGetInputTips(list);
                } else {
                    mListener.onGetInputTipsFail();
                }
            }
        });
        inputTips.requestInputtipsAsyn();
    }

    /**
     * 缩放移动地图，已某一个点为中心，显示说有的点。
     */
    public static void zoomToSpanWithCenter(AMap aMap, LatLng centerPoint, List<LatLng> pointList) {
        if (pointList != null && pointList.size() > 0) {
            if (aMap == null)
                return;
            LatLngBounds bounds = getLatLngBounds(centerPoint, pointList);
            // 第二个参数，经纬度范围与限制区域的边缘间隙，单位像素
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }

    /**
     * 根据中心点和自定义内容获取缩放bounds
     */

    private static LatLngBounds getLatLngBounds(LatLng centerpoint, List<LatLng> pointList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        if (centerpoint != null) {
            for (int i = 0; i < pointList.size(); i++) {
                LatLng p = pointList.get(i);
                LatLng p1 = new LatLng((centerpoint.latitude * 2) - p.latitude, (centerpoint.longitude * 2) - p
                        .longitude);
                b.include(p);
                b.include(p1);
            }
        }
        return b.build();
    }

    /**
     * 缩放移动地图，保证所有自定义点在可视范围中。
     */
    public static void zoomToSpan(AMap aMap, List<LatLng> pointList) {
        if (pointList != null && pointList.size() > 0) {
            if (aMap == null)
                return;
            LatLngBounds bounds = getLatLngBounds(pointList);
            // 第二个参数，经纬度范围与限制区域的边缘间隙，单位像素
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }

    /**
     * 根据自定义内容获取缩放bounds
     */
    private static LatLngBounds getLatLngBounds(List<LatLng> pointList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < pointList.size(); i++) {
            LatLng p = pointList.get(i);
            b.include(p);
        }
        return b.build();
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    /**
     * 初始化蓝点显示
     * 隐藏精确圈
     *
     * @return style对象
     */
    public static MyLocationStyle getMapStyle(int res) {
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(res));
        // 自定义精度范围的圆形边框颜色   隐藏精确圈
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
        return myLocationStyle;
    }
}
