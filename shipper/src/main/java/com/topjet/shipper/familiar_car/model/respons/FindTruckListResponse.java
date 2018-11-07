package com.topjet.shipper.familiar_car.model.respons;

import com.topjet.common.common.modle.bean.TruckInfo;

import java.util.List;

/**
 * creator: yy
 * describe: 我要用车-车源列表
 */

public class FindTruckListResponse {
    private List<TruckInfo> truck_list;

    public List<TruckInfo> getList() {
        return truck_list;
    }

    @Override
    public String toString() {
        return "FindTruckListResponse{" +
                "truck_list=" + truck_list +
                '}';
    }
}
