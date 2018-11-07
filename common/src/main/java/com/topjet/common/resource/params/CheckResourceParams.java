package com.topjet.common.resource.params;

import com.topjet.common.resource.bean.CheckResourseParamsBean;

import java.util.ArrayList;

/**
 * Created by yy on 2017/12/7.
 * 校验资源文件更新 请求体
 */

public class CheckResourceParams {
    private ArrayList<CheckResourseParamsBean> resource_file_check;

    public CheckResourceParams(ArrayList<CheckResourseParamsBean> list) {
        this.resource_file_check = list;
    }

    @Override
    public String toString() {
        return "CheckResourceParams{" +
                "resource_file_check=" + resource_file_check +
                '}';
    }
}
