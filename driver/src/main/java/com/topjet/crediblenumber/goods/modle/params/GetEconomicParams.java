package com.topjet.crediblenumber.goods.modle.params;

import com.topjet.common.base.model.BaseExtra;

/**
 * Created by tsj005 on 2017/11/17.
 * 司机-是否有货运经济人
 */

public class GetEconomicParams extends BaseExtra {
    private String begin_city;//出发地
    private String end_city;//目的地
    private String page;


    public GetEconomicParams(String begin_city, String end_city) {
        this.begin_city = begin_city;
        this.end_city = end_city;
    }

    public void setPage(String page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "GetEconomicParams{" +
                "page='" + page + '\'' +
                "begin_city='" + begin_city + '\'' +
                ", end_city='" + end_city + '\'' +
                '}';
    }
}
