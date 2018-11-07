package com.topjet.shipper.familiar_car.model.params;

import com.topjet.common.utils.NumberFormatUtils;

/**
 * Created by yy on 2017/8/9.
 * 附近车源 地图页 请求参数
 */

public class AroundTruckMapParams {
    private String page;
    private String longitude;//经度
    private String latitude;//维度
    private String city_id;//城市id	否	string
    private String truck_type_id;//车型	否	string
    private String truck_length_id;//车长	否	string
    private String destination_city_id;//目的地城市id
    private String map_level;//地图级别

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

    public void setDestination_city_id(String destination_city_id) {
        this.destination_city_id = destination_city_id;
    }

    public void setMap_level(String map_level) {
        this.map_level = NumberFormatUtils.removeDecimal(map_level);
    }

    public void setPage(String page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "AroundTruckMapParams{" +
                "page='" + page + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", city_id='" + city_id + '\'' +
                ", truck_type_id='" + truck_type_id + '\'' +
                ", truck_length_id='" + truck_length_id + '\'' +
                ", destination_city_id='" + destination_city_id + '\'' +
                ", map_level='" + map_level + '\'' +
                '}';
    }
}