package com.topjet.crediblenumber.goods.modle.response;

import com.topjet.common.utils.StringUtils;

/**
 * Created by yy on 2017/9/9.
 * <p>
 * 司机 接单.修改报价 返回定金账单号
 */

public class BidOrderAlterResponse {
    private String bill_no;// 账单号；

    @Override
    public String toString() {
        return "BidOrderAlterResponse{" +
                "bill_no='" + bill_no + '\'' +
                '}';
    }

    public BidOrderAlterResponse() {
    }

    public BidOrderAlterResponse(String bill_no) {
        this.bill_no = bill_no;
    }

    public String getBill_no() {
        return StringUtils.isBlank(bill_no) ? "" : bill_no;
    }
}
