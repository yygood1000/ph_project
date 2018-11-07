package com.topjet.common.common.modle.bean;

/**
 * 常跑城市
 * 因为需要在内存中保存常跑城市列表，所以该实体类写在common中。本该是司机的实体类
 * Created by yy on 2017/8/16
 */
public class UsualCityBean {

    public UsualCityBean(String cityCode, String cityName) {
        this.business_line_city_id = cityCode;
        this.business_line_city = cityName;
    }

    public UsualCityBean() {
    }

    public String getBusinessLineCityName() {
        return business_line_city;
    }


    private String business_line_city_id;       // 常跑城市城市ID
    private String business_line_city;          // 常跑城市名
    private String business_line_id;            // 数据表的id
    private String driver_id;                   // 司机id


    public String getBussinessLineId() {
        return business_line_id;
    }

    public String getDriverId() {
        return driver_id;
    }

    public String getBusinessLineCitycode() {
        return business_line_city_id;
    }

    public void setBusiness_line_city_id(String business_line_city_id) {
        this.business_line_city_id = business_line_city_id;
    }

    public void setBusiness_line_id(String business_line_id) {
        this.business_line_id = business_line_id;
    }

    public void setBusiness_line_city(String business_line_city) {
        this.business_line_city = business_line_city;
    }

    @Override
    public String toString() {
        return "UsualCityBean{" +
                "business_line_city_id='" + business_line_city_id + '\'' +
                ", business_line_city='" + business_line_city + '\'' +
                ", business_line_id='" + business_line_id + '\'' +
                ", driver_id='" + driver_id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsualCityBean)) return false;

        UsualCityBean that = (UsualCityBean) o;

        return business_line_id.equals(that.business_line_id);

    }
}
