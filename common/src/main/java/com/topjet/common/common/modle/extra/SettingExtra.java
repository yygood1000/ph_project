package com.topjet.common.common.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * creator: yy
 * time:    2017/10/31
 * describe: 跳转设置页面需要带入参数
 */

public class SettingExtra extends BaseExtra {
    private String referrerName;//推荐人姓名


    public String getReferrerName() {
        return referrerName;
    }

    public void setReferrerName(String referrerName) {
        this.referrerName = referrerName;
    }
}
