package com.topjet.common.adv.modle.response;

import com.topjet.common.adv.modle.bean.BannerBean;
import com.topjet.common.utils.ListUtils;

import java.util.ArrayList;

/**
 * Created by yy on 2017/11/14.
 * <p>
 * 首页轮播广告返回体
 */

public class GetBannerDataResponse {
    private ArrayList<BannerBean> list;

    public ArrayList<BannerBean> getList() {
        return ListUtils.isEmpty(list) ? getDefaultList() : list;
    }

    private ArrayList<BannerBean> getDefaultList() {
        list.add(new BannerBean());
        return list;
    }

    @Override
    public String toString() {
        return "GetBannerDataResponse{" +
                "list=" + list +
                '}';
    }
}
