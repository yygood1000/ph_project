package com.topjet.crediblenumber.goods.modle.response;

import com.topjet.common.common.modle.bean.GoodsListData;

import java.util.ArrayList;

/**
 * Created by yy on 2017/9/9.
 * <p>
 * 司机 接单.修改报价 返回定金账单号
 */

public class SmartSearchGoodsResponse {
    private String icon_image_key;//好货节补贴金额图key
    private String icon_image_url;//好货节补贴金额图url
    private String icon_key; // 好货节活动标识头像key
    private String icon_url; // 好货节活动标识头像url
    private ArrayList<GoodsListData> list;// 	货源集合；

    @Override
    public String toString() {
        return "SmartSearchGoodsResponse{" +
                '}';
    }

    public ArrayList<GoodsListData> getGoodsList() {
        for(GoodsListData data : list){
            data.setIcon_image_key(icon_image_key);
            data.setIcon_image_url(icon_image_url);
            data.setIcon_key(icon_key);
            data.setIcon_url(icon_url);
        }
        return list;
    }

    public String getIcon_image_key() {
        return icon_image_key;
    }

    public String getIcon_image_url() {
        return icon_image_url;
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
}
