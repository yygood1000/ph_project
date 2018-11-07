package com.topjet.common.order_detail.modle.response;

import com.topjet.common.order_detail.modle.bean.ShowOfferList;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/9/11
 * describe: 获取报价列表，响应类
 */

public class GetOfferListResponse {

    private List<ShowOfferList> pre_order_list_response_list;

    public List<ShowOfferList> getPre_order_list_response_list() {
        return pre_order_list_response_list;
    }

    public void setPre_order_list_response_list(List<ShowOfferList> pre_order_list_response_list) {
        this.pre_order_list_response_list = pre_order_list_response_list;
    }
}
