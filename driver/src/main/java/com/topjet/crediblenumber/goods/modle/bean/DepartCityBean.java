package com.topjet.crediblenumber.goods.modle.bean;

/**
 * Created by yy on 2017/9/2.
 * 听单 出发地实体类
 */

public class DepartCityBean {
    private String departCityId;
    private String departCityName;

    public DepartCityBean() {

    }

    public DepartCityBean(String departCityId, String departCityName) {
        this.departCityId = departCityId;
        this.departCityName = departCityName;
    }

    @Override
    public String toString() {
        return "DepartCityBean{" +
                "departCityId='" + departCityId + '\'' +
                ", departCityName='" + departCityName + '\'' +
                '}';
    }

    public String getDepartCityId() {
        return departCityId;
    }

    public void setDepartCityId(String departCityId) {
        this.departCityId = departCityId;
    }

    public String getDepartCityName() {
        return departCityName;
    }

    public void setDepartCityName(String departCityName) {
        this.departCityName = departCityName;
    }
}
