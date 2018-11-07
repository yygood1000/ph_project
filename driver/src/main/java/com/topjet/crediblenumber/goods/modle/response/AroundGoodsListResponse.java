package com.topjet.crediblenumber.goods.modle.response;

import com.topjet.common.common.modle.bean.GoodsListData;

import java.util.ArrayList;

/**
 * Created by yy on 2017/8/29.
 * <p>
 * 附近货源 列表数据 返回体
 */

public class AroundGoodsListResponse {
    private ArrayList<GoodsListData> list;

    public AroundGoodsListResponse() {
    }

    public ArrayList<GoodsListData> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "AroundGoodsListResponse{" +
                "list=" + list +
                '}';
    }
}
