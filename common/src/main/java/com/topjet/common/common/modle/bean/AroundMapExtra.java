package com.topjet.common.common.modle.bean;

import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;

import java.util.ArrayList;

/**
 * Created by yy on 2017/9/6.
 * <p>
 * 附近货源 列表 / 地图 页面间传递的数据
 */

public class AroundMapExtra extends BaseExtra {
    /**
     * 附近货源/车源 地图页面 跳转至 列表页面 或 地图列表页面 会使用到的字段
     */
    public double lat;
    public double lng;
    public String zoom;
    public String departCityId;
    public String destinationCityId;
    public TruckTypeLengthSelectedData truckSelectedData;

    public String departCityName;
    public String destinationCityName;

    // 是否需要获取Extra中的数据，情况为：附近车源页面再onResume 中获取内存数据，但是退到后台，再次进入页面也会获取，在不该获取的时候获取了，会导致请求参数被覆盖从而出现错误的数据。
    private boolean isNeedGetParams;

    /**
     * 被选中的目的地城市Id集合
     * 附近货源地图页面 跳转到 地图列表页面 需要该参数请求服务端
     * 注意：附近货源列表页面之所以不传，是因为附近货源列表页面会在初始化的时候从内存中获取一份 *总的目的地集合* 用与显示，
     * 请求时可通过popup.getSelectedDestId();获取到选中集合。
     */
    private ArrayList<String> selectedDestCityIds;
    /**
     * 地图列表页面标题，只要在需要请求
     */
    private String title;

    /**
     * 附近货源 地图列表页(区、街道缩放级别) 直接传递列表显示的数据集合
     */
    private ArrayList<GoodsListData> goodsListData;
    /**
     * 附近车源 地图列表页(区、街道缩放级别) 直接传递列表显示的数据集合
     */
    private ArrayList<TruckInfo> truckListData;

    public AroundMapExtra() {
    }

    /**
     * 附近货源 *地图页面* 跳转 *列表页面*
     *
     * @param lat                纬度
     * @param lng                经度
     * @param zoom               缩放级别
     * @param departCityId       城市ID
     * @param trcuckSelectedData 车型车长
     */
    public AroundMapExtra(double lat, double lng, String zoom, String
            departCityId, TruckTypeLengthSelectedData trcuckSelectedData) {
        this.lat = lat;
        this.lng = lng;
        this.zoom = zoom;
        this.departCityId = departCityId;
        this.truckSelectedData = trcuckSelectedData;
    }

    /**
     * 跳转 附近货源 *地图列表* 页面，区级显示时，将请求参数传入列表页面，请求获取列表数据
     *
     * @param lat                纬度
     * @param lng                经度
     * @param zoom               缩放级别
     * @param departCityId       城市ID
     * @param destinationCityIds 目的地城市ID集合
     */
    public AroundMapExtra(double lat, double lng, String zoom, String departCityId, ArrayList<String>
            destinationCityIds, TruckTypeLengthSelectedData trcuckSelectedData, String title) {
        this.lat = lat;
        this.lng = lng;
        this.zoom = zoom;
        this.departCityId = departCityId;
        this.selectedDestCityIds = destinationCityIds;
        this.truckSelectedData = trcuckSelectedData;
        this.title = title;
    }

    /**
     * 跳转 附近车源 *地图列表* 页面，区级显示时，将请求参数传入列表页面，请求获取列表数据
     *
     * @param lat               纬度
     * @param lng               经度
     * @param zoom              缩放级别
     * @param departCityId      城市ID
     * @param destinationCityId 目的地城市ID
     */
    public AroundMapExtra(double lat, double lng, String zoom, String departCityId, String destinationCityId,
                          TruckTypeLengthSelectedData trcuckSelectedData, String title) {
        this.lat = lat;
        this.lng = lng;
        this.zoom = zoom;
        this.departCityId = departCityId;
        this.destinationCityId = destinationCityId;
        this.truckSelectedData = trcuckSelectedData;
        this.title = title;
    }

    /**
     * *我要用车*  -> *附近车源*
     */
    /**
     * @param lat                 经度
     * @param lng                 纬度
     * @param departCityId        出发地城市Id
     * @param destinationCityId   目的地城市Id
     * @param trcuckSelectedData  车型车长选择对象
     * @param departCityName      出发地城市名称
     * @param destinationCityName 目的地城市名称
     */
    public AroundMapExtra(double lat, double lng, String departCityId, String destinationCityId,
                          TruckTypeLengthSelectedData trcuckSelectedData,
                          String departCityName, String destinationCityName) {
        this.lat = lat;
        this.lng = lng;
        this.departCityId = departCityId;
        this.destinationCityId = destinationCityId;
        this.truckSelectedData = trcuckSelectedData;
        this.departCityName = departCityName;
        this.destinationCityName = destinationCityName;
    }


    public ArrayList<String> getSelectedDestCityIds() {
        return selectedDestCityIds;
    }

    public ArrayList<GoodsListData> getGoodsListData() {
        return goodsListData;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getZoom() {
        return zoom;
    }

    public String getDepartCityId() {
        return departCityId;
    }

    public String getTitle() {
        return title;
    }

    public String getDestinationCityId() {
        return destinationCityId;
    }

    public String getDepartCityName() {
        return departCityName;
    }

    public String getDestinationCityName() {
        return destinationCityName;
    }

    public void setDestinationCityId(String destinationCityId) {
        this.destinationCityId = destinationCityId;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public void setDepartCityId(String departCityId) {
        this.departCityId = departCityId;
    }

    public TruckTypeLengthSelectedData getTruckSelectedData() {
        return truckSelectedData;
    }

    public void setTruckSelectedData(TruckTypeLengthSelectedData trcuckSelectedData) {
        this.truckSelectedData = trcuckSelectedData;
    }

    public boolean isNeedGetParams() {
        return isNeedGetParams;
    }

    public void setNeedGetParams(boolean needGetParams) {
        isNeedGetParams = needGetParams;
    }

    public void setDepartCityName(String departCityName) {
        this.departCityName = departCityName;
    }

    public void setDestinationCityName(String destinationCityName) {
        this.destinationCityName = destinationCityName;
    }

    public void setGoodsListData(ArrayList<GoodsListData> goodsListData) {
        this.goodsListData = goodsListData;
    }

    public void setTruckListData(ArrayList<TruckInfo> truckListData) {
        this.truckListData = truckListData;
    }

    public ArrayList<TruckInfo> getTruckListData() {
        return truckListData;
    }

    @Override
    public String toString() {
        return "AroundMapExtra{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", zoom='" + zoom + '\'' +
                ", departCityId='" + departCityId + '\'' +
                ", destinationCityId='" + destinationCityId + '\'' +
                ", truckSelectedData=" + truckSelectedData +
                ", departCityName='" + departCityName + '\'' +
                ", destinationCityName='" + destinationCityName + '\'' +
                ", selectedDestCityIds=" + selectedDestCityIds +
                ", title='" + title + '\'' +
                ", goodsListData=" + goodsListData +
                '}';
    }
}
