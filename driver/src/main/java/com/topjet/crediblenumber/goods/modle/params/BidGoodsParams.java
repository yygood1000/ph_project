package com.topjet.crediblenumber.goods.modle.params;

import com.topjet.common.common.modle.bean.GpsInfo;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.StringUtils;

/**
 * Created by yy on 2017/9/9.
 * <p>
 * 司机，接单接口
 */

public class BidGoodsParams {

    private String transport_fee;//报价金额
    private String deposit_fee;//	定金	是
    private String goods_id;//货源id
    private String goods_version;//货源版本

    private GpsInfo order_gps_info;// 	订单GPS信息

    public BidGoodsParams(String transport_fee, String deposit_fee, String goods_id, String goods_version) {
        this.transport_fee = transport_fee;
        this.deposit_fee = StringUtils.isNotBlank(deposit_fee) ? deposit_fee : 0 + "";
        this.goods_id = goods_id;
        this.goods_version = goods_version;

        if (CMemoryData.getLocationInfo() != null) {
            order_gps_info = new GpsInfo();
            this.order_gps_info.setGps_address_city_id(CMemoryData.getLocationInfo().getCity_id());
            this.order_gps_info.setGps_detail(CMemoryData.getLocationInfo().getDetail());
            this.order_gps_info.setGps_latitude(CMemoryData.getLocationInfo().getLatitude() + "");
            this.order_gps_info.setGps_longitude(CMemoryData.getLocationInfo().getLongitude() + "");
            this.order_gps_info.setGps_remark("接单");
        }
    }

    @Override
    public String toString() {
        return "BidGoodsParams{" +
                "transport_fee='" + transport_fee + '\'' +
                ", deposit_fee='" + deposit_fee + '\'' +
                ", goods_id='" + goods_id + '\'' +
                ", goods_version='" + goods_version + '\'' +
                ", order_gps_info=" + order_gps_info +
                '}';
    }
}
