package com.topjet.shipper.deliver.modle.params;

import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.common.modle.bean.GpsInfo;

import java.util.List;

/**
 * Created by tsj-004 on 2017/9/6.
 * 货主-发布货源请求实体类
 */

public class OwnerGoodsParams extends BaseExtra {
    // 新发订单
    public final static int IN_TYPE_ADD = 0;
    // 复制订单
    public final static int IN_TYPE_COPY = 100;
    // 修改订单
    public final static int IN_TYPE_EDIT = 200;
    // 新发定向订单
    public final static int IN_TYPE_ADD_ASSIGNED = 300;
    // 定向订单-重新找车
    public final static int IN_TYPE_ADD_ASSIGNED_REFIND_TRUCK = 310;
    // 修改定向订单
    public final static int IN_TYPE_EDIT_ASSIGNED = 400;
    // 进入页面的类型，是复制还是修改还是新发
    private int inType = IN_TYPE_ADD;


    private String goods_id = null;     // 货源id
    private String goods_version = null;// 货源version
    private String sender_name = null;//发货人姓名
    private String sender_mobile = null;//发货人手机
    private String depart_city_id = null;//发货地城市id
    private String depart_detail = null;//详细地址
    private String depart_lng = null;//发货人定位经度
    private String depart_lat = null;//发货人定位纬度
    private String receiver_name = null;//收货人姓名
    private String receiver_mobile = null;//收货人手机号
    private String destination_city_id = null;//收货人城市id
    private String destination_detail = null;//收货人详细地址
    private String destination_lng = null;//收货人定位经度
    private String destination_lat = null;//收货人定位纬度
    private String distance = null;//总里程
    private String pay_style = null;//支付方式1:货到付款2:提付全款3:提付部分运费4:回单付运费
    private String is_carpool = null;//可拼车0:不可拼车/整车1:可拼车
    private String load_date_type = null;//提货时间类型
    private String load_date = null;//提货时间0:具体时间1:今定今装2:今定明装3:随到随走
    private String category = null;//货物类型
    private String pack_type = null;//卸货方式
    private String load_type = null;//提货方式
    private String quantity_type = null;//货物数量类型1固定2:范围
    private String quantity_max = null;//货物数量最大值
    private String quantity_min = null;//货物数量最小值
    private String unit = null;//数量单位吨/方/个/件/箱/台/车
    private List<String> truck_type_ids = null;//车型ids
    private List<String> truck_length_ids = null;//车长ids
    private List<String> truck_type;//接单车型
    private List<String> truck_length;//接单车长
    private String flush_num = null;//刷新次数
    private String text_remark = null;//文字备注
    private String photo_remark_key = null;    //图片备注key
    private String photo_remark = null;    //图片备注url
    private String freight_fee_managed = null;//费用托管  0 : 否 1 :是

    private String driver_id = null;//司机id
    private String driver_truck_id = null;//车辆id
    private String ahead_fee = null;//提付费
    private String ahead_fee_is_managed = null;//提付费是否托管
    private String delivery_fee = null;//到付费
    private String delivery_fee_is_managed = null;//到付费是否托管
    private String back_fee = null;//回单付费用
    private String freight_fee = null;//运费
    private GpsInfo order_gps_info = null;  // 订单gps信息

    public OwnerGoodsParams(String goods_id) {
        this.goods_id = goods_id;
    }

    public OwnerGoodsParams() {
    }

    public String getGoods_version() {
        return goods_version;
    }

    public void setGoods_version(String goods_version) {
        this.goods_version = goods_version;
    }

    public int getInType() {
        return inType;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public void setInType(int inType) {
        this.inType = inType;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_mobile() {
        return sender_mobile;
    }

    public void setSender_mobile(String sender_mobile) {
        this.sender_mobile = sender_mobile;
    }

    public String getDepart_city_id() {
        return depart_city_id;
    }

    public void setDepart_city_id(String depart_city_id) {
        this.depart_city_id = depart_city_id;
    }

    public String getDepart_detail() {
        return depart_detail;
    }

    public void setDepart_detail(String depart_detail) {
        this.depart_detail = depart_detail;
    }

    public String getDepart_lng() {
        return depart_lng;
    }

    public void setDepart_lng(String depart_lng) {
        this.depart_lng = depart_lng;
    }

    public String getDepart_lat() {
        return depart_lat;
    }

    public void setDepart_lat(String depart_lat) {
        this.depart_lat = depart_lat;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_mobile() {
        return receiver_mobile;
    }

    public void setReceiver_mobile(String receiver_mobile) {
        this.receiver_mobile = receiver_mobile;
    }

    public String getDestination_city_id() {
        return destination_city_id;
    }

    public void setDestination_city_id(String destination_city_id) {
        this.destination_city_id = destination_city_id;
    }

    public String getDestination_detail() {
        return destination_detail;
    }

    public void setDestination_detail(String destination_detail) {
        this.destination_detail = destination_detail;
    }

    public String getDestination_lng() {
        return destination_lng;
    }

    public void setDestination_lng(String destination_lng) {
        this.destination_lng = destination_lng;
    }

    public String getDestination_lat() {
        return destination_lat;
    }

    public void setDestination_lat(String destination_lat) {
        this.destination_lat = destination_lat;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPay_style() {
        return pay_style;
    }

    public void setPay_style(String pay_style) {
        this.pay_style = pay_style;
    }

    public String getIs_carpool() {
        return is_carpool;
    }

    public void setIs_carpool(String is_carpool) {
        this.is_carpool = is_carpool;
    }

    public String getLoad_date_type() {
        return load_date_type;
    }

    public void setLoad_date_type(String load_date_type) {
        this.load_date_type = load_date_type;
    }

    public String getLoad_date() {
        return load_date;
    }

    public void setLoad_date(String load_date) {
        this.load_date = load_date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPack_type() {
        return pack_type;
    }

    public void setPack_type(String pack_type) {
        this.pack_type = pack_type;
    }

    public String getLoad_type() {
        return load_type;
    }

    public void setLoad_type(String load_type) {
        this.load_type = load_type;
    }

    public String getQuantity_type() {
        return quantity_type;
    }

    public void setQuantity_type(String quantity_type) {
        this.quantity_type = quantity_type;
    }

    public String getQuantity_max() {
        return quantity_max;
    }

    public void setQuantity_max(String quantity_max) {
        this.quantity_max = quantity_max;
    }

    public String getQuantity_min() {
        return quantity_min;
    }

    public void setQuantity_min(String quantity_min) {
        this.quantity_min = quantity_min;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<String> getTruck_type_ids() {
        return truck_type_ids;
    }

    public void setTruck_type_ids(List<String> truck_type_ids) {
        this.truck_type_ids = truck_type_ids;
    }

    public List<String> getTruck_length_ids() {
        return truck_length_ids;
    }

    public void setTruck_length_ids(List<String> truck_length_ids) {
        this.truck_length_ids = truck_length_ids;
    }

    public String getFlush_num() {
        return flush_num;
    }

    public void setFlush_num(String flush_num) {
        this.flush_num = flush_num;
    }

    public String getText_remark() {
        return text_remark;
    }

    public void setText_remark(String text_remark) {
        this.text_remark = text_remark;
    }

    public String getPhoto_remark_key() {
        return photo_remark_key;
    }

    public void setPhoto_remark_key(String photo_remark_key) {
        this.photo_remark_key = photo_remark_key;
    }

    public String getPhoto_remark() {
        return photo_remark;
    }

    public void setPhoto_remark(String photo_remark) {
        this.photo_remark = photo_remark;
    }

    public String getFreight_fee_managed() {
        return freight_fee_managed;
    }

    public void setFreight_fee_managed(String freight_fee_managed) {
        this.freight_fee_managed = freight_fee_managed;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_truck_id() {
        return driver_truck_id;
    }

    public void setDriver_truck_id(String driver_truck_id) {
        this.driver_truck_id = driver_truck_id;
    }

    public String getAhead_fee() {
        return ahead_fee;
    }

    public void setAhead_fee(String ahead_fee) {
        this.ahead_fee = ahead_fee;
    }

    public String getAhead_fee_is_managed() {
        return ahead_fee_is_managed;
    }

    public void setAhead_fee_is_managed(String ahead_fee_is_managed) {
        this.ahead_fee_is_managed = ahead_fee_is_managed;
    }

    public String getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(String delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public String getDelivery_fee_is_managed() {
        return delivery_fee_is_managed;
    }

    public void setDelivery_fee_is_managed(String delivery_fee_is_managed) {
        this.delivery_fee_is_managed = delivery_fee_is_managed;
    }

    public String getBack_fee() {
        return back_fee;
    }

    public void setBack_fee(String back_fee) {
        this.back_fee = back_fee;
    }

    public String getFreight_fee() {
        return freight_fee;
    }

    public void setFreight_fee(String freight_fee) {
        this.freight_fee = freight_fee;
    }

    public GpsInfo getOrder_gps_info() {
        return order_gps_info;
    }

    public void setOrder_gps_info(GpsInfo order_gps_info) {
        this.order_gps_info = order_gps_info;
    }

    @Override
    public String toString() {
        return "OwnerGoodsParams{" +
                "goods_id='" + goods_id + '\'' +
                ", sender_name='" + sender_name + '\'' +
                ", sender_mobile='" + sender_mobile + '\'' +
                ", depart_city_id='" + depart_city_id + '\'' +
                ", depart_detail='" + depart_detail + '\'' +
                ", depart_lng='" + depart_lng + '\'' +
                ", depart_lat='" + depart_lat + '\'' +
                ", receiver_name='" + receiver_name + '\'' +
                ", receiver_mobile='" + receiver_mobile + '\'' +
                ", destination_city_id='" + destination_city_id + '\'' +
                ", destination_detail='" + destination_detail + '\'' +
                ", destination_lng='" + destination_lng + '\'' +
                ", destination_lat='" + destination_lat + '\'' +
                ", distance='" + distance + '\'' +
                ", pay_style='" + pay_style + '\'' +
                ", is_carpool='" + is_carpool + '\'' +
                ", load_date_type='" + load_date_type + '\'' +
                ", load_date='" + load_date + '\'' +
                ", category='" + category + '\'' +
                ", pack_type='" + pack_type + '\'' +
                ", load_type='" + load_type + '\'' +
                ", quantity_type='" + quantity_type + '\'' +
                ", quantity_max='" + quantity_max + '\'' +
                ", quantity_min='" + quantity_min + '\'' +
                ", unit='" + unit + '\'' +
                ", truck_type_ids=" + truck_type_ids +
                ", truck_length_ids=" + truck_length_ids +
                ", truck_type=" + truck_type +
                ", truck_length=" + truck_length +
                ", flush_num='" + flush_num + '\'' +
                ", text_remark='" + text_remark + '\'' +
                ", photo_remark_key='" + photo_remark_key + '\'' +
                ", photo_remark='" + photo_remark + '\'' +
                ", freight_fee_managed='" + freight_fee_managed + '\'' +
                ", driver_id='" + driver_id + '\'' +
                ", driver_truck_id='" + driver_truck_id + '\'' +
                ", ahead_fee='" + ahead_fee + '\'' +
                ", ahead_fee_is_managed='" + ahead_fee_is_managed + '\'' +
                ", delivery_fee='" + delivery_fee + '\'' +
                ", delivery_fee_is_managed='" + delivery_fee_is_managed + '\'' +
                ", back_fee='" + back_fee + '\'' +
                ", freight_fee='" + freight_fee + '\'' +
                ", order_gps_info=" + order_gps_info +
                ", inType=" + inType +
                '}';
    }
}
