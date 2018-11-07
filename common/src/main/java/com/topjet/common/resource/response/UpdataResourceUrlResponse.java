package com.topjet.common.resource.response;

/**
 * Created by tsj028 on 2018/3/14 0014.
 */

public class UpdataResourceUrlResponse  {
    private String inner_version;// 新的资源版本号
    private String city_url;   // 城市文件下载地址
    private String type_url;   //车型json下载地址
    private String length_url;   // 车长 url
    private String url;   // 包装方式、卸载方式、货物类型、货物单位url

    public String getInner_version() {
        return inner_version;
    }

    public void setInner_version(String inner_version) {
        this.inner_version = inner_version;
    }

    public String getCity_url() {
        return city_url;
    }

    public void setCity_url(String city_url) {
        this.city_url = city_url;
    }

    public String getType_url() {
        return type_url;
    }

    public void setType_url(String type_url) {
        this.type_url = type_url;
    }

    public String getLength_url() {
        return length_url;
    }

    public void setLength_url(String length_url) {
        this.length_url = length_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UpdataResourceUrlResponse{" +
                "inner_version='" + inner_version + '\'' +
                ", city_url='" + city_url + '\'' +
                ", type_url='" + type_url + '\'' +
                ", length_url='" + length_url + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
