package com.topjet.crediblenumber.goods.modle.response;

/**
 * Created by yy on 2017/9/14.
 * <p>
 * 司机-查询该货源的有效报价数 返回体
 */

public class GetBidedPersonCountResponse {

    private String pregoods_valid_count;

    public GetBidedPersonCountResponse(String pregoods_valid_count) {
        this.pregoods_valid_count = pregoods_valid_count;
    }

    @Override
    public String toString() {
        return "GetBidedPersonCountResponse{" +
                "pregoods_valid_count='" + pregoods_valid_count + '\'' +
                '}';
    }

    public String getPregoods_valid_count() {
        return pregoods_valid_count;
    }
}
