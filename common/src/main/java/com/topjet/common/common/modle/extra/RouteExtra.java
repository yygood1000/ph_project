package com.topjet.common.common.modle.extra;

import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.order_detail.modle.bean.OrderUserInfo;
import com.topjet.common.order_detail.modle.response.GoodsInfoResponse;
import com.topjet.common.order_detail.modle.response.OrderInfoResponse;
import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/11/7
 * describe: 去 地图导航页，需要带的参数
 */

public class RouteExtra extends BaseExtra {
    private String city; // 提货城市
    private String cityCode;// 城市编码
    private String address;// 详细地址
    private String distance;// 全程距离
    private double la; // 终点经度
    private double lon; // 终点维度
    private double laTwo; // 第二个终点经度
    private double lonTow; // 第二个终点维度
    private String mobile; // 发货人，或者收货人电话
    private String name; // 发货人或者收货人姓名
    private String callText; // 拨打电话的显示文本

    private int type; // 1去提货， 2 去送货
    public static final int PICK_UP  = 1; // 提货
    public static final int DELIVERY  = 2; // 送货
    public static final int SHOW_ROUT  = 3; // 查看线路

    /**
     * 根据OrderUserInfo转RouteExtra
     */
    public static RouteExtra getRouteExtraByUserInfo(OrderUserInfo userInfo, int type){
        if(userInfo == null){
            return null;
        }
        RouteExtra extra = new RouteExtra();
        String address = userInfo.getDetailed_address();
        if(StringUtils.isEmpty(address)){
            address = userInfo.getCity();
        }
        extra.setAddress(address);
        extra.setCity(userInfo.getCity().split(" ")[0]); // 空格分数组，去第一个为城市
        extra.setLa(StringUtils.getDoubleToString(userInfo.getLatitude()));
        extra.setLon(StringUtils.getDoubleToString(userInfo.getLongitude()));
        extra.setMobile(userInfo.getMobile());
        extra.setName(userInfo.getName());
        extra.setType(type);
        extra.setCityCode(userInfo.getCity_id());

        return extra;
    }



    /**
     * 根据货源详情获取跳转查看线路的页面
     */
    public static RouteExtra getRouteExtraByGoodsInfo(GoodsInfoResponse goodsInfo){
        RouteExtra extra = new RouteExtra();
        // 全程
        extra.setDistance(goodsInfo.getThe_total_distance());
        extra.setLa(StringUtils.getDoubleToString(goodsInfo.getSender_info().getLatitude()));
        extra.setLon(StringUtils.getDoubleToString(goodsInfo.getSender_info().getLongitude()));
        extra.setLaTwo(StringUtils.getDoubleToString(goodsInfo.getReceiver_info().getLatitude()));
        extra.setLonTow(StringUtils.getDoubleToString(goodsInfo.getReceiver_info().getLongitude()));
        extra.setType(RouteExtra.SHOW_ROUT);
        return extra;
    }



    public String getCallText() {
        return callText;
    }

    public void setCallText(String callText) {
        this.callText = callText;
    }

    public double getLaTwo() {
        return laTwo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setLaTwo(double laTwo) {
        this.laTwo = laTwo;
    }

    public double getLonTow() {
        return lonTow;
    }

    public void setLonTow(double lonTow) {
        this.lonTow = lonTow;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLa() {
        return la;
    }

    public void setLa(double la) {
        this.la = la;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "RouteExtra{" +
                "city='" + city + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", address='" + address + '\'' +
                ", distance='" + distance + '\'' +
                ", la=" + la +
                ", lon=" + lon +
                ", laTwo=" + laTwo +
                ", lonTow=" + lonTow +
                ", mobile='" + mobile + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
