package com.topjet.crediblenumber.goods.modle.response;

/**
 * Created by tsj-004 on 2017/9/8.
 */

public class ListenSettingObject {
    public final static String TYPE_CHUFA = "1";        // 出发
    public final static String TYPE_CHANGPAO = "2";     // 常跑
    public final static String TYPE_ZIXUAN = "3";       // 自选

    public String type;         // 听单设置类型 1: 出发地 2 常跑路线--目的地 3: 自选路线--目的地
    public String city_id;      //城市id
    public String city_name;    //城市名称

    public ListenSettingObject() {

    }

    public ListenSettingObject(String type, String city_id, String city_name) {
        this.type = type;
        this.city_id = city_id;
        this.city_name = city_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
