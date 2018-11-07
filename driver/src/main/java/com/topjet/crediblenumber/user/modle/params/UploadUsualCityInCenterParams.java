package com.topjet.crediblenumber.user.modle.params;

import com.topjet.common.common.modle.bean.UsualCityBean;

import java.util.ArrayList;

/**
 * Created by tsj-004 on 2017/8/9.
 * 司机 常跑城市 个人中心修改请求体
 */

public class UploadUsualCityInCenterParams {
    private ArrayList<String> add_business_line_city_codes;// 添加常跑城市的集合
    private ArrayList<UsualCityBean> update_business_lines;// 修改常跑城市的集合
    private ArrayList<String> delete_business_line_ids;// 删除常跑城市的集合

    public void setAddCityCodes(ArrayList<String> add_business_line_city_codes) {
        this.add_business_line_city_codes = add_business_line_city_codes;
    }

    public void setAlterBusinessLines(ArrayList<UsualCityBean> update_business_lines) {
        this.update_business_lines = update_business_lines;
    }

    public void setDeleteIds(ArrayList<String> delete_business_line_ids) {
        this.delete_business_line_ids = delete_business_line_ids;
    }

    @Override
    public String toString() {
        return "UploadUsualCityInCenterParams{" +
                "add_business_line_city_codes=" + add_business_line_city_codes +
                ", update_business_lines=" + update_business_lines +
                ", delete_business_line_ids=" + delete_business_line_ids +
                '}';
    }
}