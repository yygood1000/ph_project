package com.topjet.common.order_detail.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * creator: zhulunjun
 * time:    2017/10/25
 * describe: 用户id
 */

public class UserIdExtra extends BaseExtra{
    private String id;

    public UserIdExtra(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
