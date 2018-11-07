package com.topjet.common.common.modle.response;

import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.common.common.modle.bean.UserInfoGoods;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/11/13
 * describe: 用户信息页 诚信查询车辆信息
 */

public class UserInfoTruckResponse {

    private List<TruckInfo> list;
    private String business_line_info;	// 常跑路线文字	String

    public String getBusiness_line_info() {
        return business_line_info;
    }

    public void setBusiness_line_info(String business_line_info) {
        this.business_line_info = business_line_info;
    }

    public List<TruckInfo> getList() {
        return list;
    }

    public void setList(List<TruckInfo> list) {
        this.list = list;
    }
}
