package com.topjet.common.common.modle.bean;

import com.amap.api.maps.model.LatLng;
import com.topjet.common.adv.modle.bean.GoodsListAdvBean;
import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.order_detail.modle.bean.OwnerInfo;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.cluster.ClusterItem;

/**
 * Created by yy on 2017/8/28.
 * <p>
 * 所有 货源列表 实体类
 * 实现ClusterItem接口，用于地图点聚合
 * 听单页面用到的了RxBus，所以继承 BaseEvent
 */

public class GoodsListData extends BaseExtra implements ClusterItem {
    private String latitude;//纬度
    private String longitude;//经度
    private String depart_city;//	出发地
    private String destination_city;//目的地
    private String the_goods;//	货物	 	（货物类型，货物数量、货物单位 拼接的总字符）
    private String tuck_length_type;//车型车长拼接的总字符串
    private String update_time;//货源更新时间
    private String goods_id;//	货源id
    private String goods_version;//货源version
    private OwnerInfo owner_info;//货主信息	是
    private String listen_single_content;       //	听单播报内容
    private String goods_status = null;     // 货源状态
    private String is_call;//	是否对此货源拨打过电话	0:没打过 1：打过
    private String is_examine;//	是否查看过货源		0：没看过 1：看过
    private String is_receiving_the_order;//	是否接过此单		0：木有 1：接过
    private String is_carpool = "0";   // 是否可拼车 0 : 不可拼车/整车 1: 可拼车

    private GoodsListAdvBean advInfo;// 广告数据

    // 活动相关字段
    private String icon_image_key;//好货节补贴金额图key
    private String icon_image_url;//好货节补贴金额图url
    private String icon_key; // 好货节活动标识头像key
    private String icon_url; // 好货节活动标识头像url


    /**
     * 地图集合实现接口，返回该点经纬度对象
     */
    @Override
    public LatLng getPosition() {
        if (StringUtils.isNotBlank(latitude) || StringUtils.isNotBlank(longitude)) {
            return new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        }
        return new LatLng(0, 0);
    }

    public String getIcon_key() {
        return icon_key;
    }

    public void setIcon_key(String icon_key) {
        this.icon_key = icon_key;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public void setIcon_image_key(String icon_image_key) {
        this.icon_image_key = icon_image_key;
    }

    public void setIcon_image_url(String icon_image_url) {
        this.icon_image_url = icon_image_url;
    }

    public GoodsListData(GoodsListAdvBean advInfo) {
        this.advInfo = advInfo;
    }

    public GoodsListAdvBean getAdvInfo() {
        return advInfo;
    }

    public void setAdvInfo(GoodsListAdvBean advInfo) {
        this.advInfo = advInfo;
    }

    public void setIs_call(String is_call) {
        this.is_call = is_call;
    }

    public void setIs_examine(String is_examine) {
        this.is_examine = is_examine;
    }

    public void setIs_receiving_the_order(String is_receiving_the_order) {
        this.is_receiving_the_order = is_receiving_the_order;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setDepart_city(String depart_city) {
        this.depart_city = depart_city;
    }

    public void setDestination_city(String destination_city) {
        this.destination_city = destination_city;
    }

    public void setThe_goods(String the_goods) {
        this.the_goods = the_goods;
    }

    public void setTuck_length_type(String tuck_length_type) {
        this.tuck_length_type = tuck_length_type;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public void setGoods_version(String goods_version) {
        this.goods_version = goods_version;
    }

    public void setOwner_info(OwnerInfo owner_info) {
        this.owner_info = owner_info;
    }

    public String getListen_single_content() {
        return listen_single_content;
    }

    public void setListen_single_content(String listen_single_content) {
        this.listen_single_content = listen_single_content;
    }

    public boolean getIs_call() {
        return StringUtils.isNotBlank(is_call) && is_call.equals("1");
    }

    public boolean getIs_examine() {
        return StringUtils.isNotBlank(is_examine) && is_examine.equals("1");
    }

    public String getIs_receiving_the_order() {
        return is_receiving_the_order;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getDepart_city() {
        return depart_city;
    }

    public String getDestination_city() {
        return destination_city;
    }

    public String getThe_goods() {
        return the_goods;
    }

    public String getTuck_length_type() {
        return tuck_length_type;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public String getGoods_version() {
        return goods_version;
    }

    public OwnerInfo getOwner_info() {
        return owner_info;
    }

    public String getGoods_status() {
        return goods_status;
    }

    public void setGoods_status(String goods_status) {
        this.goods_status = goods_status;
    }

    public String getIcon_image_url() {
        return icon_image_url;
    }

    public String getIcon_image_key() {
        return icon_image_key;
    }

    public String getIs_carpool() {
        return is_carpool.equals("0") ? "整车" : "";
    }

    public void setIs_carpool(String is_carpool) {
        this.is_carpool = is_carpool;
    }

    @Override
    public String toString() {
        return "GoodsListData{" +
                "success=" + success +
                ", msgId='" + msgId + '\'' +
                ", msg='" + msg + '\'' +
                ", tag='" + tag + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", depart_city='" + depart_city + '\'' +
                ", destination_city='" + destination_city + '\'' +
                ", the_goods='" + the_goods + '\'' +
                ", tuck_length_type='" + tuck_length_type + '\'' +
                ", update_time='" + update_time + '\'' +
                ", goods_id='" + goods_id + '\'' +
                ", goods_version='" + goods_version + '\'' +
                ", owner_info=" + owner_info +
                ", listen_single_content='" + listen_single_content + '\'' +
                ", goods_status='" + goods_status + '\'' +
                ", is_call='" + is_call + '\'' +
                ", is_examine='" + is_examine + '\'' +
                ", is_receiving_the_order='" + is_receiving_the_order + '\'' +
                ", icon_image_key='" + icon_image_key + '\'' +
                ", icon_image_url='" + icon_image_url + '\'' +
                ", advInfo=" + advInfo +
                '}';
    }

}