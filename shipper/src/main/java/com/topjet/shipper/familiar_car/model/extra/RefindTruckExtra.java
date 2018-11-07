package com.topjet.shipper.familiar_car.model.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * Created by yy on 2017/9/6.
 * <p>
 * 重新找车 操作需要的字段
 */

public class RefindTruckExtra extends BaseExtra {
    private String departCityId;
    private String destinationCityId;
    private String departCityName;
    private String destinationCityName;
    private boolean isRefund;

    public void setDepartCityId(String departCityId) {
        this.departCityId = departCityId;
    }

    public void setDestinationCityId(String destinationCityId) {
        this.destinationCityId = destinationCityId;
    }

    public void setDepartCityName(String departCityName) {
        this.departCityName = departCityName;
    }

    public void setDestinationCityName(String destinationCityName) {
        this.destinationCityName = destinationCityName;
    }

    public String getDestinationCityId() {
        return destinationCityId;
    }

    public String getDepartCityName() {
        return departCityName;
    }

    public String getDestinationCityName() {
        return destinationCityName;
    }

    public String getDepartCityId() {
        return departCityId;
    }

    public boolean isRefund() {
        return isRefund;
    }

    public void setRefund(boolean refund) {
        isRefund = refund;
    }
}
