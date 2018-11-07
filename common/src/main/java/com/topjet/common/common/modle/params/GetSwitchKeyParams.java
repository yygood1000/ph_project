package com.topjet.common.common.modle.params;

import com.topjet.common.config.CConstants;

/**
 * Created by tsj-004 on 2017/12/4.
 * 切换app前获取key 请求类
 */

public class GetSwitchKeyParams {
    private String switch_type = null;  // 要切换的app类型[常量对照表]--> [通用常量] --[请求源常量]

    /**
     * Android司机版	1
     * Android发货版	2
     * IOS司机版       3
     * IOS发货版       4
     * H5请求          5	 H5请求专用
     */

    public GetSwitchKeyParams(String switch_type) {
        this.switch_type = switch_type;
    }

    public GetSwitchKeyParams(String packageName, String s) {
        this.switch_type = getTypeByPackageName(packageName);
    }

    /**
     * 根据包名获取type
     */
    private String getTypeByPackageName(String packageName) {
        if (CConstants.DRIVER_PACKAGE_NAME.equalsIgnoreCase(packageName)) {
            return "1";
        } else if (CConstants.SHIPPER_PACKAGE_NAME.equalsIgnoreCase(packageName)) {
            return "2";
        }

        return "2";
    }

    public String getSwitch_type() {
        return switch_type;
    }

    public void setSwitch_type(String switch_type) {
        this.switch_type = switch_type;
    }


}