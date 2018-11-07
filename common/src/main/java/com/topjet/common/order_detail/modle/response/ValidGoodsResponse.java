package com.topjet.common.order_detail.modle.response;

import com.topjet.common.utils.StringUtils;

/**
 * Created by tsj-004 on 2017/11/21.
 * <p>
 * 查询货源是否有效
 */

public class ValidGoodsResponse {
    private String valid = null; // 是否有效	1.有效 2.失效

    public boolean isVallid() {
        return StringUtils.isBlank(valid) || valid.equals("1");
    }
}