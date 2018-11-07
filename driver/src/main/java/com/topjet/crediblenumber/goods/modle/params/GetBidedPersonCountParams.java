package com.topjet.crediblenumber.goods.modle.params;

/**
 * Created by yy on 2017/9/14.
 * <p>
 * 司机-查询该货源的有效报价数  请求体
 */

public class GetBidedPersonCountParams {
    private String goods_id;

    public GetBidedPersonCountParams(String goods_id) {
        this.goods_id = goods_id;
    }

    @Override
    public String toString() {
        return "GetBidedPersonCountParams{" +
                "goods_id='" + goods_id + '\'' +
                '}';
    }
}
