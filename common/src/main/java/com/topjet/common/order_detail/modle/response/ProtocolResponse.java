package com.topjet.common.order_detail.modle.response;

/**
 * Created by yy on 2017/9/25.
 * <p>
 * 交易协议页面 查看的信息
 */

public class ProtocolResponse {

    private String contract_no;//合同编号 
    private String take_effect_time;//合同创建时间 
    private String sender_name;//发货人姓名 
    private String sender_mobile;//发货人手机
    private String depart_city; // 发货地
    private String depart_city_id;//发货地城市id	 
    private String depart_detail;//详细地址 
    private String receiver_name;//收货人姓名 
    private String receiver_mobile;//收货人手机号
    private String destination_city; // 收货地
    private String destination_city_id;//收货人城市id	 
    private String destination_detail;//收货人详细地址 
    private String truck_type;//接单车型id	是	list
    private String truck_length;//接单车长id	是	list
    private String quantity_max;//货物数量最大值	 
    private String quantity_min;//货物数量最小值 
    private String unit;//	数量单位	 	吨/方/个/件/箱/台/车
    private String freight_fee;//运费总金额	 
    private String ahead_fee;//	提付费金额	 
    private String delivery_fee;//	到付费金额 
    private String back_fee;//回单付金额	 
    private String owner_name;//货主姓名 
    private String owner_id_card;//货主身份证号	 
    private String driver_name;//司机姓名 
    private String driver_mobile;//司机手机号码	 
    private String driver_id_card;//司机身份证号 
    private String driver_license_plate_number;//	车牌号	 

    public void setTruck_type(String truck_type) {
        this.truck_type = truck_type;
    }

    public void setTruck_length(String truck_length) {
        this.truck_length = truck_length;
    }

    public void setFreight_fee(String freight_fee) {
        this.freight_fee = freight_fee;
    }

    public void setAhead_fee(String ahead_fee) {
        this.ahead_fee = ahead_fee;
    }

    public void setDelivery_fee(String delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public void setBack_fee(String back_fee) {
        this.back_fee = back_fee;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public void setDriver_mobile(String driver_mobile) {
        this.driver_mobile = driver_mobile;
    }

    public void setDriver_id_card(String driver_id_card) {
        this.driver_id_card = driver_id_card;
    }

    public void setDriver_license_plate_number(String driver_license_plate_number) {
        this.driver_license_plate_number = driver_license_plate_number;
    }



    @Override
    public String toString() {
        return "ProtocolResponse{" +
                "contract_no='" + contract_no + '\'' +
                ", take_effect_time='" + take_effect_time + '\'' +
                ", sender_name='" + sender_name + '\'' +
                ", sender_mobile='" + sender_mobile + '\'' +
                ", depart_city_id='" + depart_city_id + '\'' +
                ", depart_detail='" + depart_detail + '\'' +
                ", receiver_name='" + receiver_name + '\'' +
                ", receiver_mobile='" + receiver_mobile + '\'' +
                ", destination_city_id='" + destination_city_id + '\'' +
                ", destination_detail='" + destination_detail + '\'' +
                ", truck_type='" + truck_type + '\'' +
                ", truck_length='" + truck_length + '\'' +
                ", quantity_max='" + quantity_max + '\'' +
                ", quantity_min='" + quantity_min + '\'' +
                ", unit='" + unit + '\'' +
                ", freight_fee='" + freight_fee + '\'' +
                ", ahead_fee='" + ahead_fee + '\'' +
                ", delivery_fee='" + delivery_fee + '\'' +
                ", back_fee='" + back_fee + '\'' +
                ", owner_name='" + owner_name + '\'' +
                ", owner_id_card='" + owner_id_card + '\'' +
                ", driver_name='" + driver_name + '\'' +
                ", driver_mobile='" + driver_mobile + '\'' +
                ", driver_id_card='" + driver_id_card + '\'' +
                ", driver_license_plate_number='" + driver_license_plate_number + '\'' +
                '}';
    }
}
