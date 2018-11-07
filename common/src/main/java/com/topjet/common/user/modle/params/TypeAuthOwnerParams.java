package com.topjet.common.user.modle.params;

/**
 * Created by tsj-004 on 2017/8/9.
 * 货主身份认证请求参数
 */

public class TypeAuthOwnerParams {
    private String shipper_business_img = null;//营业执照
    private String shipper_business_img_url = null;//营业执照url
    private String shipper_title_img = null;//门头照
    private String shipper_card_img = null;//名片
    private String shipperi_certificate_img = null;//机打发货单
    private String detailed_address = null;//详细地址
    private String longitude = null;//经度
    private String latitude = null;//维度
    private String registered_address_code = null;//注册地城市编码
    private String version = null;//数据版本
    private String shipper_business_img_key = null;//营业执照key
    private String shipper_title_img_key = null;//门头照key
    private String shipper_title_img_url = null;//门头照url
    private String shipper_card_img_key = null;//名片key
    private String shipper_card_img_url = null;//名片url
    private String shipperi_certificate_img_key = null;//机打发货单key
    private String shipperi_certificate_img_url = null;//机打发货单url
    private String registered_address_id = null;//注册地城市id
    private String user_auth_status = null;//审核状态 0、未认证 1、认证中 2、认证失败 3、认证通过
    private String user_auth_remark = null;//审核备注

    public String getShipper_business_img() {
        return shipper_business_img;
    }

    public void setShipper_business_img(String shipper_business_img) {
        this.shipper_business_img = shipper_business_img;
    }

    public String getShipper_title_img_key() {
        return shipper_title_img_key;
    }

    public void setShipper_title_img_key(String shipper_title_img_key) {
        this.shipper_title_img_key = shipper_title_img_key;
    }

    public String getShipper_title_img() {
        return shipper_title_img;
    }

    public void setShipper_title_img(String shipper_title_img) {
        this.shipper_title_img = shipper_title_img;
    }

    public String getShipper_business_img_key() {
        return shipper_business_img_key;
    }

    public void setShipper_business_img_key(String shipper_business_img_key) {
        this.shipper_business_img_key = shipper_business_img_key;
    }

    public String getShipper_card_img() {
        return shipper_card_img;
    }

    public void setShipper_card_img(String shipper_card_img) {
        this.shipper_card_img = shipper_card_img;
    }

    public String getShipper_card_img_key() {
        return shipper_card_img_key;
    }

    public void setShipper_card_img_key(String shipper_card_img_key) {
        this.shipper_card_img_key = shipper_card_img_key;
    }

    public String getShipperi_certificate_img() {
        return shipperi_certificate_img;
    }

    public void setShipperi_certificate_img(String shipperi_certificate_img) {
        this.shipperi_certificate_img = shipperi_certificate_img;
    }

    public String getShipperi_certificate_img_key() {
        return shipperi_certificate_img_key;
    }

    public void setShipperi_certificate_img_key(String shipperi_certificate_img_key) {
        this.shipperi_certificate_img_key = shipperi_certificate_img_key;
    }

    public String getDetailed_address() {
        return detailed_address;
    }

    public void setDetailed_address(String detailed_address) {
        this.detailed_address = detailed_address;
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

    public String getRegistered_address_code() {
        return registered_address_code;
    }

    public void setRegistered_address_code(String registered_address_code) {
        this.registered_address_code = registered_address_code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getShipper_business_img_url() {
        return shipper_business_img_url;
    }

    public void setShipper_business_img_url(String shipper_business_img_url) {
        this.shipper_business_img_url = shipper_business_img_url;
    }

    public String getShipper_title_img_url() {
        return shipper_title_img_url;
    }

    public void setShipper_title_img_url(String shipper_title_img_url) {
        this.shipper_title_img_url = shipper_title_img_url;
    }

    public String getShipper_card_img_url() {
        return shipper_card_img_url;
    }

    public void setShipper_card_img_url(String shipper_card_img_url) {
        this.shipper_card_img_url = shipper_card_img_url;
    }

    public String getShipperi_certificate_img_url() {
        return shipperi_certificate_img_url;
    }

    public void setShipperi_certificate_img_url(String shipperi_certificate_img_url) {
        this.shipperi_certificate_img_url = shipperi_certificate_img_url;
    }

    public String getRegistered_address_id() {
        return registered_address_id;
    }

    public void setRegistered_address_id(String registered_address_id) {
        this.registered_address_id = registered_address_id;
    }

    public String getUser_auth_status() {
        return user_auth_status;
    }

    public void setUser_auth_status(String user_auth_status) {
        this.user_auth_status = user_auth_status;
    }

    public String getUser_auth_remark() {
        return user_auth_remark;
    }

    public void setUser_auth_remark(String user_auth_remark) {
        this.user_auth_remark = user_auth_remark;
    }
}