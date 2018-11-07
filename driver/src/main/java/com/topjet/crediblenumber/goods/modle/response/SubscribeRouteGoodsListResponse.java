package com.topjet.crediblenumber.goods.modle.response;

import com.topjet.common.common.modle.bean.GoodsListData;

import java.util.ArrayList;

/**
 * Created by yy on 2017/8/29.
 * <p>
 * 订阅路线 货源列表 数据返回体
 */

public class SubscribeRouteGoodsListResponse {
    private ArrayList<GoodsListData> list;

    public ArrayList<GoodsListData> getList() {
        return list;

    }

    @Override
    public String toString() {
        return "SubscribeRouteGoodsListResponse{" +
                "list=" + list +
                '}';
    }
}
