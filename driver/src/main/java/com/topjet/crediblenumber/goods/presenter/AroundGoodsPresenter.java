package com.topjet.crediblenumber.goods.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.topjet.common.adv.modle.bean.GoodsListAdvBean;
import com.topjet.common.adv.modle.response.GetGoodsListAdvResponse;
import com.topjet.common.adv.modle.serverAPI.AdvCommand;
import com.topjet.common.adv.modle.serverAPI.AdvCommandAPI;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.common.modle.bean.StatisticalData;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.cluster.ClusterItem;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.params.AroundGoodsListParams;
import com.topjet.crediblenumber.goods.modle.params.AroundGoodsMapParams;
import com.topjet.crediblenumber.goods.modle.response.AroundGoodsListResponse;
import com.topjet.crediblenumber.goods.modle.response.AroundGoodsMapResponse;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommand;
import com.topjet.crediblenumber.goods.view.activity.AroundGoodsView;
import com.topjet.crediblenumber.goods.view.dialog.DestinationPopup;
import com.topjet.crediblenumber.user.modle.response.GetUsualCityListResponse;
import com.topjet.crediblenumber.user.modle.serverAPI.UsualCityCommand;
import com.topjet.crediblenumber.user.modle.serverAPI.UsualCityCommandAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * 附近货源
 */

public class AroundGoodsPresenter extends BaseApiPresenter<AroundGoodsView<GoodsListData>, GoodsCommand> {
    // 广告集合
    public ArrayList<GoodsListAdvBean> advListData = new ArrayList<>();
    // 纬度
    public double lat = AreaDataDict.DEFAULT_CITY_LAT;
    // 经度
    public double lng = AreaDataDict.DEFAULT_CITY_LNG;
    // 城市ID
    public String cityId = AreaDataDict.DEFAULT_CITY_CODE;
    // 缩放级别
    public float zoom = 10f;
    // 车型车长选中结果
    public TruckTypeLengthSelectedData truckSelectedData;
    public ArrayList<StatisticalData> statisticalDatas;

    // 是否在附近货源列表页面
    public boolean isInListPage = true;

    public AroundGoodsPresenter(AroundGoodsView<GoodsListData> mView, Context mContext, GoodsCommand
            mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 获取列表 广告 数据
     */
    public void getGoodsListAdv() {
        new AdvCommand(AdvCommandAPI.class, mActivity).getGoodsListAdv(new ObserverOnResultListener<GetGoodsListAdvResponse>() {
            @Override
            public void onResult(GetGoodsListAdvResponse response) {
                advListData.addAll(response.getList());
                // 货源列表不为空，往货源集合中插入数据
                mView.insertAdvInfoToList();
            }
        });
    }

    /**
     * 定位当前位置，这里需要添加
     */
    public void getLocate(final boolean isShowLoading) {
        if (isShowLoading) {
            mView.showLoadingDialog();
        }
        LocationUtils.location(mContext, new LocationUtils.OnLocationListener() {
            @Override
            public void onLocated(AMapLocation aMapLocation) {
                if (isShowLoading) {
                    mView.cancelShowLoadingDialog();
                }
                if (aMapLocation.getErrorCode() == LocationUtils.LOCATE_SUCCEED) {
                    lat = aMapLocation.getLatitude();
                    lng = aMapLocation.getLongitude();
                    cityId = aMapLocation.getAdCode();
                } else {
                    setDefaultValue();
                }
                if (isInListPage) {
                    // 获取附近货源列表数据
                    mView.refresh();
                } else {
                    mView.moveAndZoomMap();
                    // 刷新地图数据
                    mView.refreshMapData();
                }
            }

            // 定位权限获取失败
            @Override
            public void onLocationPermissionfaild() {
                if (isShowLoading) {
                    mView.cancelShowLoadingDialog();
                }
                setDefaultValue();
                if (isInListPage) {
                    // 获取附近货源列表数据
                    mView.refresh();
                } else {
                    mView.moveAndZoomMap();
                    // 刷新地图数据
                    mView.refreshMapData();
                }
            }
        });
    }

    /**
     * 定位失败，定位权限获取失败，设置默认值（上海）
     */
    private void setDefaultValue() {
        lat = AreaDataDict.DEFAULT_CITY_LAT;
        lng = AreaDataDict.DEFAULT_CITY_LNG;
        cityId = AreaDataDict.DEFAULT_CITY_CODE;
    }

    /**
     * 获取 附近货源 列表数据
     */
    public void getListData(int page, ArrayList<String> destination_city_ids) {
        AroundGoodsListParams params = new AroundGoodsListParams();
        params.setPage(page + "");
        params.setDestination_city_ids(destination_city_ids);
        params.setCity_id(cityId);
        params.setLatitude(lat + "");
        params.setLongitude(lng + "");
        params.setTruck_type_id(truckSelectedData != null ? truckSelectedData.getSingleTruckTypeId() : "");
        params.setTruck_length_id(truckSelectedData != null ? truckSelectedData.getSingleTruckLengthId() : "");

        mApiCommand.getAroundGoodsList(params, new ObserverOnNextListener<AroundGoodsListResponse>() {
            @Override
            public void onNext(AroundGoodsListResponse response) {
                mView.loadSuccess(response.getList());
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });
    }

    /**
     * 请求服务器,获取常跑城市列表
     * 获取成功后处理几个，构造出目的地列表需要的数据
     */
    public void getUsualCityList() {
        CommonParams params = new CommonParams(UsualCityCommandAPI.GET_USUAL_CITY_LIST);
        new UsualCityCommand(UsualCityCommandAPI.class, mActivity).getUsualCityList(params, new
                ObserverOnNextListener<GetUsualCityListResponse>() {
                    @Override
                    public void onNext(GetUsualCityListResponse getUsualCityListResponse) {
                        mView.setDestinationPopData(DestinationPopup.creatDestinationCityList
                                (getUsualCityListResponse.getList()));
                    }

                    @Override
                    public void onError(String errorCode, String msg) {
                        Logger.e("onError :" + "常跑城市列表数据获取失败");
                        mView.setDestinationPopData(DestinationPopup.creatDestinationCityList(null));
                    }
                });
    }

    /**
     * 重置广告的插入状态
     */
    public void resetAdvInsertStatus() {
        if (!ListUtils.isEmpty(advListData)) {
            for (GoodsListAdvBean a : advListData) {
                a.setInserted(false);
            }
        }
    }


    /**
     * 获取附近货源 地图页 数据
     * <p>
     * 1.地图页面请求数据都是在地图状态变更后才去获取。
     * 2.方法会在选择了目的地或者车型车长后调用
     */
    public void getAroundGoodsMapData(ArrayList<String> destination_city_ids) {
        Logger.i("oye", "获取附近货源 地图页 数据");
        AroundGoodsMapParams params = new AroundGoodsMapParams();
        params.setCity_id(cityId);
        params.setDestination_city_ids(destination_city_ids);
        params.setLatitude(lat + "");
        params.setLongitude(lng + "");
        params.setMap_level(zoom + "");
        params.setTruck_type_id(truckSelectedData != null ? truckSelectedData.getSingleTruckTypeId() : "");
        params.setTruck_length_id(truckSelectedData != null ? truckSelectedData.getSingleTruckLengthId() : "");

        mApiCommand.getAroundGoodsMap(params, new ObserverOnResultListener<AroundGoodsMapResponse>() {
            @Override
            public void onResult(AroundGoodsMapResponse response) {
                mView.clearMarker();
                // 地图数据返回，进行判断展示
                if (StringUtils.isEmpty(response.getParameter_level())) {
                    Logger.i("oye", "聚合标记 为空");
                    return;
                }
                if (response.getParameter_level().equals("1")) {// 1：群组信息级别，进行大头针展示
                    Logger.i("oye", "省市级，循环添加覆盖物");
                    // 添加省市级聚合点
                    statisticalDatas = response.getGoods_statistical();
                    creatMarkerOptions(response.getGoods_statistical());
                } else {
                    Logger.i("oye", "高德地图点聚合");
                    // 构造高德地图点聚合集合
                    creatClusterItemListAndAssign(response.getNear_goods_response_list());
                }
            }
        });
    }


     /* ========================== 构造地图覆盖物相关代码 ==========================*/

    /**
     * 添加自定义覆盖物，实现省市级 统计Marker
     */
    private void creatMarkerOptions(ArrayList<StatisticalData> goods_statistical) {
        ArrayList<MarkerOptions> markerOptionList = new ArrayList<>();

        for (int i = 0; i < goods_statistical.size(); i++) {
            MarkerOptions markerOption = new MarkerOptions();
            StatisticalData data = goods_statistical.get(i);
            // 设置Marker 经纬度
            markerOption
                    .position(new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude
                            ())))
                    .icon(getCustomMarkerContent(data))
                    .zIndex(i);
            markerOptionList.add(markerOption);
        }
        mView.addMarkers(markerOptionList);
    }

    /**
     * 创建聚合点覆盖物具体显示内容
     */
    private BitmapDescriptor getCustomMarkerContent(StatisticalData data) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_bg_around_goods_marker, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_marker);
        // 设置覆盖物文本
        textView.setText(data.getCity_name() + "\n" + data.getCount());
        return BitmapDescriptorFactory.fromView(view);
    }

    /**
     * 构造高德地图点聚合集合
     */
    private void creatClusterItemListAndAssign(ArrayList<GoodsListData> goodslist) {
        List<ClusterItem> items = new ArrayList<>();
        for (GoodsListData data : goodslist) {
            if (StringUtils.isNotBlank(data.getLatitude()) || StringUtils.isNotBlank(data.getLongitude())) {
                items.add(data);
            }
        }
        // 进行地图点聚合操作
        mView.assignClusters(items);
    }

    /**
     * 构造地图状态变换类
     */
    public CameraUpdate creatCurrentCameraUpdata() {
        return CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom);
    }
}
