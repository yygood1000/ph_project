package com.topjet.shipper.familiar_car.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.bean.SearchAddressResultExtra;
import com.topjet.common.common.modle.bean.StatisticalData;
import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.cluster.ClusterItem;
import com.topjet.shipper.R;
import com.topjet.shipper.familiar_car.model.params.AroundTruckMapParams;
import com.topjet.shipper.familiar_car.model.respons.AroundTruckMapResponse;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommand;
import com.topjet.shipper.familiar_car.view.activity.AroundTruckMapView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * 附近货源地图
 */

public class AroundTruckMapPresenter extends BaseApiPresenter<AroundTruckMapView, TruckCommand> {
    public String departCityId;// 起点城市adCode
    public String destinationCityId;// 目的地城市adCode
    public double lat;// 维度
    public double lng;// 经度
    public TruckTypeLengthSelectedData truckSelectedData;
    public float zoom = 10f;// 缩放级别

    public ArrayList<StatisticalData> statisticalDatas = new ArrayList<>();

    public boolean isLocating = true;// 是否正在定位

    public AroundTruckMapPresenter(AroundTruckMapView mView, Context mContext, TruckCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 录入 搜索地址 页面返回方法
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        SearchAddressResultExtra extra = (SearchAddressResultExtra) mActivity.getIntentExtra(data,
                SearchAddressResultExtra.getExtraName());
        if (extra != null) {
            // Adcode
            departCityId = extra.adCode;
            // 纬度
            lat = extra.lat;
            // 经度
            lng = extra.lng;
            // 设置出发地的详细地址信息
            mView.setEditTextContent(extra.getFullAddress());
            // 移动地图
            mView.moveAndZoomMap();
        }
    }

    //  请求地图数据 ==============================================================start

    /**
     * 获取附近货源 地图页 数据
     * <p>
     * 1.地图页面请求数据都是在地图状态变更后才去获取。
     * 2.方法会在选择了目的地或者车型车长后调用
     */
    public void getAroundTruckMapData() {
        if (isLocating) {
            Logger.i("oye", "定位未完成");
            return;
        }

        AroundTruckMapParams params = new AroundTruckMapParams();
        params.setCity_id(departCityId);
        params.setDestination_city_id(destinationCityId);
        params.setLatitude(lat + "");
        params.setLongitude(lng + "");
        params.setMap_level(zoom + "");
        params.setTruck_type_id(truckSelectedData != null ? truckSelectedData.getSingleTruckTypeId() : "");
        params.setTruck_length_id(truckSelectedData != null ? truckSelectedData.getSingleTruckLengthId() : "");

        mApiCommand.getAroundTruckMap(params, new ObserverOnResultListener<AroundTruckMapResponse>() {
            @Override
            public void onResult(AroundTruckMapResponse response) {
                mView.clearMarker();
                // 地图数据返回，进行判断展示
                if (StringUtils.isEmpty(response.getParameter_level())) {
                    Logger.i("oye", "聚合标记 为空");
                    return;
                }
                if (response.getParameter_level().equals("1")) {// 1：群组信息级别，进行大头针展示
                    Logger.i("oye", "省市级，循环添加覆盖物");
                    statisticalDatas = response.getTruck_statistical();
                    // 添加省市级聚合点
                    addMarkers();
                } else {
                    Logger.i("oye", "高德地图点聚合");
                    // 构造高德地图点聚合集合
                    creatClusterItemListAndAssign(response.getNear_truck_response_list());
                }
            }
        });
    }


    //  请求地图数据 ==============================================================end

    //  地图相关代码 ==============================================================start

    /**
     * 定位当前位置，这里需要添加
     */
    public void getLocate() {
        LocationUtils.location(mContext, new LocationUtils.OnLocationListener() {
            @Override
            public void onLocated(AMapLocation aMapLocation) {

                if (aMapLocation.getErrorCode() == LocationUtils.LOCATE_SUCCEED) {
                    lat = aMapLocation.getLatitude();
                    lng = aMapLocation.getLongitude();
                    departCityId = aMapLocation.getAdCode();
                    mView.setEditTextContent(aMapLocation.getAddress());
                } else {
                    lat = AreaDataDict.DEFAULT_CITY_LAT;
                    lng = AreaDataDict.DEFAULT_CITY_LAT;
                    departCityId = AreaDataDict.DEFAULT_CITY_CODE;
                    mView.setEditTextContent(AreaDataDict.DEFAULT_CITY_NAME);
                }
                isLocating = false;// 定位结束
                // 设置地图中心
                mView.moveAndZoomMap();
            }

            // 定位权限获取失败
            @Override
            public void onLocationPermissionfaild() {
                isLocating = false;
                AutoDialogHelper.showSettingLocationPermission(mActivity).show();
            }
        });
    }

    /* ========================== 构造地图覆盖物相关代码 ==========================*/

    /**
     * 添加自定义覆盖物，实现省市级 统计Marker
     */
    private void addMarkers() {
        ArrayList<MarkerOptions> markerOptionList = new ArrayList<>();

        for (int i = 0; i < statisticalDatas.size(); i++) {
            StatisticalData data = statisticalDatas.get(i);
            markerOptionList.add(creatMarkerOptions(data, i));
        }

        mView.addMarkers(markerOptionList);
    }

    /**
     * 创建 单个聚合Marker点
     */
    private MarkerOptions creatMarkerOptions(StatisticalData data, int i) {
        MarkerOptions markerOption = new MarkerOptions();
        // 设置Marker 经纬度
        markerOption
                .position(new LatLng(Double.parseDouble(data.getLatitude()),
                        Double.parseDouble(data.getLongitude())))
                .icon(getBitmapDes(data))
                .zIndex(i);

        return markerOption;
    }

    /**
     * 创建聚合点覆盖物具体显示内容
     */
    private BitmapDescriptor getBitmapDes(StatisticalData data) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_bg_around_truck_marker, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_marker);
        // 设置覆盖物文本
        textView.setText(data.getCity_name() + "\n" + data.getCount());
        return BitmapDescriptorFactory.fromView(view);
    }

    /**
     * 构造高德地图点聚合集合
     */
    private void creatClusterItemListAndAssign(ArrayList<TruckInfo> goodslist) {
        List<ClusterItem> items = new ArrayList<>();
        for (TruckInfo data : goodslist) {
            if (StringUtils.isNotBlank(data.getLatitude()) || StringUtils.isNotBlank(data.getLongitude())) {
                items.add(data);
            }
        }
        // 进行地图点聚合操作
        mView.assignClusters(items);
    }

    /* ========================== 构造地图覆盖物相关代码 ==========================*/


    /**
     * 构造地图状态变换类
     */
    public CameraUpdate creatCurrentCameraUpdata() {
        return CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom);
    }


    /* ========================== infowinfow相关代码 ==========================*/

    /**
     * 自定义infowinfow窗口 适配器
     */
    public AMap.InfoWindowAdapter getInfoWindowAdapter() {
        return new AMap.ImageInfoWindowAdapter() {
            @Override
            public long getInfoWindowUpdateTime() {
                return 0;
            }

            @Override
            public View getInfoWindow(Marker marker) {
                View infoWindow = mActivity.getLayoutInflater().inflate(R.layout.shipper_info_window, null);
                render(marker, infoWindow);
                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        };
    }

    /**
     * 自定义infowinfow窗口
     */
    private void render(Marker marker, View view) {
        TruckInfo data = (TruckInfo) marker.getObject();
        TextView tvTruckInfo = ((TextView) view.findViewById(R.id.tv_truck_info));
        TextView tvDriver = ((TextView) view.findViewById(R.id.tv_driver));
        tvTruckInfo.setText(data.getTruck_type_name() + " " + data.getTruck_length_name());
        tvDriver.setText(data.getDriver_name() + " 共成交" + data.getDriver_order_count() + "笔");
    }
    /* ========================== infowinfow相关代码 ==========================*/

    // 地图相关代码 ==============================================================end
}
