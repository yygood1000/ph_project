package com.topjet.common.contact;

import com.topjet.common.base.model.BaseExtra;

/**
 * creator: zhulunjun
 * time:    2017/10/23
 * describe: 选择联系人的不同分类
 */

public class ContactExtra extends BaseExtra {

    public static final int FAMILIAR_CAR = 1; // 熟车邀请
    public static final int SHARE = 2; // 分享下载

    private int type ;

    public ContactExtra(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
