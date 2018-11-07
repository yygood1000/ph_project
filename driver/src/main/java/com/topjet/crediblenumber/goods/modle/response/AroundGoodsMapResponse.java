package com.topjet.crediblenumber.goods.modle.response;

import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.common.modle.bean.StatisticalData;

import java.util.ArrayList;

/**
 * Created by yy on 2017/8/29.
 * <p>
 * 附近货源 地图数据 返回体
 */

public class AroundGoodsMapResponse {
    private ArrayList<GoodsListData> near_goods_response_list;//附近货源列表
    private ArrayList<StatisticalData> goods_statistical;//货源统计
    private String parameter_level;//1:全国级 / 省级 2:地区级

    @Override
    public String toString() {
        return "AroundGoodsMapResponse{" +
                "near_goods_response_list=" + near_goods_response_list +
                ", goods_statistical=" + goods_statistical +
                ", parameter_level='" + parameter_level + '\'' +
                '}';
    }

    public ArrayList<GoodsListData> getNear_goods_response_list() {
        return near_goods_response_list;
    }

    public ArrayList<StatisticalData> getGoods_statistical() {
        return goods_statistical;
    }

    public String getParameter_level() {
        return parameter_level;
    }
}
