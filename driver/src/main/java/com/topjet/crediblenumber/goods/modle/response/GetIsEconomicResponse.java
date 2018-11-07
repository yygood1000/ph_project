package com.topjet.crediblenumber.goods.modle.response;

/**
 * Created by yy on 2017/11/17.
 * 司机-是否有货运经济人
 */

public class GetIsEconomicResponse {
    private String economic_count;

    public String getEconomic_count() {
        return economic_count;
    }

    @Override
    public String toString() {
        return "GetIsEconomicResponse{" +
                "economic_count='" + economic_count + '\'' +
                '}';
    }
}
