package com.topjet.crediblenumber.goods.modle.params;

import com.topjet.common.utils.NumberFormatUtils;

import java.util.ArrayList;

/**
 * Created by yy on 2017/8/9.
 * 附近货源列表页 货源列表 请求参数
 */

public class AroundGoodsListParams {

    private String page;//	页数	 	初始为1
    private String longitude;//经度
    private String latitude;//	维度	 
    private String city_id;//城市id	 
    private String truck_type_id;//	车型	 
    private String truck_length_id;//车长	 
    private ArrayList<String> destination_city_ids;//	目的地城市id[数组]
    private String map_level;//地图级别

    @Override
    public String toString() {
        return "AroundGoodsListParams{" +
                "page='" + page + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", city_id='" + city_id + '\'' +
                ", truck_type_id='" + truck_type_id + '\'' +
                ", truck_length_id='" + truck_length_id + '\'' +
                ", destination_city_ids='" + destination_city_ids + '\'' +
                ", map_level='" + map_level + '\'' +
                '}';
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public void setTruck_type_id(String truck_type_id) {
        this.truck_type_id = truck_type_id;
    }

    public void setTruck_length_id(String truck_length_id) {
        this.truck_length_id = truck_length_id;
    }

    public void setDestination_city_ids(ArrayList<String> destination_city_ids) {
        this.destination_city_ids = destination_city_ids;
    }

    public void setMap_level(String map_level) {
        this.map_level = NumberFormatUtils.removeDecimal(map_level);
    }
}