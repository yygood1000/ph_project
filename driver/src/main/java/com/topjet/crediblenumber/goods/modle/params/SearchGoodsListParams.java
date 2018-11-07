package com.topjet.crediblenumber.goods.modle.params;

/**
 * Created by yy on 2017/8/9.
 * 智能找货 请求实体
 */

public class SearchGoodsListParams {

    private String page;//	页数	 	初始为1
    private String truck_type_id;//车型ID
    private String truck_length_id;//	车长ID
    private String destination_city_code;//	目的地ID
    private String start_city_id;//	出发地ID

    public SearchGoodsListParams() {
    }

    public SearchGoodsListParams(String page, String truck_type_id, String truck_length_id, String
            destination_city_code, String start_city_id) {
        this.page = page;
        this.truck_type_id = truck_type_id;
        this.truck_length_id = truck_length_id;
        this.destination_city_code = destination_city_code;
        this.start_city_id = start_city_id;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setTruck_type_id(String truck_type_id) {
        this.truck_type_id = truck_type_id;
    }

    public void setTruck_length_id(String truck_length_id) {
        this.truck_length_id = truck_length_id;
    }

    public void setDestination_city_code(String destination_city_code) {
        this.destination_city_code = destination_city_code;
    }

    public void setStart_city_id(String start_city_id) {
        this.start_city_id = start_city_id;
    }

    @Override
    public String toString() {
        return "SearchGoodsListParams{" +
                "page='" + page + '\'' +
                ", truck_type_id='" + truck_type_id + '\'' +
                ", truck_length_id='" + truck_length_id + '\'' +
                ", destination_city_code='" + destination_city_code + '\'' +
                ", start_city_id='" + start_city_id + '\'' +
                '}';
    }
}