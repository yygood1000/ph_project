package com.topjet.common.resource.response;

import com.topjet.common.resource.bean.CheckResourseResponseBean;

import java.util.ArrayList;

/**
 * Created by yy on 2017/12/7.
 * 校验资源文件更新 返回体
 */

public class CheckReourceResponse {
    private ArrayList<CheckResourseResponseBean> resource_file_check_resp_list;

    public ArrayList<CheckResourseResponseBean> getCheckResourceList() {
        return resource_file_check_resp_list;
    }

    @Override
    public String toString() {
        return "CheckReourceResponse{" +
                "resource_file_check_resp_list=" + resource_file_check_resp_list +
                '}';
    }
}
