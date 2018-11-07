package com.topjet.shipper.familiar_car.model.params;

/**
 * creator: yy
 * 我要用车，车源列表
 */

public class FindTruckParams {
    // 分页
    private String page;

    private String start_city_id;//出发地城市id	是
    private String destination_city_id;//目的地城市id	否
    private String truck_type_id;//车型id	否
    private String truck_length_id;//	车长id	否

    public void setPage(String page) {
        this.page = page;
    }

    public void setStart_city_id(String start_city_id) {
        this.start_city_id = start_city_id;
    }

    public void setDestination_city_id(String destination_city_id) {
        this.destination_city_id = destination_city_id;
    }

    public void setTruck_type_id(String truck_type_id) {
        this.truck_type_id = truck_type_id;
    }

    public void setTruck_length_id(String truck_length_id) {
        this.truck_length_id = truck_length_id;
    }

    @Override
    public String toString() {
        return "FindTruckParams{" +
                "page='" + page + '\'' +
                ", start_city_id='" + start_city_id + '\'' +
                ", destination_city_id='" + destination_city_id + '\'' +
                ", truck_type_id='" + truck_type_id + '\'' +
                ", truck_length_id='" + truck_length_id + '\'' +
                '}';
    }
}
