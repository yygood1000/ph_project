package com.topjet.common.order_detail.modle.response;

import com.topjet.common.order_detail.modle.bean.MyOrderListItem;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/10/24
 * describe: 能分享的list 货源
 */

public class ShareGoodsListResponse {

    private List<MyOrderListItem> share_goods_list_by_owner_ids;

    public List<MyOrderListItem> getShare_goods_list_by_owner_ids() {
        return share_goods_list_by_owner_ids;
    }

    public void setShare_goods_list_by_owner_ids(List<MyOrderListItem> share_goods_list_by_owner_ids) {
        this.share_goods_list_by_owner_ids = share_goods_list_by_owner_ids;
    }
}
