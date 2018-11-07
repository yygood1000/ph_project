package com.topjet.crediblenumber.goods.modle.params;

/**
 * Created by yy on 2017/8/9.
 * 订阅路线 货源列表 请求参数
 */

public class SubscribeRouteGoodsParams {

    private String page;//	页数	是	string	初始为1
    private String subscribe_line_id;//	订阅路线ID

    public SubscribeRouteGoodsParams(String page) {
        this.page = page;
    }

    public SubscribeRouteGoodsParams(String page, String subscribe_line_id) {
        this.page = page;
        this.subscribe_line_id = subscribe_line_id;
    }

    @Override
    public String toString() {
        return "SubscribeRouteGoodsParams{" +
                "page='" + page + '\'' +
                ", subscribe_line_id='" + subscribe_line_id + '\'' +
                '}';
    }
}