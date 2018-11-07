package com.topjet.shipper.user.model.bean;

/**
 * 添加或编辑常用联系人时提交的参数类
 * Created by tsj004 on 2017/8/28.
 */

public class ContactsInputParams {
    private String linkman_id;
    private String contacts_name_user_id;
    private String contacts_mobile;
    private String contacts_name;
    private String contacts_city_code;
    private String contacts_address;
    private String longitude;
    private String latitude;

    public String getLinkman_id() {
        return linkman_id;
    }

    public void setLinkman_id(String linkman_id) {
        this.linkman_id = linkman_id;
    }

    public String getContacts_name_user_id() {
        return contacts_name_user_id;
    }

    public void setContacts_name_user_id(String contacts_name_user_id) {
        this.contacts_name_user_id = contacts_name_user_id;
    }

    public String getContacts_mobile() {
        return contacts_mobile;
    }

    public void setContacts_mobile(String contacts_mobile) {
        this.contacts_mobile = contacts_mobile;
    }

    public String getContacts_name() {
        return contacts_name;
    }

    public void setContacts_name(String contacts_name) {
        this.contacts_name = contacts_name;
    }

    public String getContacts_city_code() {
        return contacts_city_code;
    }

    public void setContacts_city_code(String contacts_city_code) {
        this.contacts_city_code = contacts_city_code;
    }

    public String getContacts_address() {
        return contacts_address;
    }

    public void setContacts_address(String contacts_address) {
        this.contacts_address = contacts_address;
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
}
