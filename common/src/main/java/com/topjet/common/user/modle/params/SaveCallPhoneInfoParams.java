package com.topjet.common.user.modle.params;

/**
 * Created by tsj-004 on 2017/9/28.
 */

public class SaveCallPhoneInfoParams {
    private String call_type = null;// 呼叫类型
    private String call_user_mobile = null;// 呼叫人的号码
    private String called_user_mobile = null;// 被呼叫人的号码
    private String call_time = null;// 通话时长
    private String goods_id = null;// 货源Id，如果呼叫的不是客服则必填
    private String goods_status = null;// 拨打时货源的状态，如果呼叫的不是客服则必填
    private String longitude = null;// 经度
    private String latitude = null;// 纬度
    private String gps_remark = null;// GPS备注
    private String gps_detail = null;// 详细位置
    private String gps_city_id = null;// 城市代码

    public String getCall_type() {
        return call_type;
    }

    public void setCall_type(String call_type) {
        this.call_type = call_type;
    }

    public String getCall_user_mobile() {
        return call_user_mobile;
    }

    public void setCall_user_mobile(String call_user_mobile) {
        this.call_user_mobile = call_user_mobile;
    }

    public String getCalled_user_mobile() {
        return called_user_mobile;
    }

    public void setCalled_user_mobile(String called_user_mobile) {
        this.called_user_mobile = called_user_mobile;
    }

    public String getCall_time() {
        return call_time;
    }

    public void setCall_time(String call_time) {
        this.call_time = call_time;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_status() {
        return goods_status;
    }

    public void setGoods_status(String goods_status) {
        this.goods_status = goods_status;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getGps_remark() {
        return gps_remark;
    }

    public void setGps_remark(String gps_remark) {
        this.gps_remark = gps_remark;
    }

    public String getGps_detail() {
        return gps_detail;
    }

    public void setGps_detail(String gps_detail) {
        this.gps_detail = gps_detail;
    }

    public String getGps_city_id() {
        return gps_city_id;
    }

    public void setGps_city_id(String gps_city_id) {
        this.gps_city_id = gps_city_id;
    }
}
