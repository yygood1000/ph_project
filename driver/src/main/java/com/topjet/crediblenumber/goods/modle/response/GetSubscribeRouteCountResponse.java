package com.topjet.crediblenumber.goods.modle.response;

import com.topjet.common.utils.StringUtils;

/**
 * Created by yy on 2017/9/13.
 * <p>
 * 订阅路线新增货源总数 返回实体
 */

public class GetSubscribeRouteCountResponse {
    private String goods_count;

    public String getGoods_count() {
        if (StringUtils.isNotBlank(goods_count)) {
            int sum = Integer.parseInt(goods_count);
            if (sum == 0) {
                return "";
            } else if (sum > 99) {
                return "99+";
            } else {
                return sum + "";
            }
        } else {
            return "";
        }
    }
}
