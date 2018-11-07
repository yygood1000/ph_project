package com.topjet.common.order_detail.modle.response;

/**
 * creator: zhulunjun
 * time:    2017/9/11
 * describe:
 */

public class GetOfferNumResponse {
    private String pregoods_valid_count;

    public String getPregoods_valid_count() {
        return pregoods_valid_count;
    }

    public void setPregoods_valid_count(String pregoods_valid_count) {
        this.pregoods_valid_count = pregoods_valid_count;
    }

    public GetOfferNumResponse(String pregoods_valid_count) {
        this.pregoods_valid_count = pregoods_valid_count;
    }
}
