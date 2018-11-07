package com.topjet.common.order_detail.modle.params;

/**
 * Created by yy on 2017/12/29.
 * 确认承接
 */

@SuppressWarnings("FieldCanBeLocal")
public class AccepyOrderParams extends OrderIdParams {

    private String driver_truck_id;

    public AccepyOrderParams(String order_id, String order_version, String truckId) {
        super(order_id, order_version);
        this.driver_truck_id = truckId;
    }
}
