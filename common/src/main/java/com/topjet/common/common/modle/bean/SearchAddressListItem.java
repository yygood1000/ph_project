package com.topjet.common.common.modle.bean;

import com.amap.api.services.core.LatLonPoint;

/**
 * 附近货源搜索页面 下拉列表 实体类
 */
public class SearchAddressListItem {
    private LatLonPoint latLonPoint;// 经纬度点
    private String address;// 详细地址
    private String districtName;// 第三级区名称
    private String adCode;// 提示区域编码。
    private boolean isSearch;// 是否是搜索联想数据

    @Override
    public String toString() {
        return "SearchAddressListItem{" +
                "latLonPoint=" + latLonPoint +
                ", address='" + address + '\'' +
                ", districtName='" + districtName + '\'' +
                ", adCode='" + adCode + '\'' +
                ", isSearch=" + isSearch +
                '}';
    }

    public LatLonPoint getLatLonPoint() {
        return latLonPoint;
    }

    public String getAddress() {
        return address;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getAdCode() {
        return adCode;
    }

    public SearchAddressListItem setSearch(boolean search) {
        isSearch = search;
        return this;
    }

    public boolean isSearch() {
        return isSearch;
    }

    public SearchAddressListItem(LatLonPoint latLonPoint, String address, String districtName, String adCode, boolean
            isSearch) {
        this.latLonPoint = latLonPoint;
        this.address = address;
        this.districtName = districtName;
        this.adCode = adCode;
        this.isSearch = isSearch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchAddressListItem)) return false;

        SearchAddressListItem that = (SearchAddressListItem) o;

        return address.equals(that.address);

    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }
}
