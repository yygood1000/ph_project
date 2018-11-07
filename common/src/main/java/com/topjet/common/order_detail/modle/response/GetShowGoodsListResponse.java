package com.topjet.common.order_detail.modle.response;

import com.topjet.common.order_detail.modle.bean.DriverInfo;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/10/10
 * describe: 货主-查看过货源的司机列表
 */

public class GetShowGoodsListResponse {

    private List<DriverInfo> check_on_supply_of_goodss;

    public List<DriverInfo> getCheck_on_supply_of_goodss() {
        return check_on_supply_of_goodss;
    }

    public void setCheck_on_supply_of_goodss(List<DriverInfo> check_on_supply_of_goodss) {
        this.check_on_supply_of_goodss = check_on_supply_of_goodss;
    }
}
