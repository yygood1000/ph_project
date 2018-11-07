package com.topjet.shipper.user.model.extra;

import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.utils.StringUtils;

/**
 * creator: yy
 * time:    2017/9/28
 * describe: 跳转匿名设置页面
 */

public class SetAnonymousExtra extends BaseExtra {
    private boolean is_anonymous;

    public SetAnonymousExtra(String is_anonymous) {
        this.is_anonymous = StringUtils.isNotBlank(is_anonymous) && is_anonymous.equals("1");
    }

    public boolean isAnonymous() {
        return is_anonymous;
    }
}
