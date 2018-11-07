package com.topjet.shipper.familiar_car.model.respons;

import com.topjet.common.common.modle.bean.TruckInfo;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/10/17
 * describe: 熟车列表
 */

public class TruckListResponse {
    private List<TruckInfo> owner_truck_list;

    public List<TruckInfo> getOwner_truck_list() {
        return owner_truck_list;
    }

    public void setOwner_truck_list(List<TruckInfo> owner_truck_list) {
        this.owner_truck_list = owner_truck_list;
    }
}
