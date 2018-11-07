package com.topjet.crediblenumber.goods.view.activity;

import com.amap.api.maps.model.MarkerOptions;
import com.topjet.common.base.view.activity.IListView;
import com.topjet.common.common.modle.bean.DestinationListItem;
import com.topjet.common.widget.cluster.ClusterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 2017/7/29.
 * 附近货源
 */

public interface AroundGoodsView<D> extends IListView<D> {

    void insertAdvInfoToList();

    void setDestinationPopData(ArrayList<DestinationListItem> destinationCityList);

    void addMarkers(ArrayList<MarkerOptions> markerOptionList);

    void assignClusters(List<ClusterItem> clusterItemst);

    void clearMarker();

    void refreshMapData();

    void moveAndZoomMap();
}
