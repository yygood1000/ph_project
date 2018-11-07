package com.topjet.crediblenumber.goods.modle.params;

/**
 * Created by tsj-004 on 2017/8/9.
 * 添加订阅路线
 */

public class AddSubscribeRouteParams {
    private String depart_city_code;//出发地城市ID	是	String
    private String destination_city_code;//目的地城市ID	是	String
    private String truck_type_id;//车型ID	是	String
    private String truck_length_id;//车长ID	是	String

    public AddSubscribeRouteParams() {
    }

    public String getDepart_city_code() {
        return depart_city_code;
    }

    public void setDepart_city_code(String depart_city_code) {
        this.depart_city_code = depart_city_code;
    }

    public String getDestination_city_code() {
        return destination_city_code;
    }

    public void setDestination_city_code(String destination_city_code) {
        this.destination_city_code = destination_city_code;
    }

    public String getTruck_type_id() {
        return truck_type_id;
    }

    public void setTruck_type_id(String truck_type_id) {
        this.truck_type_id = truck_type_id;
    }

    public String getTruck_length_id() {
        return truck_length_id;
    }

    public void setTruck_length_id(String truck_length_id) {
        this.truck_length_id = truck_length_id;
    }

    @Override
    public String toString() {
        return "AddSubscribeRouteParams{" +
                "depart_city_code='" + depart_city_code + '\'' +
                ", destination_city_code='" + destination_city_code + '\'' +
                ", truck_type_id='" + truck_type_id + '\'' +
                ", truck_length_id='" + truck_length_id + '\'' +
                '}';
    }
}