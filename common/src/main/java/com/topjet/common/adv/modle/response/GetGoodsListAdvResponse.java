package com.topjet.common.adv.modle.response;

import com.topjet.common.adv.modle.bean.GoodsListAdvBean;

import java.util.ArrayList;

/**
 * Created by yy on 2017/11/10.
 * <p>
 * 货源列表广告返回体
 */

public class GetGoodsListAdvResponse {
    ArrayList<GoodsListAdvBean> list;

    public ArrayList<GoodsListAdvBean> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "GetGoodsListAdvResponse{" +
                "list=" + list +
                '}';
    }
}
