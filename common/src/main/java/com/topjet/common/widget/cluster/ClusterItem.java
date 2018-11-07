package com.topjet.common.widget.cluster;

import com.amap.api.maps.model.LatLng;

/**
 * Created by yiyi.qi on 16/10/10.
 * <p>
 * 单个聚合点实例 必须实现该接口，用于计算聚合点
 */

public interface ClusterItem {
    /**
     * 返回聚合元素的经纬度
     */
    LatLng getPosition();
}
