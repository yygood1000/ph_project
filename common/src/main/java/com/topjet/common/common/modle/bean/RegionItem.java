package com.topjet.common.common.modle.bean;

import com.amap.api.maps.model.LatLng;
import com.topjet.common.widget.cluster.ClusterItem;

/**
 * Created by yy on 16/10/10.
 * 高德地图聚合点实体类
 */

public class RegionItem implements ClusterItem {
    private LatLng mLatLng;
    private GoodsListData goodsData;
    private TruckInfo truckData;

    public RegionItem(LatLng latLng, GoodsListData data) {
        mLatLng = latLng;
        goodsData = data;
    }


    public RegionItem(LatLng latLng, TruckInfo data) {
        mLatLng = latLng;
        truckData = data;
    }

    @Override
    public LatLng getPosition() {
        return mLatLng;
    }

    public GoodsListData getGoodsData() {
        return goodsData;
    }

    public TruckInfo getTruckData() {
        return truckData;
    }

    @Override
    public String toString() {
        return "RegionItem{" +
                "mLatLng=" + mLatLng +
                ", goodsData=" + goodsData +
                ", truckData=" + truckData +
                '}';
    }
}
