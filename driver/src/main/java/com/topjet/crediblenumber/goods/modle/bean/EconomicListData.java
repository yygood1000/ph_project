package com.topjet.crediblenumber.goods.modle.bean;

import com.topjet.common.utils.StringUtils;

/**
 * Created by yy on 2017/11/17.
 * 货运经纪人列表实体类
 */

public class EconomicListData {

    private String broker_id;//	货运经济人id	 
    private String name;//	姓名	 
    private String mobile;//	手机号	 
    private String icon_key;//	用户头像key	 
    private String icon_url;//用户头像url	 
    private String broker_route_begin_city_1;//经营路线 出发地1	  
    private String broker_route_end_city_1;//经营路线 目的地1	  
    private String broker_route_begin_city_2;//经营路线 出发地2	  
    private String broker_route_end_city_2;//经营路线 目的地2	  
    private String broker_route_begin_city_3;//经营路线 出发地3	  
    private String broker_route_end_city_3;//经营路线 目的地3

    public String getBroker_id() {
        return broker_id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getIcon_key() {
        return icon_key;
    }

    public String getIcon_url() {
        return icon_url;
    }


    public String getBusinessLine1() {
        return broker_route_begin_city_1 + "——" + broker_route_end_city_1;
    }

    public String getBusinessLine2() {
        return (StringUtils.isNotBlank(broker_route_begin_city_2) && StringUtils.isNotBlank
                (broker_route_end_city_2)) ?
                broker_route_begin_city_2 + "——" + broker_route_end_city_2 : "";
    }

    public String getBusinessLine3() {
        return (StringUtils.isNotBlank(broker_route_begin_city_3) && StringUtils.isNotBlank
                (broker_route_end_city_3)) ?
                broker_route_begin_city_3 + "——" + broker_route_end_city_3 : "";
    }

    @Override
    public String toString() {
        return "EconomicListData{" +
                "broker_id='" + broker_id + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", icon_key='" + icon_key + '\'' +
                ", icon_url='" + icon_url + '\'' +
                ", broker_route_begin_city_1='" + broker_route_begin_city_1 + '\'' +
                ", broker_route_end_city_1='" + broker_route_end_city_1 + '\'' +
                ", broker_route_begin_city_2='" + broker_route_begin_city_2 + '\'' +
                ", broker_route_end_city_2='" + broker_route_end_city_2 + '\'' +
                ", broker_route_begin_city_3='" + broker_route_begin_city_3 + '\'' +
                ", broker_route_end_city_3='" + broker_route_end_city_3 + '\'' +
                '}';
    }
}
