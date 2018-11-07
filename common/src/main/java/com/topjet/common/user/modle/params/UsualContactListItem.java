package com.topjet.common.user.modle.params;

import com.topjet.common.base.model.BaseEvent;
import com.topjet.common.resource.bean.CityItem;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.utils.StringUtils;

/**
 * 常用联系人列表数据 实体类
 * Created by tsj004 on 2017/8/24.
 */

public class UsualContactListItem extends BaseEvent{
    private String contacts_name_user_id;
    private String linkman_id;
    private String icon_url;
    private String icon_key;
    private String contacts_mobile;
    private String contacts_name;
    private String contacts_city;
    private String contacts_city_code;
    private String contacts_address;
    private String longitude;
    private String latitude;
    private boolean isCheck = false;

    public String getContacts_city() {
//        if(StringUtils.isNotBlank(contacts_city_code)){
//            CityItem item = AreaDataDict.getCityItemByAdcode(contacts_city_code);
//            if(item != null) {
//                contacts_city = item.getCityFullName();
//            }
//        }
        return contacts_city;
    }

    public void setContacts_city(String contacts_city) {
        this.contacts_city = contacts_city;
    }

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

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getIcon_key() {
        return icon_key;
    }

    public void setIcon_key(String icon_key) {
        this.icon_key = icon_key;
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

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public String toString() {
        return "UsualContactListItem{" +
                "contacts_name_user_id='" + contacts_name_user_id + '\'' +
                ", linkman_id='" + linkman_id + '\'' +
                ", icon_url='" + icon_url + '\'' +
                ", icon_key='" + icon_key + '\'' +
                ", contacts_mobile='" + contacts_mobile + '\'' +
                ", contacts_name='" + contacts_name + '\'' +
                ", contacts_city_code='" + contacts_city_code + '\'' +
                ", contacts_address='" + contacts_address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }
}
