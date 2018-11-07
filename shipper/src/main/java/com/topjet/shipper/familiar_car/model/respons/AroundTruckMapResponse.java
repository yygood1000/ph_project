package com.topjet.shipper.familiar_car.model.respons;

import com.topjet.common.common.modle.bean.StatisticalData;
import com.topjet.common.common.modle.bean.TruckInfo;

import java.util.ArrayList;

/**
 * Created by yy on 2017/8/29.
 * <p>
 * 附近车源 地图/地图列表 数据 返回体
 */

public class AroundTruckMapResponse {
    private ArrayList<TruckInfo> near_truck_response_list;//附近车源列表
    private ArrayList<StatisticalData> truck_statistical;//车源统计
    private String parameter_level;//1:全国级 / 省级 2:地区级
    private String total;//总数


    @Override
    public String toString() {
        return "AroundTruckMapResponse{" +
                "near_truck_response_list=" + near_truck_response_list +
                ", truck_statistical=" + truck_statistical +
                ", parameter_level='" + parameter_level + '\'' +
                ", total='" + total + '\'' +
                '}';
    }

    public ArrayList<TruckInfo> getNear_truck_response_list() {
        return near_truck_response_list;
    }

    public ArrayList<StatisticalData> getTruck_statistical() {
        return truck_statistical;
    }

    public String getParameter_level() {
        return parameter_level;
    }

    public String getTotal() {
        return total;
    }
}
