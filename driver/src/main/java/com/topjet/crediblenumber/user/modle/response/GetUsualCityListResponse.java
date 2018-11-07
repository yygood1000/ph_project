package com.topjet.crediblenumber.user.modle.response;

import com.topjet.common.base.model.BaseEvent;
import com.topjet.common.common.modle.bean.UsualCityBean;

import java.util.ArrayList;

/**
 * Created by yy on 2017/9/9.
 * <p>
 * 获取常跑城市列表 返回实体类
 */

public class GetUsualCityListResponse extends BaseEvent{
    private ArrayList<UsualCityBean> list;

    public ArrayList<UsualCityBean> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "GetUsualCityListResponse{" +
                "list=" + list +
                '}';
    }
}
