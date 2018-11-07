package com.topjet.crediblenumber.order.view.map;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.view.dialog.ChooseMapDialog;
import com.topjet.common.utils.AMapUtil;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.RxHelper;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.crediblenumber.R;
import com.topjet.common.common.modle.extra.RouteExtra;
import com.topjet.crediblenumber.order.view.adapter.SearchAddressAdapter;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * creator: zhulunjun
 * time:    2017/11/6
 * describe: 线路规划
 * 地图导航页面
 * 1. 去提货
 * 2. 去送货
 * 3. 查看线路， 无导航按钮
 * <p>
 * 这里 逻辑
 * 1. 首先 定位
 * 2. 画线路
 * 3. 能搜索位置
 */

public class RouteActivity extends MvpActivity<BasePresenter> implements IView,
        RouteSearch.OnRouteSearchListener,
        AMapLocationListener,
        LocationUtils.onGetInputTipsListener {
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.tv_map_info)
    TextView tvMapInfo;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.ll_input)
    LinearLayout llInput;
    @BindView(R.id.rv_poi_address)
    RecyclerView rvPoiAddress;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_navigation)
    TextView tvNavigation;
    private AMap aMap;

    private SearchAddressAdapter searchAddressAdapter;
    private String cityCode; // 指定收货城市
    private String address; // 详细地址

    private RouteSearch mRouteSearch;
    // 这里是默认地址，如果没有传入终点地址，则是默认北京地址
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576, 116.481288);//终点，116.481288,39.995576
    // 第二个终点, 查看线路时需要传入两个终点
    private LatLonPoint mEndPointTwo = new LatLonPoint(39.995576, 116.481288);//终点，116.481288,39.995576

    private LatLonPoint myLatLon; // 自己的定位点
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private RouteExtra mRouteExtra;

    /**
     * 跳转地图导航页，需要以下参数：
     * 提货城市
     * 详细地址
     * 终点经度
     * 终点维度
     * 收货人或者发货人 姓名，电话
     */
    public static void toRoute(MvpActivity activity, RouteExtra routeExtra) {
        activity.turnToActivity(RouteActivity.class, routeExtra);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_route;
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT);

    }

    @Override
    protected void onClickRightText(View view) {
        super.onClickRightText();
        new CallPhoneUtils().showCallDialogWithAdvNotUpload(this,
                view,
                mRouteExtra.getName(),
                mRouteExtra.getMobile(),
                mRouteExtra.getCallText(),
                1);

    }

    @Override
    protected void initData() {
        super.initData();
        mRouteExtra = getIntentExtra(RouteExtra.getExtraName());
        if (mRouteExtra == null) {
            finishPage();
            return;
        }
        mEndPoint.setLatitude(mRouteExtra.getLa());
        mEndPoint.setLongitude(mRouteExtra.getLon());
        // 查看线路
        if (mRouteExtra.getType() == RouteExtra.SHOW_ROUT) {
            getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText("查看路线");
            tvNavigation.setVisibility(View.GONE);
            llInput.setVisibility(View.GONE);
            mEndPointTwo.setLatitude(mRouteExtra.getLaTwo());
            mEndPointTwo.setLongitude(mRouteExtra.getLonTow());
        } else {
            // 送货
            if (mRouteExtra.getType() == RouteExtra.DELIVERY) {
                getMyTitleBar().setTitleText("去送货").setRightText(mRouteExtra.getCallText());
                etAddress.setHint("请输入详细送货地址");
            } else { // 提货
                getMyTitleBar().setTitleText("去提货").setRightText(mRouteExtra.getCallText());
                etAddress.setHint("请输入详细提货地址");
            }
            cityCode = mRouteExtra.getCityCode();
            tvCity.setText(mRouteExtra.getCity());
            address = mRouteExtra.getAddress();
            Logger.d("线路规划 "+mRouteExtra.toString());
        }


        // 请求定位权限
        requestPermissions();
    }

    @Override
    protected void initView() {
        RxTextView.textChanges(etAddress)
                .debounce(200, TimeUnit.MILLISECONDS)
                .compose(this.<CharSequence>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        String queryKey = charSequence.toString().trim();
                        if (!TextUtils.isEmpty(queryKey)) {
                            doSearchQuery(queryKey);
                        }

                    }
                });
        searchAddressAdapter = new SearchAddressAdapter();
        rvPoiAddress.setLayoutManager(new LinearLayoutManager(this));
        rvPoiAddress.setAdapter(searchAddressAdapter);

        searchAddressAdapter.setAddressListener(new SearchAddressAdapter.AddressListener() {
            @Override
            public void addressClick(Tip item) {
                etAddress.setText("");
                mEndPoint = item.getPoint();
                address = item.getName();
                getRoute(myLatLon, mEndPoint);
                rvPoiAddress.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 初始化地图
     */
    private void initMap() {
        aMap = mapView.getMap();
        UiSettings settings = aMap.getUiSettings();
        // 隐藏缩放控件
        settings.setZoomControlsEnabled(false);
        initLocation();
        initRoute();
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        // 初始化定位
        locationClient = new AMapLocationClient(this.getApplicationContext());
        aMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        aMap.setMyLocationStyle(LocationUtils.getMapStyle(R.drawable.abcdefghijklmnopqrstuvwxyz));//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        locationOption = LocationUtils.getDefaultOption();
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
        locationOption.setOnceLocationLatest(true);
        locationClient.setLocationOption(locationOption);

        // 设置定位监听
        locationClient.setLocationListener(this);
        locationClient.startLocation();

        showLoadingDialog();
    }


    /**
     * 初始化规划线路
     */
    private void initRoute() {
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
    }

    /**
     * 在地图上设置点
     * 设置起点，终点
     */
    private void addMarker(LatLng latLng, int res, float rotate) {
        aMap.addMarker(new MarkerOptions()
                .position(latLng)
//                .setInfoWindowOffset(-42, -42) // 偏移
                .rotateAngle(rotate)
                .icon(BitmapDescriptorFactory.fromResource(res)));

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        initMap();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    private boolean drawOneLine = true; // 是否在画第一条线

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        // 汽车线路规划

        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    tvMapInfo.setVisibility(View.VISIBLE);
                    if(drawOneLine && mRouteExtra.getType() == RouteExtra.SHOW_ROUT){
                        // 画第二条线
                        getRouteTwo(mEndPoint, mEndPointTwo);
                        drawOneLine = false;
                    } else {
                        cancelShowLoadingDialog();
                    }
                    routeDate(result);

                } else if (result.getPaths() == null) {

                    Toaster.showShortToast("没有搜索到相关数据");
                }

            } else {
                Toaster.showShortToast("没有搜索到相关数据");
            }
        } else {
            Toaster.showShortToast(errorCode + " 搜索错误");
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    /**
     * 画线
     */
    private void routeDate(DriveRouteResult result) {
        final DrivePath drivePath = result.getPaths()
                .get(0);
        DrivingRouteOverLay drivingRouteOverlay = new DrivingRouteOverLay(
                mContext, aMap, drivePath,
                result.getStartPos(),
                result.getTargetPos(), null);
        drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
        drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
        drivingRouteOverlay.removeFromMap();
        drivingRouteOverlay.addToMap();
        drivingRouteOverlay.zoomToSpan();
        int dis = (int) drivePath.getDistance();
        int dur = (int) drivePath.getDuration();
        String des;
        if (mRouteExtra.getType() == RouteExtra.SHOW_ROUT) {
            des = "总里程约为：<font color='#4d94f1'>" + mRouteExtra.getDistance() + "</font>公里。成交后，可使用导航提货和配送。";
        } else {
            des = "全程<font color='#4d94f1'>" + AMapUtil.getFriendlyLength(dis) + "</font>，预计<font " +
                    "color='#00C649'>" + AMapUtil.getFriendlyTime(dur) + "</font>到达";
        }
        tvMapInfo.setText(Html.fromHtml(des));
    }

    private boolean first = false;

    /**
     * 定位回调
     *
     * @param aMapLocation 位置信息
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation && !first) {
            first = true;
            Double latitude = aMapLocation.getLatitude();
            Double longitude = aMapLocation.getLongitude();
            myLatLon = new LatLonPoint(latitude, longitude);

            getRoute(myLatLon, mEndPoint);

            /**
             * 	getBearing()
             获取方向角(单位：度） 默认值：0.0
             取值范围：【0，360】，其中0度表示正北方向，90度表示正东，180度表示正南，270度表示正西
             3.1.0之前的版本只有定位类型为 AMapLocation.LOCATION_TYPE_GPS时才有值
             自3.1.0版本开始，不限定定位类型，当定位类型不是AMapLocation.LOCATION_TYPE_GPS时，可以通过 AMapLocationClientOption.setSensorEnable
             (boolean) 控制是否返回方向角，当设置为true时会通过手机传感器获取方向角,如果手机没有对应的传感器会返回0.0 注意：
             定位类型为AMapLocation.LOCATION_TYPE_GPS时，方向角指的是运动方向
             定位类型不是AMapLocation.LOCATION_TYPE_GPS时，方向角指的是手机朝向
             */
            mRotate = aMapLocation.getBearing();
        }
    }

    private float mRotate = 0; // 旋转角度

    /**
     * 画线
     *
     * @param starLat 开始点
     * @param endLat  结束点
     */
    private void getRoute(LatLonPoint starLat, LatLonPoint endLat) {
        if (starLat == null) {
            Toaster.showShortToast("出发地为空");
            return;
        } else if (endLat == null) {
            Toaster.showShortToast("目的地为空");
            return;
        }
        showLoadingDialog();
        aMap.clear();
        addMarker(AMapUtil.convertToLatLng(starLat), R.drawable.icon_my_location, mRotate); // 起点 自己的点
        if(mRouteExtra.getType() == RouteExtra.SHOW_ROUT){
            addMarker(AMapUtil.convertToLatLng(endLat), R.drawable.icon_point_start, 0); // 终点1
            addMarker(AMapUtil.convertToLatLng(mEndPointTwo), R.drawable.icon_point_end, 0); // 终点2
        } else {
            addMarker(AMapUtil.convertToLatLng(endLat), R.drawable.icon_green_point, mRotate); // 终点
        }
        searchRout(starLat, endLat);
    }

    /**
     * 画线 第二条
     *
     * @param starLat 开始点
     * @param endLat  结束点
     */
    private void getRouteTwo(LatLonPoint starLat, LatLonPoint endLat) {
        searchRout(starLat, endLat);
    }

    /**
     * 查询线路
     *
     * @param starLat 开始点
     * @param endLat  结束点
     */
    private void searchRout(LatLonPoint starLat, LatLonPoint endLat) {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                starLat, endLat);
        // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null,
                null, "");
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }

    /**
     * 定位到指定点 中心点
     */
    private void locationCenter() {
        if (myLatLon != null) {
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(myLatLon), 15));

        }
    }


    /**
     * 定位到自己的位置
     */
    @OnClick(R.id.iv_location)
    public void locationClick() {
        locationCenter();
    }

    /**
     * 弹窗去导航 高德或者百度
     */
    @OnClick(R.id.tv_navigation)
    public void navigationClick() {
        new ChooseMapDialog(this,
                address,
                "" + mEndPoint.getLatitude(),
                "" + mEndPoint.getLongitude())
                .show();
    }

    private List<Tip> tipItems;

    /**
     * 开始进行搜索
     */
    protected void doSearchQuery(String keyWord) {
        Logger.d("搜索 " + keyWord);
        LocationUtils.getInputTipsList(this, keyWord, cityCode, this);
    }

    @Override
    public void onGetInputTips(List<Tip> result) {
        tipItems = result;
        if (tipItems != null && tipItems.size() > 0) {
            rvPoiAddress.setVisibility(View.VISIBLE);
            searchAddressAdapter.setNewData(tipItems);

        }
        Logger.d("搜索 " + result.size());
    }

    @Override
    public void onGetInputTipsFail() {
        Toaster.showShortToast("查询失败");
    }

    @OnClick(R.id.iv_clear)
    public void clear() {
        etAddress.setText("");
        rvPoiAddress.setVisibility(View.GONE);
    }

    /**
     * 请求定位权限
     */
    public void requestPermissions() {
        new RxPermissions(this).request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .compose(RxHelper.<Boolean>rxSchedulerHelper()).subscribe
                (new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            AutoDialogHelper.showSettingLocationPermission(RouteActivity.this).show();
                        }
                    }
                });
    }
}
