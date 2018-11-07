package com.topjet.shipper.deliver.modle.bean;

import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;

/**
 * 常发货源列表数据项类
 * Created by tsj004 on 2017/8/29.
 */

public class OftenGoodsListItem {
    private String goods_id;
    private String goods_no;
    private String depart_city;
    private String destination_city;
    private String goods_size;
    private String truck_length_type;
    private String update_time;
    private String isSelect = "0";

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoodsNo() {
        return goods_no;
    }

    public String getdepart_city() {
        return depart_city;
    }

    public String getdestination_city() {
        return destination_city;
    }

    public String getgoods_size() {
        return goods_size;
    }

    public String gettruck_length_type() {
        return truck_length_type;
    }

    public String getupdate_time() {
        return StringUtils.isNotBlank(update_time) ?
                TimeUtils.getDateFormatOnOftenGoods(Long.parseLong(update_time)) : "";
    }

    public boolean getIsSelect() {
        return StringUtils.isNotBlank(isSelect) && isSelect.equals("1");
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
    }
}
