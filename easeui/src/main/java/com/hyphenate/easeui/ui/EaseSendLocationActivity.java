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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.help.Tip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.IMNearPoiAddressAdapter;
import com.hyphenate.easeui.adapter.PoiAddressAdapter;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.AMapUtil;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.PermissionsUtils;
import com.topjet.common.widget.MyTitleBar;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * 发送位置
 */
public class EaseSendLocationActivity extends MvpActivity implements AMap.OnCameraChangeListener           // 移动地图回调
        , LocationUtils.OnLocationListener       // 定位回调
        , LocationUtils.onGetInputTipsListener  // 自定义的输入框自动提示集合数据回调接口
        , BaseQuickAdapter.OnItemClickListener {
    private PoiAddressAdapter poiAddressAdapter = null;
    private IMNearPoiAddressAdapter imNearPoiAddressAdapter = null;
    private double extraLatitude = 0;
    private double extraLongitude = 0;
    private String extraAddress = "";
    private String extraName = "";

    TextView tvCity;                          // 城市
    TextView tvAddress;                      // 输入框
    LinearLayout llInput;                    // 输入框外层
    EditText etAddress;                     // 输入框
    RelativeLayout rlSearch;                // 输入框外层
    RecyclerView rvPoiSearch;              // 显示搜索结果
    RecyclerView rvPoiNear;                // 显示附近搜索结果
    private ImageView iv_clear = null;
    private TextView tv_cancel = null;
    private ImageView iv_location = null;

    private MapView mapView = null;
    private AMap aMap = null;
    private LatLonPoint centerLatLng = null;
    private List<Tip> tipItems = null;
    private List<PoiItem> bottomTipItems = null;
    private boolean editChangeFromUser = false;         // 是否来自用户输入
    private boolean clickBottomListFromUser = false;   // 用户点击下方检索，移动地图，不再进行新的检索
    private AutoDialog dialog = null;   // 是否去打开定位权限

    boolean isSetLayoutParamsed = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.ease_activity_baidumap;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT).setTitleText(R.string.send_localtion).setRightText(R
                .string.send);
    }

    @Override
    public void initView() {
        tvCity = ButterKnife.findById(this, R.id.tv_city);
        tvAddress = ButterKnife.findById(this, R.id.tv_address);
        llInput = ButterKnife.findById(this, R.id.ll_input);
        etAddress = ButterKnife.findById(this, R.id.et_address);
        rlSearch = ButterKnife.findById(this, R.id.rl_search);
        rvPoiSearch = ButterKnife.findById(this, R.id.rv_poi_search);
        rvPoiNear = ButterKnife.findById(this, R.id.rv_poi_near);
        iv_clear = ButterKnife.findById(this, R.id.iv_clear);
        tv_cancel = ButterKnife.findById(this, R.id.tv_cancel);
        iv_location = ButterKnife.findById(this, R.id.iv_location);

        llInput.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        iv_location.setOnClickListener(this);

        RxTextView.textChanges(etAddress)
                .debounce(200, TimeUnit.MILLISECONDS)
                .compose(this.<CharSequence>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        if (editChangeFromUser) {
                            String queryKey = charSequence.toString().trim();
                            if (!TextUtils.isEmpty(queryKey)) {
                                doSearchQuery(queryKey);
                            }
                        } else {
                            editChangeFromUser = true;
                        }
                    }
                });

        poiAddressAdapter = new PoiAddressAdapter(R.layout.listitem_just_textview);
        rvPoiSearch.setLayoutManager(new LinearLayoutManager(this));
        rvPoiSearch.setAdapter(poiAddressAdapter);
        poiAddressAdapter.setOnItemClickListener(this);

        imNearPoiAddressAdapter = new IMNearPoiAddressAdapter(R.layout.listitem_im_near_poi);
        rvPoiNear.setAdapter(imNearPoiAddressAdapter);
        imNearPoiAddressAdapter.setOnItemClickListener(this);
        rvPoiNear.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public void onMeasure(final RecyclerView.Recycler recycler, RecyclerView.State state, final int widthSpec, final int heightSpec) {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
                if (bottomTipItems != null) {
                    int maxHeightCount = bottomTipItems.size();
                    if (maxHeightCount > 3) {
                        maxHeightCount = 3;
                    }

                    if (maxHeightCount > 0) {
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rvPoiNear.getLayoutParams();
                        lp.setMargins(0, 0, 0, 0);

                        final int finalMaxHeightCount = maxHeightCount;
                        if (!isSetLayoutParamsed) {
                            isSetLayoutParamsed = true;
                            rvPoiNear.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (getChildCount() > 0) {
                                        View view = recycler.getViewForPosition(0);
                                        if (view != null) {
                                            measureChild(view, widthSpec, heightSpec);
                                            int measuredHeight = view.getMeasuredHeight();
                                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rvPoiNear.getLayoutParams();
                                            lp.height = measuredHeight * finalMaxHeightCount;
                                            rvPoiNear.setLayoutParams(lp);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        init();
    }

    @Override
    protected void onClickRightText() {
        Intent intent = new Intent();
        intent.putExtra("latitude", extraLatitude);
        intent.putExtra("longitude", extraLongitude);
        intent.putExtra("address", extraAddress);
        intent.putExtra("name", extraName);
        this.setResult(RESULT_OK, intent);
        this.finish();
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
        if (id == R.id.ll_input) {   // 下方输入的外层
            getMyTitleBar().setVisibility(View.GONE);
            rlSearch.setVisibility(View.VISIBLE);
            tvCity.setVisibility(View.GONE);
            llInput.setVisibility(View.GONE);
            etAddress.requestFocus();
        } else if (id == R.id.iv_clear) {   // 清除输入
            editChangeFromUser = false;
            etAddress.setText("");
        } else if (id == R.id.tv_cancel) {   // 取消
            getMyTitleBar().setVisibility(View.VISIBLE);
            rvPoiSearch.setVisibility(View.GONE);
            llInput.setVisibility(View.VISIBLE);
            rlSearch.setVisibility(View.GONE);
        } else if (id == R.id.iv_location) {   // 手动定位
            RxPermissions rxPermissions = new RxPermissions(this);
            if (rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION) && rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                LocationUtils.location(EaseSendLocationActivity.this, EaseSendLocationActivity.this);
            } else {
                showLocationPermissionDialog();
            }
        }
    }

    /**
     * 显示是否打开定位权限弹窗
     */
    private void showLocationPermissionDialog() {
        dialog = new AutoDialog(mContext);
        String versionName = "";
        if (CMemoryData.isDriver()) {
            versionName = "司机版";
        } else {
            versionName = "货主版";
        }
        dialog.setTitle("请在手机“设置”选项中，打开定位服务，允许560" + versionName + "访问您的位置");
        dialog.setRightText("去设置");
        dialog.setLeftText("暂不");
        dialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onLeftClick() {
                dialog.toggleShow();
            }

            @Override
            public void onRightClick() {
                dialog.toggleShow();
                PermissionsUtils.goToSyetemSetting(EaseSendLocationActivity.this);
            }
        });
        dialog.toggleShow();
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
     * 开始进行搜索
     */
    protected void doSearchQuery(String keyWord) {
        LocationUtils.getInputTipsList(this, keyWord, "", this);
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
        isSetLayoutParamsed = false;
        tv_cancel.performClick();
        centerLatLng = AMapUtil.convertToLatLonPoint(cameraPosition.target);
        /**
         * 设置回传数据
         */
        extraLatitude = centerLatLng.getLatitude();
        extraLongitude = centerLatLng.getLongitude();

        if (clickBottomListFromUser) {
            clickBottomListFromUser = false;
        } else {
            LocationUtils.reGeocodeSearch(this, centerLatLng, new LocationUtils.onReGeocodeSearchedListener() {
                @Override
                public void onReGeocodeSearched(RegeocodeAddress result) {
                    if (result != null && result.getFormatAddress() != null) {
                        String address = result.getFormatAddress();
                        editChangeFromUser = false;
                        etAddress.setText(address);
                        tvAddress.setText(address);

                        bottomTipItems = result.getPois();
                        if (bottomTipItems != null) {
                        }
                        if (bottomTipItems != null && bottomTipItems.size() > 0) {
                            for (int i = 0; i < bottomTipItems.size(); i++) {
                                if (bottomTipItems.get(i).getLatLonPoint() == null) {
                                    bottomTipItems.remove(i);
                                }
                            }

                            if (bottomTipItems != null && bottomTipItems.size() > 0) {
                                rvPoiNear.setVisibility(View.VISIBLE);
                                imNearPoiAddressAdapter.setCurItemId(bottomTipItems.get(0).getPoiId());
                                extraName = bottomTipItems.get(0).getTitle();
                                imNearPoiAddressAdapter.setNewData(bottomTipItems);
                            }
                        }

                        /**
                         * 设置回传数据
                         */
                        extraAddress = address;
                    }
                }
            });
        }
    }

    /**
     * RecyclerView 点击回调
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter instanceof PoiAddressAdapter) {
            editChangeFromUser = false;
            tv_cancel.performClick();
            if (tipItems != null && tipItems.size() > position) {
                centerLatLng = tipItems.get(position).getPoint();
                if (centerLatLng != null) {
                    etAddress.setText(tipItems.get(position).getAddress());
                    tvAddress.setText(tipItems.get(position).getAddress());
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(centerLatLng), 15));

                    /**
                     * 设置回传数据
                     */
                    extraAddress = tipItems.get(position).getAddress();
                    extraName = tipItems.get(position).getName();
                    extraLatitude = centerLatLng.getLatitude();
                    extraLongitude = centerLatLng.getLongitude();
                }
            }
        } else if (adapter instanceof IMNearPoiAddressAdapter) {
            editChangeFromUser = false;
            clickBottomListFromUser = true;
            if (bottomTipItems != null && bottomTipItems.size() > position) {
                imNearPoiAddressAdapter.setCurItemId(bottomTipItems.get(position).getPoiId());
                imNearPoiAddressAdapter.notifyDataSetChanged();

                etAddress.setText(bottomTipItems.get(position).getSnippet());
                tvAddress.setText(bottomTipItems.get(position).getSnippet());
                centerLatLng = bottomTipItems.get(position).getLatLonPoint();
                if (centerLatLng != null) {
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(centerLatLng), 15));

                    /**
                     * 设置回传数据
                     */
                    extraAddress = bottomTipItems.get(position).getSnippet();
                    extraName = bottomTipItems.get(position).getTitle();
                    extraLatitude = centerLatLng.getLatitude();
                    extraLongitude = centerLatLng.getLongitude();
                }
            }
        }
    }

    /**
     * 上面部分poi检索
     */
    @Override
    public void onGetInputTips(List<Tip> result) {
        tipItems = result;
        if (tipItems != null) {
        }
        if (tipItems != null && tipItems.size() > 0) {
            for (int i = 0; i < tipItems.size(); i++) {
                if (tipItems.get(i).getPoint() == null) {
                    tipItems.remove(i);
                }
            }

            if (tipItems != null && tipItems.size() > 0) {
                if (editChangeFromUser) {
                    rvPoiSearch.setVisibility(View.VISIBLE);
                    poiAddressAdapter.setNewData(tipItems);
                    rvPoiNear.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onGetInputTipsFail() {
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onLocated(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            aMap.clear();

            centerLatLng = AMapUtil.convertToLatLonPoint(aMapLocation);
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(centerLatLng), 15));

            LatLng latLng = new LatLng(centerLatLng.getLatitude(), centerLatLng.getLongitude());
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(latLng);

            markerOption.draggable(false);//设置Marker可拖动
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.drawable.iv_order_local_point)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(false);//设置marker平贴地图效果

            Animation animation = new AlphaAnimation(0, 1);
            animation.setDuration(300);
            animation.setInterpolator(new LinearInterpolator());
            final Marker marker = aMap.addMarker(markerOption);

            marker.setAnimation(animation);
            marker.startAnimation();
        }
    }

    @Override
    public void onLocationPermissionfaild() {

    }
}
