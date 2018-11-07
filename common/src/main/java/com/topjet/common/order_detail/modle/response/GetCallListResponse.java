package com.topjet.common.order_detail.modle.response;

import com.topjet.common.order_detail.modle.bean.DriverInfo;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/10/10
 * describe: 货主-拨打电话记录列表
 */

public class GetCallListResponse {

    private List<DriverInfo> list;

    public List<DriverInfo> getList() {
        return list;
    }

    public void setList(List<DriverInfo> list) {
        this.list = list;
    }
}
