package com.topjet.shipper.familiar_car.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.AroundMapExtra;
import com.topjet.common.common.modle.bean.StatisticalData;
import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.common.view.activity.SearchAddressActivity;
import com.topjet.common.common.view.dialog.CitySelectPopupWindow;
import com.topjet.common.common.view.dialog.TruckLengthAndTypePopupWindow;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.resource.bean.AreaInfo;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.cluster.ClusterClickListener;
import com.topjet.common.widget.cluster.ClusterItem;
import com.topjet.common.widget.cluster.ClusterOverlay;
import com.topjet.common.widget.cluster.ClusterRender;
import com.topjet.shipper.R;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommand;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommandAPI;
import com.topjet.shipper.familiar_car.presenter.AroundTruckMapPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 附近车源 地图页面
 */
public class AroundTruckMapActivity extends MvpActivity<AroundTruckMapPresenter> implements
        AroundTruckMapView, ClusterRender {
    @Nullable
    @BindView(R.id.rl_options)
    RelativeLayout rlOptions;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    @BindView(R.id.tv_depart)
    TextView tvDepart;
    @BindView(R.id.iv_local_point)
    ImageView ivLocalPoint;
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.iv_destination_arrows_down)
    ImageView ivDestinationArrowsDown;
    @BindView(R.id.iv_truck_type_arrows_down)
    ImageView ivTruckTypeArrowsDown;
    @BindView(R.id.tv_destination)
    TextView tvDestination;
    @BindView(R.id.tv_truck_typelength)
    TextView tvTruckTypelength;


    /**
     * 地图先关变量/常量
     */
    private AMap aMap = null; // 地图对象
    private Map<Integer, Drawable> mBackDrawAbles = new HashMap<Integer, Drawable>();
    private ClusterOverlay mClusterOverlay;// 点聚合操作类
    private CitySelectPopupWindow citySelectPopupWindow;
    private TruckLengthAndTypePopupWindow truckLengthAndTypePopupWindow;
    // 地图信息窗是否正在显示
    private boolean isShowingInfoWindow;
    // 正在显示信息窗的Marker
    private Marker showInfoWindowMarker;

    @Override
    protected void initPresenter() {
        mPresenter = new AroundTruckMapPresenter(this, mContext, new TruckCommand(TruckCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_around_truck_map;
    }

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(getString(R.string.around_truck));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        // 初始化地图
        initMap();
    }

    @Override
    protected void initView() {
        initPopWindow();
    }

    @Override
    protected void initData() {
        Logger.i("oye", "跳转地图页面收到的参数设置 == " + CMemoryData.getAroundMapExtra().toString());
        // 从内存中获取请求参数，对请求参数进行赋值
        mPresenter.lat = CMemoryData.getAroundMapExtra().lat;// 设置纬度
        mPresenter.lng = CMemoryData.getAroundMapExtra().lng;// 设置经度
        mPresenter.departCityId = CMemoryData.getAroundMapExtra().departCityId;// 设置出发地城市Id
        mPresenter.destinationCityId = CMemoryData.getAroundMapExtra().destinationCityId;// 设置目的地城市Id
        mPresenter.truckSelectedData = CMemoryData.getAroundMapExtra().truckSelectedData;// 设置车型车长选择

        if (mPresenter.truckSelectedData != null) {
            setTruckInfo(mPresenter.truckSelectedData.getLengthList().get(0).getDisplayName(),
                    mPresenter.truckSelectedData.getTypeList().get(0).getDisplayName());
        }
        setEditTextContent(CMemoryData.getAroundMapExtra().departCityName);
        tvDestination.setText(StringUtils.isNotBlank(CMemoryData.getAroundMapExtra().destinationCityName) ?
                CMemoryData.getAroundMapExtra().destinationCityName : "目的地");
    }


    /**
     * 初始化地图
     */
    public void initMap() {
        aMap = mapView.getMap();
        UiSettings mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        // 不显示缩放按钮
        mUiSettings.setZoomControlsEnabled(false);
        // 禁止倾斜手势
        mUiSettings.setTiltGesturesEnabled(true);
        // 添加移动地图事件监听器
        aMap.setOnCameraChangeListener(onCameraChangeListener);
        // 设置泡泡窗点击事件
        aMap.setOnInfoWindowClickListener(onInfoWindowClickListener);
        // 设置自定义泡泡窗适配器
        aMap.setInfoWindowAdapter(mPresenter.getInfoWindowAdapter());
        // 地图点击事件，清空信息窗
        aMap.setOnMapClickListener(onMapClickListener);
        // 判断是否我要用车页面传入了 出发地城市ID，如果有则不定位了
        if (StringUtils.isEmpty(mPresenter.departCityId)) {
            // 开始定位，定位后获取地图数据
            mPresenter.getLocate();
        } else {
            mPresenter.isLocating = false;
            moveAndZoomMap();
        }
    }


    /**
     * 初始化PopWindow
     */
    private void initPopWindow() {
        citySelectPopupWindow = new CitySelectPopupWindow(TAG)
                .setOnDismissListener(new CitySelectPopupWindow.OnCustomDismissListener() {
                    @Override
                    public void onDismiss() {
                        ivDestinationArrowsDown.setBackgroundResource(R.drawable.arrows_down_gray);
                    }
                });

        truckLengthAndTypePopupWindow = new TruckLengthAndTypePopupWindow(TAG, true, true)
                .setOnDismissListener(new TruckLengthAndTypePopupWindow.OnCustomDismissListener() {
                    @Override
                    public void onDismiss() {
                        ivTruckTypeArrowsDown.setBackgroundResource(R.drawable.arrows_down_gray);
                    }
                });
    }

    /**
     * 设置出发地文本框详细地址内容
     */
    @Override
    public void setEditTextContent(String str) {
        llEdit.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        tvDepart.setText(str);
    }

    /**
     * 根据坐标信息移动地图
     * <p>
     * 该方法在调用的地方
     * 1.首次进入页面定位后调用
     * 2.手动点击定位后会调用
     * 3.从搜索地址页面跳转回来时会调用
     */
    @Override
    public void moveAndZoomMap() {
        ivLocalPoint.setVisibility(View.VISIBLE);
        aMap.moveCamera(mPresenter.creatCurrentCameraUpdata());
    }

    /**
     * 向地图上添加省市级别 集合点
     */
    @Override
    public void addMarkers(ArrayList<MarkerOptions> markerOptionList) {
        // 设置省市级别Marker点击事件
        aMap.addMarkers(markerOptionList, false);
        // 设置Marker点击事件
        aMap.setOnMarkerClickListener(onMarkerClickListener);
    }

    /**
     * 进行高德地图点聚合
     *
     * @param clusterItems 聚合的数据
     */
    @Override
    public void assignClusters(List<ClusterItem> clusterItems) {
        if (mClusterOverlay != null) {
            mClusterOverlay.onDestroy();
            mClusterOverlay = null;
        }
        mClusterOverlay = new ClusterOverlay(aMap, clusterItems, ScreenUtils.dp2px(mContext, LocationUtils
                .CLUSTER_RADIUS), getApplicationContext());
        // 设置聚合渲染器接口（getDrawAble()）
        mClusterOverlay.setClusterRenderer(this);
        // 设置点聚合点击事件
        mClusterOverlay.setOnClusterClickListener(clusterClickListener);
    }

    /**
     * 根据聚合点的元素返回渲染背景样式
     */
    @Override
    public Drawable getDrawAble(List<ClusterItem> mClusterItems, int clusterNum) {
        Drawable bitmapDrawable;
        if (clusterNum == 1) {
            if (((TruckInfo) mClusterItems.get(0)).getIsFamiliarTruck()) {
                bitmapDrawable = mBackDrawAbles.get(1);
                if (bitmapDrawable == null) {
                    bitmapDrawable = ResourceUtils.getDrawable(R.drawable.iv_icon_familiar_truck);
                    mBackDrawAbles.put(1, bitmapDrawable);
                }
            } else {
                bitmapDrawable = mBackDrawAbles.get(2);
                if (bitmapDrawable == null) {
                    bitmapDrawable = ResourceUtils.getDrawable(R.drawable.iv_icon_not_familiar_truck);
                    mBackDrawAbles.put(2, bitmapDrawable);
                }
            }
        } else {
            bitmapDrawable = mBackDrawAbles.get(3);
            if (bitmapDrawable == null) {
                bitmapDrawable = ResourceUtils.getDrawable(R.drawable.iv_bg_cluter_overlay);
                mBackDrawAbles.put(3, bitmapDrawable);
            }
        }
        return bitmapDrawable;
    }

    /**
     * 清空覆盖物
     */
    @Override
    public void clearMarker() {
        aMap.clear();
    }

    /**
     * 设置车型车长文本框
     */
    @Override
    public void setTruckInfo(String truckLength, String truckType) {
        tvTruckTypelength.setText(truckLength + ("/" + truckType));
    }

    /* ================================地图相关点击事件================================*/

    /**
     * 地图点击事件
     */
    AMap.OnMapClickListener onMapClickListener = new AMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if (isShowingInfoWindow) {
                isShowingInfoWindow = false;
                showInfoWindowMarker.hideInfoWindow();
            }
        }
    };

    /**
     * 地图状态发生变化的监听接口。
     */
    AMap.OnCameraChangeListener onCameraChangeListener = new AMap.OnCameraChangeListener() {
        //对正在移动地图事件回调
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
        }

        //对移动地图结束事件回调
        @Override
        public void onCameraChangeFinish(CameraPosition cameraPosition) {
            Logger.i("oye", "地图状态改变结束 ：onCameraChangeFinish");
            mPresenter.lat = cameraPosition.target.latitude;
            mPresenter.lng = cameraPosition.target.longitude;
            mPresenter.zoom = cameraPosition.zoom;

            LocationUtils.reGeocodeSearch(mContext, LocationUtils.convertToLatLonPoint(cameraPosition.target),
                    new LocationUtils.onReGeocodeSearchedListener() {

                        @Override
                        public void onReGeocodeSearched(RegeocodeAddress result) {
                            mPresenter.departCityId = result.getAdCode();
                            // 地图状态变化结束，请求地图数据
                            mPresenter.getAroundTruckMapData();
                        }
                    });
        }
    };

    /**
     * 省市区级别Marker      点击事件
     */
    AMap.OnMarkerClickListener onMarkerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Logger.i("oye", "省市区界别 Marker点击事件");
            StatisticalData data = mPresenter.statisticalDatas.get((int) marker.getZIndex());
            if (mPresenter.zoom >= 3 && mPresenter.zoom <= 8) {//全国
                if (AreaDataDict.isSpecialFirstLevel(data.getCity_name().replace("市", ""))) {//
                    // 显示全国范围，点击则是放大到某省，显示该省全部市的信息。如果是直辖市，级别直接到区
                    mPresenter.zoom = 11f;
                } else {
                    mPresenter.zoom = 9f;
                }
                moveAndZoomMap();
            } else if (mPresenter.zoom >= 9 && mPresenter.zoom <= 10) {//省
                mPresenter.zoom = 11f;
                moveAndZoomMap();
            } else if (mPresenter.zoom >= 11 && mPresenter.zoom <= 14) {//市
                //  跳转地图列表页展示页面，这里跳转后需要请求列表接口
                AroundMapExtra extra = new AroundMapExtra(
                        marker.getPosition().latitude,
                        marker.getPosition().longitude,
                        mPresenter.zoom + "",
                        data.getCity_id(),
                        mPresenter.destinationCityId,
                        mPresenter.truckSelectedData,
                        data.getCity_name() + "(共" + data.getCountNotEnter() + "）");
                Logger.i("oye", "turnToAroundGoodsMapListActivity extra = " + extra.toString());
                turnToActivity(AroundTruckMapListActivity.class, extra);
                return true;
            }
            return true;
        }
    };

    /**
     * 高德聚合点 点击事件
     */
    ClusterClickListener clusterClickListener = new ClusterClickListener() {
        @Override
        public void onClick(Marker marker, List<ClusterItem> clusterItems) {
            Logger.i("oye", "高德聚合点 被点击了 = " + clusterItems.toString());
            if (clusterItems.size() == 1) {
                showInfoWindowMarker = marker;
                isShowingInfoWindow = true;
                // 将构建信息窗的数据实体类赋值给marker
                marker.setObject(clusterItems.get(0));
                // 弹出泡泡窗
                marker.showInfoWindow();
            } else {
                // 弹出附近货源地图列表页面，列表所需要显示的数据集合为 clusterItems 中的数据，跳转后不需要请求列表接口
                ArrayList<TruckInfo> list = new ArrayList<>();
                for (ClusterItem item : clusterItems) {
                    list.add((TruckInfo) item);
                }
                AroundMapExtra extra = new AroundMapExtra();
                extra.setTruckListData(list);
                turnToActivity(AroundTruckMapListActivity.class, extra);
            }
        }
    };

    /**
     * 设置泡泡窗点击事件
     */
    AMap.OnInfoWindowClickListener onInfoWindowClickListener = new AMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            // 跳转车辆详情页面
            TruckInfo item = (TruckInfo) marker.getObject();
            TruckInfoActivity.truckInfo((MvpActivity) mContext, item.getTruck_id());
        }
    };

    /* ================================地图相关点击事件================================*/
    /* ================================页面点击事件================================*/

    @OnClick({R.id.ll_destination, R.id.ll_truck_type, R.id.ll_input, R.id.iv_refresh, R.id.iv_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_destination:
                // 目的地选择
                ivDestinationArrowsDown.setBackgroundResource(R.drawable.arrows_down_green);
                citySelectPopupWindow.showCitySelectPopupWindow((Activity) mContext, rlOptions, false, true, true);
                break;
            case R.id.ll_truck_type:
                // 车型车长选择
                ivTruckTypeArrowsDown.setBackgroundResource(R.drawable.arrows_down_green);
                truckLengthAndTypePopupWindow.showPopupWindow((Activity) mContext, rlOptions, false,
                        TruckLengthAndTypePopupWindow.ENTIRETY_NO_SELECTED, false, mPresenter.truckSelectedData);
                break;
            case R.id.ll_input:
                // 跳转搜索录入页
                startActivityForResult(new Intent(AroundTruckMapActivity.this, SearchAddressActivity.class), 1);
                break;
            case R.id.iv_refresh:
                // 刷新
                mPresenter.getAroundTruckMapData();
                break;
            case R.id.iv_location:
                // 定位
                mPresenter.getLocate();
                break;
        }
    }

    /**
     * 目的地选择事件
     */
    @Subscribe
    public void onEvent(AreaSelectedData data) {
        if (data.getTag().equals(TAG)) {
            AreaInfo areaInfo = data.getAreaInfo();
            tvDestination.setText(areaInfo.getLastCityName());
            mPresenter.destinationCityId = areaInfo.getLastCityId();
            mPresenter.getAroundTruckMapData();
        }
    }

    /**
     * 车型车长选择
     */
    @Subscribe
    public void onEvent(TruckTypeLengthSelectedData tld) {
        // 接受车型车长ID，并在内存中存一份
        if (tld.getTag().equals(TAG)) {
            mPresenter.truckSelectedData = tld;
            setTruckInfo(tld.getLengthList().get(0).getDisplayName(), tld.getTypeList().get(0).getDisplayName());
            mPresenter.getAroundTruckMapData();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TODO 在页面失去焦点的时候向内存设置参数。不要在各个地方进行参数的设置
        CMemoryData.getAroundMapExtra().setLat(mPresenter.lat);
        CMemoryData.getAroundMapExtra().setLng(mPresenter.lng);
        CMemoryData.getAroundMapExtra().setDepartCityId(mPresenter.departCityId);
        CMemoryData.getAroundMapExtra().setDepartCityId(mPresenter.departCityId);
        CMemoryData.getAroundMapExtra().setTruckSelectedData(mPresenter.truckSelectedData);
        CMemoryData.getAroundMapExtra().setDestinationCityId(mPresenter.destinationCityId);
        CMemoryData.getAroundMapExtra().setDestinationCityName(tvDestination.getText().toString());
        CMemoryData.getAroundMapExtra().setNeedGetParams(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (mClusterOverlay != null) {
            mClusterOverlay.onDestroy();
            mClusterOverlay = null;
        }
    }
}
