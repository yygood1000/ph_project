package com.topjet.crediblenumber.goods.modle.bean;

import com.topjet.common.utils.StringUtils;

/**
 * Created by yy on 2017/8/28.
 * <p>
 * 订阅路线 列表实体类
 */

public class SubscribeRouteListItem {
    public static final int ALL_ROUTE = 1;
    public static final int CHILD_ROUTE = 2;

    private String all_count;

    private String subscribe_line_id;//	出发地城市	是	String
    private String depart_city;//	出发地城市	是	String
    private String depart_city_code;//	出发地城市Code	是	String
    private String destination_city;//		目的地城市	是	String
    private String destination_city_code;//	目的地城市Code	是	String
    private String truck_type_length;//	车型车长	是	String
    private String supply_of_goods_count;//	订阅路线货源总数	是	String
    private boolean isSeleced;

    public void setSeleced(boolean seleced) {
        isSeleced = seleced;
    }

    /**
     * 构造全部路线实体类的方法
     */
    public SubscribeRouteListItem(String all_count) {
        this.all_count = all_count;
    }

    public String getSubscribe_line_id() {
        return subscribe_line_id;
    }

    public void setSubscribe_line_id(String subscribe_line_id) {
        this.subscribe_line_id = subscribe_line_id;
    }

    public boolean getIsAllRoute() {
        return StringUtils.isNotBlank(all_count);
    }

    public String getAll_count() {
        if (StringUtils.isNotBlank(all_count)) {
            int sum = Integer.parseInt(all_count);
            if (sum == 0) {
                return "";
            } else if (sum > 99) {
                return "99+";
            } else {
                return sum + "";
            }
        } else {
            return "";
        }
    }

    public void setAll_count(String all_count) {
        this.all_count = all_count;
    }

    public String getDepart_city() {
        return depart_city;
    }

    public void setDepart_city(String depart_city) {
        this.depart_city = depart_city;
    }

    public String getDepart_city_code() {
        return depart_city_code;
    }

    public void setDepart_city_code(String depart_city_code) {
        this.depart_city_code = depart_city_code;
    }

    public String getDestination_city() {
        return destination_city;
    }

    public void setDestination_city(String destination_city) {
        this.destination_city = destination_city;
    }

    public String getDestination_city_code() {
        return destination_city_code;
    }

    public void setDestination_city_code(String destination_city_code) {
        this.destination_city_code = destination_city_code;
    }

    public boolean isSeleced() {
        return isSeleced;
    }

    public String getTruck_type_length() {
        return truck_type_length;
    }

    public void setTruck_type_length(String truck_type_length) {
        this.truck_type_length = truck_type_length;
    }

    public String getSupply_of_goods_count() {
        if (StringUtils.isNotBlank(supply_of_goods_count)) {
            int sum = Integer.parseInt(supply_of_goods_count);
            if (sum == 0) {
                return "";
            } else if (sum > 99) {
                return "99+";
            } else {
                return sum + "";
            }
        } else {
            return "";
        }
    }

    public void setSupply_of_goods_count(String supply_of_goods_count) {
        this.supply_of_goods_count = supply_of_goods_count;
    }

    @Override
    public String toString() {
        return "SubscribeRouteListItem{" +
                "all_count='" + all_count + '\'' +
                ", subscribe_line_id='" + subscribe_line_id + '\'' +
                ", depart_city='" + depart_city + '\'' +
                ", depart_city_code='" + depart_city_code + '\'' +
                ", destination_city='" + destination_city + '\'' +
                ", destination_city_code='" + destination_city_code + '\'' +
                ", truck_type_length='" + truck_type_length + '\'' +
                ", supply_of_goods_count='" + supply_of_goods_count + '\'' +
                ", isSeleced=" + isSeleced +
                '}';
    }
}
