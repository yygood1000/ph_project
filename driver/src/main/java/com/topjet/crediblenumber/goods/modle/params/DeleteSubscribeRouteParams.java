package com.topjet.crediblenumber.goods.modle.params;

import java.util.ArrayList;

/**
 * Created by tsj-004 on 2017/8/9.
 * 删除订阅路线
 */

public class DeleteSubscribeRouteParams {
    private ArrayList<String> subscribe_line_id_list;

    public DeleteSubscribeRouteParams(ArrayList<String> list) {
        this.subscribe_line_id_list = list;
    }

    @Override
    public String toString() {
        return "DeleteSubscribeRouteParams{" +
                "list=" + subscribe_line_id_list +
                '}';
    }
}