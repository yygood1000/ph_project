package com.topjet.crediblenumber.goods.modle.params;

/**
 * Created by yy on 2017/8/9.
 * 修改订阅路线的货源查询时间
 */

public class UpdataGoodsQueryTimeParams {

    private String subscribe_line_id;//	订阅路线ID


    public UpdataGoodsQueryTimeParams(String subscribe_line_id) {
        this.subscribe_line_id = subscribe_line_id;
    }

    @Override
    public String toString() {
        return "UpdataGoodsQueryTimeParams{" +
                "subscribe_line_id='" + subscribe_line_id + '\'' +
                '}';
    }
}