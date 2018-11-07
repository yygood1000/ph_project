package com.topjet.common.adv.modle.params;

/**
 * Created by yy on 2017/11/13.
 * <p>
 * 获取活动链接
 */

public class GetRegularActivityUrlParams {
    private String activity_id;

    public GetRegularActivityUrlParams(String activity_id) {
        this.activity_id = activity_id;
    }


    @Override
    public String toString() {
        return "GetRegularActivityUrlParams{" +
                "activity_id='" + activity_id + '\'' +
                '}';
    }
}
