package com.topjet.common.order_detail.modle.params;

/**
 * Created by yy on 2017/11/3.
 */

public class CancelGoodsParams extends GoodsIdParams {
    private String goods_cancel_remark;

    public CancelGoodsParams(String goods_id, String goods_version, String goods_cancel_remark) {
        super(goods_id, goods_version);
        this.goods_cancel_remark = goods_cancel_remark;
    }
}
