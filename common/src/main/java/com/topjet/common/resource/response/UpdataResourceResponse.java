package com.topjet.common.resource.response;

/**
 * Created by yy on 2017/12/8.
 * <p>
 * 资源文件更新 新资源返回体
 */

public class UpdataResourceResponse {
    private String inner_version;// 新的资源版本号

    private String city_list;//城市json
    private String type_list;//车型json
    private String length_list;//车长json
    private String list;// 包装方式/装卸方式/货物类型/货物单位json

    public String getInner_version() {
        return inner_version;
    }

    public String getCity_list() {
        return city_list;
    }

    public String getType_list() {
        return type_list;
    }

    public String getLength_list() {
        return length_list;
    }

    public String getList() {
        return list;
    }

    @Override
    public String toString() {
        return "UpdataResourceResponse{" +
                "inner_version='" + inner_version + '\'' +
                ", city_list='" + city_list + '\'' +
                ", type_list='" + type_list + '\'' +
                ", length_list='" + length_list + '\'' +
                ", list='" + list + '\'' +
                '}';
    }
}
