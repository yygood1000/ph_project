package com.topjet.common.order_detail.modle.response;

import com.topjet.common.order_detail.modle.bean.MyOfferList;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/9/11
 * describe: 我的报价列表
 */

public class GetMyOfferListResponse {

    private List<MyOfferList> list;

    public List<MyOfferList> getList() {
        return list;
    }

    public void setList(List<MyOfferList> list) {
        this.list = list;
    }
}
