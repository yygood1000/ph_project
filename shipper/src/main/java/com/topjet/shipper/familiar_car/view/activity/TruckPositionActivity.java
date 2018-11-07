package com.topjet.shipper.familiar_car.view.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.shipper.R;
import com.topjet.shipper.familiar_car.model.extra.TruckExtra;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * creator: zhulunjun
 * time:    2017/10/23
 * describe: 司机车辆位置信息
 */

public class TruckPositionActivity extends MvpActivity<BasePresenter> implements IView
        , AMapLocationListener {
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_call)
    ImageView ivCall;

    private AMap aMap;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private LatLng myLatLon; // 自己的定位点
    private LatLng centerLatLon; // 中心点
    // 车辆 marker
    private Marker truckMarker;
    private LatLng truckLatLng;
    private MarkerOptions markerOption = null;
    private List<Marker> markerList = new ArrayList<>();
    private List<LatLng> latLngs = new ArrayList<>(); // 两个点的集合
    // 车辆图标
    private BitmapDescriptor ICON_TRUCK;

    private TruckExtra extra;

    /**
     * 订单列表调用,显示订单详情
     *
     * @param extra 需要 经纬度，车牌号，司机位置，司机电话，司机名字，位置更新时间等参数
     */
    public static void truckPosition(MvpActivity activity, TruckExtra extra, boolean isShowMenu) {
        extra.setShowMenu(isShowMenu);
        activity.turnToActivity(TruckPositionActivity.class, extra);
    }

    /**
     * 车辆详情， 个人中心，车辆列表 调用
     *
     * @param extra * 需要 经纬度，车牌号，司机位置，司机电话，司机名字，位置更新时间等参数
     */
    public static void truckPosition(MvpActivity activity, TruckExtra extra) {
        activity.turnToActivity(TruckPositionActivity.class, extra);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_truck_position;
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        extra = getIntentExtra(TruckExtra.getExtraName());
        if (extra != null) {
            getMyTitleBar().setTitleText(extra.getTruck_plate());
            if (extra.isShowMenu()) {
                getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT)
                        .setTitleText(extra.getTruck_plate())
                        .setRightText(R.string.truck_info);
            }
        } else {
            finishPage(); // 无参数，关闭页面
        }
    }

    @Override
    protected void initView() {

    }

    private void initMap() {
        aMap = mapView.getMap();
        UiSettings settings = aMap.getUiSettings();
        // 隐藏缩放控件
        settings.setZoomControlsEnabled(false);
        // 初始化定位
        locationClient = new AMapLocationClient(this.getApplicationContext());
        aMap.animateCamera(CameraUpdateFactory.zoomTo(15));


        aMap.setMyLocationStyle(LocationUtils.getMapStyle(R.drawable.icon_position_blue));//设置定位蓝点的Style
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

        // 添加司机点
        addTruckMarker();


    }

    @Override
    protected void initData() {
        super.initData();
        tvAddress.setText(extra.getAddress());
        tvTime.setText(StringUtils.format(R.string.update_time, TimeUtils.showTimeDayHours(extra.getTime())));

    }


    // 添加司机点
    private void addTruckMarker() {
        ICON_TRUCK = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.iv_icon_not_familiar_truck));
        // 车辆点设置
        markerOption = new MarkerOptions();
        markerOption.icon(ICON_TRUCK);
        truckMarker = aMap.addMarker(markerOption);
        // 虚拟点，测试效果
        // 121.404164,31.17622
        // 121.512232,31.127183
//        extra.setLongitude("121.512232");
//        extra.setLatitude("31.127183");
        truckLatLng = new LatLng(extra.getLatitude(), extra.getLongitude());
        latLngs.add(truckLatLng);
        truckMarker.setPosition(truckLatLng);
        markerList.add(truckMarker);
    }

    /**
     * 定位到指定点 中心点
     *
     * @param la
     * @param lo
     */
    private void locationCenter(double la, double lo) {
        if (la > 0 && lo > 0) {
            centerLatLon = new LatLng(la, lo);
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerLatLon, 15));

        }
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
//        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private boolean first = false;

    /**
     * 定位回调
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        cancelShowLoadingDialog();
        if (null != aMapLocation && !first) {
            first = true;
            Double latitude = aMapLocation.getLatitude();
            Double longitude = aMapLocation.getLongitude();
            myLatLon = new LatLng(latitude, longitude);
            locationCenter(latitude, longitude);

            // 让两个点显示在一个地图上
            latLngs.add(myLatLon);
            LocationUtils.zoomToSpanWithCenter(aMap, truckLatLng, latLngs);
        }
    }

    /**
     * 刷新，切换到司机为中心点
     */
    @OnClick(R.id.iv_refresh)
    public void refreshClick() {
        if(extra != null && truckLatLng != null) {
            getMyTitleBar().setTitleText(extra.getTruck_plate());
            locationCenter(truckLatLng.latitude, truckLatLng.longitude);
            LocationUtils.zoomToSpanWithCenter(aMap, truckLatLng, latLngs);
        }
    }

    /**
     * 定位，切换到自己为中心点
     */
    @OnClick(R.id.iv_location)
    public void locationClick() {
        if(myLatLon != null) {
            getMyTitleBar().setTitleText(getString(R.string.my_location));
            locationCenter(myLatLon.latitude, myLatLon.longitude);
        }
    }

    /**
     * 打电话
     */
    @OnClick(R.id.iv_call)
    public void callClick(final View view) {
        CheckUserStatusUtils.isRealNameAuthentication(this, new CheckUserStatusUtils.OnJudgeResultListener() {
            @Override
            public void onSucceed() {
                if (StringUtils.isNotBlank(extra.getMobile()) && StringUtils.isNotBlank(extra.getName())) {
                    new CallPhoneUtils().showCallDialogWithAdvNotUpload((MvpActivity) mContext, view, extra.getName(), extra.getMobile(), 3);
                }
            }
        });
    }

    @Override
    protected void onClickRightText() {
        super.onClickRightText();
        if (extra != null) {
            CheckUserStatusUtils.isRealNameAuthentication(this, new CheckUserStatusUtils.OnJudgeResultListener() {
                @Override
                public void onSucceed() {
                    TruckInfoActivity.truckInfo((MvpActivity) mContext, extra.getTruckId());
                }
            });
        }
    }
}
