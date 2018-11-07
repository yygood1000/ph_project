package com.topjet.common.common.modle.response;

import com.topjet.common.common.modle.bean.ScrollBtnOptions;

import java.util.ArrayList;

/**
 * Created by tsj-004 on 2017/7/21.
 * 首页-获取用户参数,返回类
 */

public class GetScrollBtnsResponse {
    private ArrayList<ScrollBtnOptions> list;// 	是否存在常跑路线
    private String version;

    public ArrayList<ScrollBtnOptions> getList() {
        return list;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "GetScrollBtnsResponse{" +
                "list=" + list +
                ", version='" + version + '\'' +
                '}';
    }
}