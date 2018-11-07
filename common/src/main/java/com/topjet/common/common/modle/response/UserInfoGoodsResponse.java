package com.topjet.common.common.modle.response;

import com.topjet.common.common.modle.bean.UserInfoGoods;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/11/13
 * describe: 用户信息页 诚信查询 货源信息
 */

public class UserInfoGoodsResponse {

    private List<UserInfoGoods> list;

    public List<UserInfoGoods> getList() {
        return list;
    }

    public void setList(List<UserInfoGoods> list) {
        this.list = list;
    }
}
