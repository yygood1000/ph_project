package com.topjet.shipper.deliver.view.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.StreetNumber;
import com.amap.api.services.help.Tip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.extra.CityAndLocationExtra;
import com.topjet.common.utils.AMapUtil;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.presenter.SelectAddressMapPresenter;
import com.topjet.shipper.deliver.view.adapter.PoiAddressAdapter;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * 拖选和输入 选择详细地址页
 * 货主版
 */
public class SelectAddressMapActivity extends MvpActivity<SelectAddressMapPresenter> implements SelectAddressMapView
        , AMap.OnCameraChangeListener           // 移动地图回调
        , GeocodeSearch.OnGeocodeSearchListener // 地理反编码回调，输入天安门返回经纬度
        , AMap.OnMyLocationChangeListener       // 定位回调
        , LocationUtils.onGetInputTipsListener  // 自定义的输入框自动提示集合数据回调接口
        , BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.tv_city)
    TextView tvCity;                            //城市
    @BindView(R.id.tv_address)
    TextView tvAddress;                      // 输入框
    @BindView(R.id.ll_input)
    LinearLayout llInput;                     // 输入框外层


    @BindView(R.id.iv_clear)
    ImageView ivClear;                     // 清空
    @BindView(R.id.et_address)
    EditText etAddress;                     // 输入框
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;                // 输入框外层

    @BindView(R.id.rv_poi_address)
    RecyclerView rvPoiAddress;              // 显示搜索结果
    private PoiAddressAdapter poiAddressAdapter = null;

    private MapView mapView = null;
    private AMap aMap = null;
    private LatLonPoint centerLatLng = null;
    private LatLonPoint oldLatLng = null;
    private GeocodeSearch geocoderSearch = null;
    private List<Tip> tipItems = null;
    private boolean editChangeFromUser = false;         // 是否来自用户输入

    private String city = null;
    private double la = 0;
    private double lo = 0;
    private double tempLa = 0;
    private double tempLo = 0;
    private CityAndLocationExtra cityAndLocationExtra = null;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_address_map;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SelectAddressMapPresenter(this, mContext);
    }

    @Override
    public void initView() {
        if (CMemoryData.isDriver()) {
            setStatusBarViewDrawableResource(R.drawable.shape_bg_gradient_driver);
        } else {
            setStatusBarViewDrawableResource(R.drawable.shape_bg_gradient_shipper);
        }
        getMyTitleBar().hideToolBar();

        RxTextView.textChanges(etAddress)
                .debounce(200, TimeUnit.MILLISECONDS)
                .compose(this.<CharSequence>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        if (editChangeFromUser) {
                            String queryKey = charSequence.toString().trim();
                            if (!TextUtils.isEmpty(queryKey)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivClear.setVisibility(View.VISIBLE);
                                    }
                                });
                                doSearchQuery(queryKey);
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivClear.setVisibility(View.GONE);
                                    }
                                });
                            }
                        } else {
                            editChangeFromUser = true;
                        }
                    }
                });

        poiAddressAdapter = new PoiAddressAdapter(R.layout.listitem_just_textview);
        rvPoiAddress.setLayoutManager(new LinearLayoutManager(this));
        rvPoiAddress.setAdapter(poiAddressAdapter);
        poiAddressAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        init();
    }

    /**
     * 不知道在干嘛，反正初始化，一大堆
     */
    private void init() {
        aMap = mapView.getMap();
        aMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.abcdefghijklmnopqrstuvwxyz));
        myLocationStyle.strokeColor(0x00000000);
        myLocationStyle.strokeWidth(0);
        myLocationStyle.radiusFillColor(0x00000000);
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE));
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        aMap.setOnMyLocationChangeListener(this);
        aMap.setOnCameraChangeListener(this);

        cityAndLocationExtra = getIntentExtra(CityAndLocationExtra.getExtraName());
        city = cityAndLocationExtra.getCityName();
        if (!TextUtils.isEmpty(city)) {
            tvCity.setText(city);
        }

        String laStr = cityAndLocationExtra.getLatitude();
        String loStr = cityAndLocationExtra.getLongitude();
        if (!TextUtils.isEmpty(laStr)) {
            la = Double.parseDouble(laStr);
            tempLa = la;
        }
        if (!TextUtils.isEmpty(loStr)) {
            lo = Double.parseDouble(loStr);
            tempLo = lo;
        }
        if (la > 0 && lo > 0) {
            centerLatLng = new LatLonPoint(la, lo);
            oldLatLng = new LatLonPoint(la, lo);
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(centerLatLng), 15));
        }
//            else {
//            产品说不需要定位
//            aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
//            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//            }
    }

    @OnClick({R.id.ll_input, R.id.iv_clear, R.id.iv_break, R.id.tv_confirm})
    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        if (id == R.id.ll_input) {   // 下方输入的外层
            rlSearch.setVisibility(View.VISIBLE);
            tvCity.setVisibility(View.GONE);
            llInput.setVisibility(View.GONE);
            etAddress.requestFocus();
        } else if (id == R.id.iv_clear) {   // 清除输入
            editChangeFromUser = false;
            etAddress.setText("");
            ivClear.setVisibility(View.GONE);
        } else if (id == R.id.iv_break) {   // 返回
            this.finish();
        } else if (id == R.id.tv_confirm) {   // 确定
            if (StringUtils.isEmpty(etAddress.getText().toString())) {
                cityAndLocationExtra.setLatitude(tempLa + "");
                cityAndLocationExtra.setLongitude(tempLo + "");
                cityAndLocationExtra.setAddress("");
            }
            this.setResultAndFinish(RESULT_OK, cityAndLocationExtra);
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
     * 开始进行搜索
     */
    protected void doSearchQuery(String keyWord) {
        LocationUtils.getInputTipsList(this, keyWord, city, this);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 地图移动
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        centerLatLng = AMapUtil.convertToLatLonPoint(cameraPosition.target);
        /**
         * 设置回传数据
         */
        cityAndLocationExtra.setLatitude(centerLatLng.getLatitude() + "");
        cityAndLocationExtra.setLongitude(centerLatLng.getLongitude() + "");

        RegeocodeQuery query = new RegeocodeQuery(centerLatLng, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 逆地理编码，坐标获取地址名
    }

    /**
     * 逆地理编码
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null) {
                StreetNumber streetNumber = result.getRegeocodeAddress().getStreetNumber(); // 返回的门牌信息
                String addressName = null;
                if (streetNumber != null) {
                    addressName = streetNumber.getStreet() + streetNumber.getNumber();// 返回门牌信息中的街道名称
                }
                editChangeFromUser = false;

                String formatAddress = result.getRegeocodeAddress().getFormatAddress();


                if (StringUtils.isEmpty(addressName) && StringUtils.isNotBlank(formatAddress)) {
                    addressName = formatAddress.replace(result.getRegeocodeAddress().getProvince(), "").replace(result.getRegeocodeAddress().getCity(), "").replace(result.getRegeocodeAddress().getDistrict(), "");
                }
                etAddress.setText(addressName);
                tvAddress.setText(addressName);
                /**
                 *
                 * 设置回传数据
                 */
                cityAndLocationExtra.setBackwards2Name(result.getRegeocodeAddress().getCity() + " " + result.getRegeocodeAddress().getDistrict());
                cityAndLocationExtra.setAddress(addressName);
                cityAndLocationExtra.setAdCode(result.getRegeocodeAddress().getAdCode());
                Logger.d("TTT","city:"+city);
                Logger.d("TTT","result.getRegeocodeAddress().getFormatAddress():"+result.getRegeocodeAddress().getFormatAddress());
                if (!result.getRegeocodeAddress().getFormatAddress().contains(city)) {
                    Toaster.showLongToast(StringUtils.format(R.string.dont_exceed_city, city));
                    centerLatLng = oldLatLng;
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(centerLatLng), 15));
                }
            }
        }

    }

    /**
     * 定位回调
     */
    @Override
    public void onMyLocationChange(Location location) {
        if (location != null) {
            centerLatLng = AMapUtil.convertToLatLonPoint(location);
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(centerLatLng), 15));
        }
    }

    /**
     * RecyclerView 点击回调
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        editChangeFromUser = false;
        etAddress.setText(tipItems.get(position).getAddress());
        centerLatLng = tipItems.get(position).getPoint();
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(centerLatLng), 15));
        rvPoiAddress.setVisibility(View.GONE);

        /**
         * 设置回传数据
         */
        cityAndLocationExtra.setAddress(tipItems.get(position).getName());
        cityAndLocationExtra.setBackwards2Name(city + " " + tipItems.get(position).getDistrict());
        cityAndLocationExtra.setLatitude(centerLatLng.getLatitude() + "");
        cityAndLocationExtra.setLongitude(centerLatLng.getLongitude() + "");
    }

    @Override
    public void onGetInputTips(List<Tip> result) {
        tipItems = result;
        if (tipItems != null && tipItems.size() > 0) {
            if (editChangeFromUser) {
                rvPoiAddress.setVisibility(View.VISIBLE);
                poiAddressAdapter.setNewData(tipItems);
            }
        }
    }

    @Override
    public void onGetInputTipsFail() {
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }
}
