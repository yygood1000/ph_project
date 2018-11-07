package com.topjet.common.widget.cluster;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyi.qi on 16/10/10.
 * <p>
 * 聚合点持有者
 * 该类用与在地图上做显示聚合点
 */

public class Cluster {
    private LatLng mLatLng;
    // 实现了ClusterItem接口的原始聚合数据
    private List<ClusterItem> mClusterItems;
    private Marker mMarker;

    Cluster(LatLng latLng) {

        mLatLng = latLng;
        mClusterItems = new ArrayList<ClusterItem>();
    }

    void addClusterItem(ClusterItem clusterItem) {
        mClusterItems.add(clusterItem);
    }

    int getClusterCount() {
        return mClusterItems.size();
    }


    LatLng getCenterLatLng() {
        return mLatLng;
    }

    void setMarker(Marker marker) {
        mMarker = marker;
    }

    Marker getMarker() {
        return mMarker;
    }

    List<ClusterItem> getClusterItems() {
        return mClusterItems;
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "mLatLng=" + mLatLng +
                ", mClusterItems=" + mClusterItems +
                ", mMarker=" + mMarker +
                '}';
    }
}
