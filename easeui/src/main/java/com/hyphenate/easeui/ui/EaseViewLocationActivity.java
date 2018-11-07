/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.ui;


import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.hyphenate.easeui.R;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.view.dialog.ChooseMapDialog;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.MyTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 查看位置
 */
public class EaseViewLocationActivity extends MvpActivity implements AMap.OnCameraChangeListener           // 移动地图回调
        , LocationUtils.OnLocationListener       // 定位回调
{
    TextView tvShowAddress;                          // 显示传入终点地址
    private ImageView iv_location = null;
    private TextView tv_navigation = null;      // 导航
    private MapView mapView = null;
    private AMap aMap = null;
    private double extraLatitude = 0;
    private double extraLongitude = 0;
    private String extraAddress = "";

    private List<Marker> markers = new ArrayList<>();   // marker集合，用于缩放到合适的级别显示所有marker
    private AutoDialog dialog = null;       // 是否去打开定位权限

    @Override
    protected int getLayoutResId() {
        return R.layout.ease_activity_view_location;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.view_localtion);
    }

    @Override
    public void initView() {


        tvShowAddress = ButterKnife.findById(this, R.id.tv_show_address);
        iv_location = ButterKnife.findById(this, R.id.iv_location);
        tv_navigation = ButterKnife.findById(this, R.id.tv_navigation);

        iv_location.setOnClickListener(this);
        tv_navigation.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        init();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String userName = intent.getStringExtra("username");
        extraLatitude = intent.getDoubleExtra("latitude", 0);
        extraLongitude = intent.getDoubleExtra("longitude", 0);
        extraAddress = intent.getStringExtra("address");
        String title = intent.getStringExtra("addressname");

        if (StringUtils.isNotBlank(extraAddress)) {
            tvShowAddress.setText(extraAddress);
        }
    }

    /**
     * 不知道在干嘛，反正初始化，一大堆
     */
    private void init() {
        aMap = mapView.getMap();

        aMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        UiSettings settings = aMap.getUiSettings();
        // 隐藏缩放控件
        settings.setZoomControlsEnabled(false);
        aMap.setOnCameraChangeListener(this);

        iv_location.performClick(); // 定位
    }

    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        if (id == R.id.iv_location) {   // 定位
            RxPermissions rxPermissions = new RxPermissions(this);
            if (rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION) && rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                LocationUtils.location(EaseViewLocationActivity.this, EaseViewLocationActivity.this);
            } else {
                dialog = AutoDialogHelper.showSettingLocationPermission(this);
                dialog.show();
            }
        } else if (id == R.id.tv_navigation) {
            /**
             * 弹窗去导航 高德或者百度
             */
            new ChooseMapDialog(this,
                    extraAddress,
                    "" + extraLatitude,
                    "" + extraLongitude)
                    .show();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 地图移动
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onLocated(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            showMaker(latLng, R.drawable.iv_icon_location_point);
        }
    }

    /**
     * 显示maker
     */
    private void showMaker(LatLng latLng, int resoureId) {
        aMap.clear();

        markers.clear();
        LatLng endLatLng = new LatLng(extraLatitude, extraLongitude);
        MarkerOptions endMarkerOption = new MarkerOptions();
        endMarkerOption.position(endLatLng);

        endMarkerOption.draggable(false);// 设置Marker可拖动
        endMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.im_icon_position_end)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        endMarkerOption.setFlat(false);//设置marker平贴地图效果

        Animation endAnimation = new AlphaAnimation(0, 1);
        endAnimation.setDuration(300);
        endAnimation.setInterpolator(new LinearInterpolator());
        final Marker endMarker = aMap.addMarker(endMarkerOption);
        markers.add(endMarker);

        if (latLng == null) {
            return;
        }
        // 移动中心点 自己的位置
        latLng = new LatLng((latLng.latitude * 2) - endLatLng.latitude,
                (latLng.longitude * 2) - endLatLng.longitude);

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);

        markerOption.draggable(false);// 设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), resoureId)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(false);//设置marker平贴地图效果

        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(300);
        animation.setInterpolator(new LinearInterpolator());
        final Marker marker = aMap.addMarker(markerOption);

        marker.setAnimation(animation);
        marker.startAnimation();

        markers.add(marker);
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();//存放所有点的经纬度
        for (int i = 0; i < markers.size(); i++) {
            boundsBuilder.include(markers.get(i).getPosition());//把所有点都include进去（LatLng类型）
        }
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 15));//第二个参数为四周留空宽度
    }

    @Override
    public void onLocationPermissionfaild() {

    }
}
