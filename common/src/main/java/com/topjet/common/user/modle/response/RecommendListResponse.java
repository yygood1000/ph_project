package com.topjet.common.user.modle.response;

import com.topjet.common.user.modle.bean.ReferrerInfoBean;

import java.util.ArrayList;

/**
 * Created by yy on 2017/11/6.
 * 个人中心-我的推荐记录
 */

public class RecommendListResponse {
    private String sum;
    ArrayList<ReferrerInfoBean> list;

    public ArrayList<ReferrerInfoBean> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "RecommendListResponse{" +
                "list=" + list +
                '}';
    }
}
