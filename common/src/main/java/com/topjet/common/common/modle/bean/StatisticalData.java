package com.topjet.common.common.modle.bean;

import com.topjet.common.config.CMemoryData;

/**
 * Created by yy on 2017/8/28.
 * <p>
 * 货源/车源统计 实体类（省市级 点聚合使用数据）
 */

public class StatisticalData {

    private String city_id;//		城市ID	string
    private String city_name;//	城市名	string
    private String count;//		货源总数	string
    private String longitude;//	经度	string
    private String latitude;//	纬度	string

    @Override
    public String toString() {
        return "StatisticalData{" +
                "city_id='" + city_id + '\'' +
                ", city_name='" + city_name + '\'' +
                ", count='" + count + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }

    public String getCity_id() {
        return city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getCount() {
        int c = Integer.parseInt(count);
        if (c > 99999) {
            if (CMemoryData.isDriver()) {
                return "99999+\n单";
            } else {
                return "99999+\n辆";
            }
        } else {
            if (CMemoryData.isDriver()) {
                return count + "\n单";
            } else {
                return count + "\n辆";
            }
        }
    }

    public String getCountNotEnter() {
        int c = Integer.parseInt(count);
        if (c > 99999) {
            if (CMemoryData.isDriver()) {
                return "99999+单";
            } else {
                return "99999+辆";
            }
        } else {
            if (CMemoryData.isDriver()) {
                return count + "单";
            } else {
                return count + "辆";
            }
        }
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }
}
