package com.topjet.crediblenumber.user.modle.response;

import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.crediblenumber.user.modle.bean.CallLogData;

import java.util.ArrayList;

/**
 * Created by yy on 2017/9/9.
 * <p>
 * 司机-通话记录
 */

public class GetCollLogListResponse {
    private ArrayList<CallLogData> call_response_list;

    public ArrayList<CallLogData> getList() {
        return call_response_list;
    }

    @Override
    public String toString() {
        return "GetCollLogListResponse{" +
                "call_response_list=" + call_response_list +
                '}';
    }
}
