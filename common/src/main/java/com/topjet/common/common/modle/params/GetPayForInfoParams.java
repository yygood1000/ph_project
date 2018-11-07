package com.topjet.common.common.modle.params;

/**
 * Created by yy on 2017/9/28.
 * <p>
 * 获取支付信息 请求体
 */

public class GetPayForInfoParams {
    String bill_no;

    public GetPayForInfoParams(String bill_no) {
        this.bill_no = bill_no;
    }

    @Override
    public String toString() {
        return "GetPayForInfoParams{" +
                "bill_no='" + bill_no + '\'' +
                '}';
    }
}
