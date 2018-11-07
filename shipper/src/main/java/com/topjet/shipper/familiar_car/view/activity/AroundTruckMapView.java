package com.topjet.shipper.familiar_car.view.activity;

import com.amap.api.maps.model.MarkerOptions;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.widget.cluster.ClusterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 2017/7/29.
 * 附近车源地图
 */

public interface AroundTruckMapView extends IView {
    void setEditTextContent(String str);

    void moveAndZoomMap();

    void addMarkers(ArrayList<MarkerOptions> markerOptionList);

    void assignClusters(List<ClusterItem> clusterItemst);

    void clearMarker();

    void setTruckInfo(String truckLength, String truckType);

}
