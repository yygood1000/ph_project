package com.topjet.common.common.modle.params;

import com.topjet.common.config.CMemoryData;

/**
 * creator: zhulunjun
 * time:    2017/10/23
 * describe: 手机号请求参数
 */

public class PhoneParams {

    private String mobile = CMemoryData.getUserMobile();

    public PhoneParams(String mobile) {
        this.mobile = mobile;
    }

    public PhoneParams() {
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
