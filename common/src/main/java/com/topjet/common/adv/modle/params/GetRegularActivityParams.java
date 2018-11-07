package com.topjet.common.adv.modle.params;

/**
 * Created by yy on 2017/11/13.
 * <p>
 * 首页定时福袋活动请求
 */

public class GetRegularActivityParams {
    private String address_code;

    public GetRegularActivityParams() {
    }

    public GetRegularActivityParams(String address_code) {
        this.address_code = address_code;
    }


    @Override
    public String toString() {
        return "GetRegularActivityResponse{" +
                "address_code='" + address_code + '\'' +
                '}';
    }
}
