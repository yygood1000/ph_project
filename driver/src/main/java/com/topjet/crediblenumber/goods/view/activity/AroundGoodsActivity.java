package com.topjet.crediblenumber.goods.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.common.modle.bean.AroundMapExtra;
import com.topjet.common.common.modle.bean.DestinationListItem;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.common.modle.bean.SearchAddressResultExtra;
import com.topjet.common.common.modle.bean.StatisticalData;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.common.view.activity.SearchAddressActivity;
import com.topjet.common.common.view.dialog.CitySelectPopupWindow;
import com.topjet.common.common.view.dialog.TruckLengthAndTypePopupWindow;
import com.topjet.common.config.CConstants;
import com.topjet.common.resource.bean.AreaInfo;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.common.widget.RecyclerViewWrapper.RecyclerViewWrapper;
import com.topjet.common.widget.cluster.ClusterClickListener;
import com.topjet.common.widget.cluster.ClusterItem;
import com.topjet.common.widget.cluster.ClusterOverlay;
import com.topjet.common.widget.cluster.ClusterRender;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.bean.DestinationsSelectEvent;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommand;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommandAPI;
import com.topjet.crediblenumber.goods.presenter.AroundGoodsPresenter;
import com.topjet.crediblenumber.goods.presenter.ProcessGoodsListAdv;
import com.topjet.crediblenumber.goods.view.adapter.GoodsListAdapter;
import com.topjet.crediblenumber.goods.view.dialog.BidOrAlterDialog;
import com.topjet.crediblenumber.goods.view.dialog.DestinationPopup;
import com.topjet.crediblenumber.order.view.activity.OrderDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yy on 2017/12/28.
 * 附近货源 页面
 */

public class AroundGoodsActivity extends BaseRecyclerViewActivity<AroundGoodsPresenter, GoodsListData>
        implements AroundGoodsView<GoodsListData>, ClusterRender {
    @BindView(R.id.iv_title_right_map)
    ImageView ivTitleRightMap;
    @BindView(R.id.iv_title_right_list)
    ImageView ivTitleRightList;
    @BindView(R.id.iv_title_right_search)
    ImageView ivTitleRightSearch;

    @BindView(R.id.iv_destination_arrows_down)
    ImageView ivDestinationArrows;
    @BindView(R.id.iv_truck_type_arrows_down)
    ImageView ivTruckTypeArrows;
    @BindView(R.id.tv_truck_typelength)
    TextView tvTruckTypeLength;
    @BindView(R.id.tv_destination)
    TextView tvDestination;
    @BindView(R.id.rl_options)
    RelativeLayout rlOptions;

    @BindView(R.id.layout_map)
    RelativeLayout layoutMap;
    @BindView(R.id.iv_local_point)
    ImageView ivLocalPoint;
    @BindView(R.id.map)
    MapView mapView;

    public DestinationPopup destinationPopup;// 目的地弹窗管理类
    private TruckLengthAndTypePopupWindow truckLengthAndTypePopupWindow;//车型车长弹窗

    /**
     * 地图先关变量/常量
     */
    private AMap aMap = null; // 地图对象
    private Map<Integer, Drawable> mBackDrawAbles = new HashMap<>();
    // 点聚合操作类
    private ClusterOverlay mClusterOverlay;
    // 是否需要进行请求,当筛选条件改变时，切换到地图页面需要重新请求
    private boolean isNeedToGetNewData;
    // 是否第一次切换到地图页,如果是第一次切换到地图页面，需要根据定位移动地图中心点
    private boolean isFristChangeToMap = true;
    // 地图信息窗是否正在显示
    private boolean isShowingInfoWindow;
    // 正在显示信息窗的Marker
    private Marker showInfoWindowMarker;

    @Override
    protected int getLayoutResId() {
        noHasTitle();
        return R.layout.activity_around_goods;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new AroundGoodsPresenter(this, mContext, new GoodsCommand(GoodsCommandAPI.class, this));
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
        super.initView();
        // 初始化空数据列表视图
        recyclerViewWrapper.getTvEmpty().setText(ResourceUtils.getString(R.string.goods_list_empty_warning));
        recyclerViewWrapper.getTvBtnEmpty().setText(ResourceUtils.getString(R.string.click_to_refresh));
        // 设置列表加载数据完成回调
        setFinishListener(setDataFinishListener);
        // 初始化目的地弹窗
        initDestinationPopupWindow();
        initTruckSelectPopWindow();
    }

    @Override
    protected void initData() {
        // 获取列表广告数据
        mPresenter.getGoodsListAdv();
        // 开始定位，定位后获取数据
        mPresenter.getLocate(false);
    }

    /**
     * 刷新地图数据
     */
    @Override
    public void refreshMapData() {
        mPresenter.getAroundGoodsMapData(destinationPopup.getSelectedDestIds());
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        GoodsListAdapter mAdapter = new GoodsListAdapter(this, true);
        mAdapter.setOnPayForDepositResultListener(new BidOrAlterDialog.OnPayForDepositResultListener() {
            @Override
            public void onSucceed() {
                refresh();
            }
        });
        return mAdapter;
    }

    @Override
    public void loadData() {
        if (page == CConstants.PAGE_START) {
            mPresenter.resetAdvInsertStatus();
        }
        mPresenter.getListData(page, destinationPopup.getSelectedDestIds());
    }

    /* TODO===============================插入广告相关代码块===============================*/

    /**
     * 单次请求成功并且设置数据完成回调,用于插入广告
     */
    RecyclerViewWrapper.OnSetDataFinishListener setDataFinishListener = new RecyclerViewWrapper
            .OnSetDataFinishListener() {
        @Override
        public void onFinish() {
            if (!ListUtils.isEmpty(mPresenter.advListData)) {
                insertAdvInfoToList();
            }
        }
    };

    /**
     * 插入广告操作
     */
    @Override
    public void insertAdvInfoToList() {
        ProcessGoodsListAdv.insertAdvInfoToList(getData(), mPresenter.advListData);
        // 刷新列表
        notifyDataSetChanged();
    }

    /* TODO===============================插入广告相关代码块===============================*/
    /* TODO===============================地图页面相关代码块===============================*/

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
        // 对amap添加移动地图事件监听器
        aMap.setOnCameraChangeListener(onCameraChangeListener);
        // 设置泡泡窗点击事件
        aMap.setOnInfoWindowClickListener(onInfoWindowClickListener);
        // 设置自定义泡泡窗适配器
        aMap.setInfoWindowAdapter(imageInfoWindowAdapter);
        // 地图点击事件，清空信息窗
        aMap.setOnMapClickListener(onMapClickListener);
    }

    /**
     * 向地图上添加省市级别 集合点
     *
     * @param markerOptionList 省市级Marker点集合
     */
    @Override
    public void addMarkers(ArrayList<MarkerOptions> markerOptionList) {
        // 设置省市级别Marker点击事件

        aMap.addMarkers(markerOptionList, false);
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
                .CLUSTER_RADIUS), mContext);
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
            bitmapDrawable = mBackDrawAbles.get(1);
            if (bitmapDrawable == null) {
                bitmapDrawable = ResourceUtils.getDrawable(R.drawable.iv_icon_single_goods);
                mBackDrawAbles.put(1, bitmapDrawable);
            }
        } else {
            bitmapDrawable = mBackDrawAbles.get(2);
            if (bitmapDrawable == null) {
                bitmapDrawable = ResourceUtils.getDrawable(R.drawable.iv_bg_cluter_overlay);
                mBackDrawAbles.put(2, bitmapDrawable);
            }
        }
        return bitmapDrawable;
    }

    @Override
    public void clearMarker() {
        aMap.clear();
    }

    /**
     * 地图状态发生变化的监听接口
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
                            mPresenter.cityId = result.getAdCode();
                            // 地图状态变化结束，请求地图数据
                            refreshMapData();
                        }
                    });
        }
    };

    /**
     * 省市区级Marker点击事件
     */
    AMap.OnMarkerClickListener onMarkerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Logger.i("oye", "省市区界别 Marker点击事件");
            StatisticalData data = mPresenter.statisticalDatas.get((int) marker.getZIndex());
            if (mPresenter.zoom >= 3 && mPresenter.zoom <= 8) {//全国
                if (AreaDataDict.isSpecialFirstLevel(data.getCity_name().replace("市", ""))) {
                    // 显示全国范围，点击则是放大到某省，显示该省全部市的信息。如果是直辖市，级别直接到区
                    mPresenter.zoom = 11f;
                } else {
                    mPresenter.zoom = 9f;
                }
                moveAndZoomMap();
            } else if (mPresenter.zoom >= 9 && mPresenter.zoom <= 10) {//省
                mPresenter.zoom = 11f;
                // 放大地图
                moveAndZoomMap();
            } else if (mPresenter.zoom >= 11 && mPresenter.zoom <= 14) {//市
                //  跳转地图列表页展示页面，这里跳转后需要请求列表接口
                AroundMapExtra extra = new AroundMapExtra(
                        marker.getPosition().latitude,
                        marker.getPosition().longitude,
                        mPresenter.zoom + "",
                        data.getCity_id(),
                        destinationPopup.getSelectedDestIds(),
                        mPresenter.truckSelectedData,
                        data.getCity_name() + "(共" + data.getCountNotEnter() + "）");
                Logger.i("oye", "turnToAroundGoodsMapListActivity extra = " + extra.toString());
                turnToActivity(AroundGoodsMapListActivity.class, extra);
                AroundGoodsActivity.this.overridePendingTransition(R.anim.slide_in_bottom, com.topjet.common.R.anim
                        .anim_no);
                return true;
            }
            return true;
        }
    };

    /**
     * 高德地图聚合点点击事件
     */
    ClusterClickListener clusterClickListener = new ClusterClickListener() {
        @Override
        public void onClick(Marker marker, List<ClusterItem> clusterItems) {
            Logger.i("oye", "ClusterClickListener = " + clusterItems.toString());
            if (clusterItems.size() == 1) {
                showInfoWindowMarker = marker;
                isShowingInfoWindow = true;
                // 将构建信息窗的数据实体类赋值给marker
                marker.setObject(clusterItems.get(0));
                // 弹出泡泡窗
                marker.showInfoWindow();
            } else {
                // 弹出附近货源地图列表页面，列表所需要显示的数据集合为 clusterItems 中的数据，跳转后不需要请求列表接口
                ArrayList<GoodsListData> list = new ArrayList<>();
                for (ClusterItem item : clusterItems) {
                    list.add((GoodsListData) item);
                }
                AroundMapExtra extra = new AroundMapExtra();
                extra.setGoodsListData(list);
                turnToActivity(AroundGoodsMapListActivity.class, extra);
                overridePendingTransition(R.anim.slide_in_bottom, com.topjet.common.R.anim.anim_no);
            }
        }
    };

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
     * 泡泡窗点击事件
     */
    AMap.OnInfoWindowClickListener onInfoWindowClickListener = new AMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            // 跳转货源详情页面
            GoodsListData data = (GoodsListData) marker.getObject();
            OrderDetailActivity.toGoodsDetail(AroundGoodsActivity.this, data.getGoods_id());
        }
    };

    /**
     * 自定义infowinfow窗口 适配器
     */
    AMap.ImageInfoWindowAdapter imageInfoWindowAdapter = new AMap.ImageInfoWindowAdapter() {
        @Override
        public long getInfoWindowUpdateTime() {
            return 0;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            View infoWindow = AroundGoodsActivity.this.getLayoutInflater().inflate(R.layout.driver_info_window, null);
            render(marker, infoWindow);
            return infoWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    };

    /**
     * 自定义infowinfow窗口
     */
    private void render(Marker marker, View view) {
        GoodsListData data = (GoodsListData) marker.getObject();
        TextView tvAddress = ((TextView) view.findViewById(R.id.tv_address));
        TextView tvTruckInfo = ((TextView) view.findViewById(R.id.tv_truck_info));
        tvAddress.setText(data.getDestination_city());
        tvTruckInfo.setText(data.getThe_goods() + " " + data.getTuck_length_type());
    }

    /**
     * 根据坐标信息移动地图
     */
    @Override
    public void moveAndZoomMap() {
        ivLocalPoint.setVisibility(View.VISIBLE);
        aMap.moveCamera(mPresenter.creatCurrentCameraUpdata());
    }
    /* TODO===============================地图页面相关代码块===============================*/
    /* TODO===============================目的地相关代码块===============================*/

    /**
     * 初始化目的地POPWindow
     */
    public void initDestinationPopupWindow() {
        showLoadingDialog();
        destinationPopup = new DestinationPopup(this, TAG);
        destinationPopup.setOnDismissListener(new TruckLengthAndTypePopupWindow.OnCustomDismissListener() {
            @Override
            public void onDismiss() {
                tvDestination.setTextColor(ResourceUtils.getColor(R.color.text_color_222222));
                ivDestinationArrows.setBackgroundResource(R.drawable.arrows_down_gray);
            }
        });
        // 从服务端获取常跑城市列表集合
        mPresenter.getUsualCityList();
    }

    @Override
    public void setDestinationPopData(ArrayList<DestinationListItem> destinationCityList) {
        cancelShowLoadingDialog();
        // 初始化目的地下拉列表数据
        destinationPopup.initPop(destinationCityList, listener);
    }

    /**
     * 目的地弹窗，手动选择城市点击事件
     */
    private DestinationPopup.onShowCitySelectPopListener listener = new DestinationPopup.onShowCitySelectPopListener() {
        @Override
        public void showCitySelectPop(View view, int flag) {
            // 手动新增目的地,显示城市选择窗
            new CitySelectPopupWindow(TAG).showCitySelectPopupWindow((Activity) mContext, rlOptions, false, true, true);
        }

        @Override
        public void showLocation(AMapLocation aMapLocation) {

        }
    };

    /**
     * 手动新增目的地返回事件
     */
    @Subscribe
    public void onEvent(AreaSelectedData data) {
        if (data.getTag().equals(TAG)) {
            AreaInfo areaInfo = data.getAreaInfo();
            destinationPopup.setDestinationCityBySelf(new DestinationListItem(areaInfo.getLastCityId(), areaInfo
                    .getFullCityName(), true, "1"));
        }
    }

    /**
     * 目的地确认事件
     */
    @Subscribe
    public void onEvent(DestinationsSelectEvent event) {
        if (event.getTag().equals(TAG)) {
            if (mPresenter.isInListPage) {
                isNeedToGetNewData = true;
                refresh();
            } else {
                refreshMapData();
            }
        }
    }

    /* TODO===============================目的地相关代码块===============================*/
    /* TODO===============================车型车长相关代码块===============================*/

    /**
     * 车型车长选择
     */
    @Subscribe
    public void onEvent(TruckTypeLengthSelectedData truckSelectedData) {
        if (truckSelectedData.getTag().equals(TAG)) {
            mPresenter.truckSelectedData = truckSelectedData;
            if (mPresenter.isInListPage) {
                isNeedToGetNewData = true;
                refresh();
            } else {
                refreshMapData();
            }
        }
    }

    /**
     * 初始化PopWindow
     */
    private void initTruckSelectPopWindow() {
        truckLengthAndTypePopupWindow = new TruckLengthAndTypePopupWindow(TAG, true, true)
                .setOnDismissListener(new TruckLengthAndTypePopupWindow.OnCustomDismissListener() {
                    @Override
                    public void onDismiss() {
                        tvTruckTypeLength.setTextColor(ResourceUtils.getColor(R.color.text_color_222222));
                        ivTruckTypeArrows.setBackgroundResource(R.drawable.arrows_down_gray);
                    }
                });
    }

    /* TODO===============================车型车长相关代码块===============================*/

    /**
     * 页面内的点击事件
     */
    @OnClick({R.id.ll_truck_type, R.id.iv_title_right_map, R.id.iv_title_right_list, R.id.iv_title_right_search, R.id
            .ll_destination, R.id.iv_refresh, R.id.iv_location, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_truck_type:// 车型车长点击事件
                ivTruckTypeArrows.setBackgroundResource(R.drawable.arrows_down_blue);
                tvTruckTypeLength.setTextColor(ResourceUtils.getColor(R.color.text_color_6E90FF));
                truckLengthAndTypePopupWindow.showPopupWindow(this,
                        rlOptions,
                        false,
                        TruckLengthAndTypePopupWindow.ENTIRETY_NO_SELECTED,
                        false,
                        mPresenter.truckSelectedData);
                break;
            case R.id.ll_destination:// 目的地点击事件
                if (destinationPopup.showPop(rlOptions)) {
                    ivDestinationArrows.setBackgroundResource(R.drawable.arrows_down_blue);
                    tvDestination.setTextColor(ResourceUtils.getColor(R.color.text_color_6E90FF));
                }
                break;
            case R.id.iv_title_right_map:// 跳转地图页
                //  显示地图页面
                if (!recyclerViewWrapper.isLoading) {
                    changePageToMap();
                }
                break;
            case R.id.iv_title_right_list:// 跳转地图页
                //  返回列表页面
                changePageToList();
                break;
            case R.id.iv_title_right_search:
                // 跳转搜索录入页
                startActivityForResult(new Intent(AroundGoodsActivity.this, SearchAddressActivity.class), 1);
                break;
            case R.id.iv_refresh:// 刷新地图数据
                refreshMapData();
                break;
            case R.id.iv_location:// 地图重新定位
                mPresenter.getLocate(true);
                break;
            case R.id.iv_back://关闭
                if (mPresenter.isInListPage) {
                    finish();
                } else {
                    // 返回列表页面
                    changePageToList();
                }
                break;
        }
    }

    /**
     * 切换为列表
     */
    private void changePageToList() {
        mPresenter.isInListPage = true;
        // 显示列表页显示的布局
        recyclerViewWrapper.setVisibility(View.VISIBLE);
        ivTitleRightMap.setVisibility(View.VISIBLE);
        // 隐藏地图页显示的布局
        layoutMap.setVisibility(View.GONE);
        ivTitleRightList.setVisibility(View.GONE);
        ivTitleRightSearch.setVisibility(View.GONE);
        refresh();
    }

    /**
     * 切换为地图
     */
    private void changePageToMap() {
        mPresenter.isInListPage = false;
        // 隐藏列表页显示的布局
        recyclerViewWrapper.setVisibility(View.GONE);
        ivTitleRightMap.setVisibility(View.GONE);
        // 显示地图页显示的布局
        layoutMap.setVisibility(View.VISIBLE);
        ivTitleRightList.setVisibility(View.VISIBLE);
        ivTitleRightSearch.setVisibility(View.VISIBLE);
        if (isFristChangeToMap) {
            isFristChangeToMap = false;
            moveAndZoomMap();
        } else {
            if (isNeedToGetNewData) {
                refreshMapData();
            }
        }
    }

    /**
     * 录入 搜索地址 页面返回方法
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        SearchAddressResultExtra extra = getIntentExtra(data,
                SearchAddressResultExtra.getExtraName());
        if (extra != null) {
            // Adcode
            mPresenter.cityId = extra.adCode;
            // 纬度
            mPresenter.lat = extra.lat;
            // 经度
            mPresenter.lng = extra.lng;
            // 移动地图
            moveAndZoomMap();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        if (mClusterOverlay != null) {
            mClusterOverlay.onDestroy();
            mClusterOverlay = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
//        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mPresenter.isInListPage) {
            super.onBackPressed();
        } else {
            //TODO 返回列表页面
            changePageToList();
        }
    }
}
