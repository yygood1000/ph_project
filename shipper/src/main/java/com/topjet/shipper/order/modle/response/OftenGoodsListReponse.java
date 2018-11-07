package com.topjet.shipper.order.modle.response;

import com.topjet.shipper.deliver.modle.bean.OftenGoodsListItem;

import java.util.List;

/**
 * 常用货源列表返回数据类
 * Created by tsj004 on 2017/8/29.
 */

public class OftenGoodsListReponse {
    private List<OftenGoodsListItem> list;

    public List<OftenGoodsListItem> getList() {
        return list;
    }

    public void setList(List<OftenGoodsListItem> list) {
        this.list = list;
    }
}
