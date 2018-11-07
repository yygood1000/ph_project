package com.topjet.common.widget.cluster;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public interface ClusterRender {
    /**
     * 根据聚合点的元素数目返回渲染背景样式
     *
     * @param mClusterItems 聚合点中有元素集合
     * @param clusterNum    聚合点中有多少个元素
     * @return 返回背景样式
     */
    Drawable getDrawAble(List<ClusterItem> mClusterItems, int clusterNum);
}
