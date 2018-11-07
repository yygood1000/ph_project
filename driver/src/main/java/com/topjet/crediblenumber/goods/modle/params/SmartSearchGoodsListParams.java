package com.topjet.crediblenumber.goods.modle.params;

/**
 * Created by yy on 2017/8/9.
 * 智能找货 请求实体
 */

public class SmartSearchGoodsListParams {

    private String page;//	页数	 	初始为1
    private String start_city_code;//出发地

    @Override
    public String toString() {
        return "SmartSearchGoodsListParams{" +
                "page='" + page + '\'' +
                ", start_city_code='" + start_city_code + '\'' +
                '}';
    }

    public SmartSearchGoodsListParams(String page, String start_city_code) {
        this.page = page;
        this.start_city_code = start_city_code;
    }
}