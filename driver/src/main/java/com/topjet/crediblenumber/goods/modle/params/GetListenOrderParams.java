package com.topjet.crediblenumber.goods.modle.params;

import java.util.Set;

/**
 * Created by tsj-004 on 2017/9/12.
 */

public class GetListenOrderParams {
    private String start_time;
    private String start_city_id;
    private Set<String> destination_city_ids;

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStart_city_id() {
        return start_city_id;
    }

    public void setStart_city_id(String start_city_id) {
        this.start_city_id = start_city_id;
    }

    public Set<String> getDestination_city_ids() {
        return destination_city_ids;
    }

    public void setDestination_city_ids(Set<String> destination_city_ids) {
        this.destination_city_ids = destination_city_ids;
    }
}
