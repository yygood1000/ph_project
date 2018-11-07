package com.topjet.common.common.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * creator: zhulunjun
 * time:    2017/12/4
 * describe: 切换app的参数
 */

public class SwitchExtra extends BaseExtra {
    private String switchKey;
    private String mobile;

    public SwitchExtra(String switchKey, String mobile) {
        this.switchKey = switchKey;
        this.mobile = mobile;
    }

    public String getSwitchKey() {
        return switchKey;
    }

    public void setSwitchKey(String switchKey) {
        this.switchKey = switchKey;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "SwitchExtra{" +
                "switchKey='" + switchKey + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
