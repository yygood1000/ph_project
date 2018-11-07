package com.topjet.crediblenumber.goods.modle.response;

import com.topjet.common.common.modle.bean.GoodsListData;

import java.util.ArrayList;

/**
 * Created by yy on 2017/9/9.
 * <p>
 * 查找货源返回实体类
 */

public class SearchGoodsResponse {
    private ArrayList<GoodsListData> list;// 	货源集合；

    @Override
    public String toString() {
        return "SmartSearchGoodsResponse{" +
                "list=" + list +
                '}';
    }

    public ArrayList<GoodsListData> getGoodslist() {
        return list;
    }
}
