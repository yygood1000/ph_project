package com.topjet.common.order_detail.modle.params;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/9/11
 * describe: 撤销报价
 */

public class CancelOfferParams {
    private List<String> pregoods_ids;

    public CancelOfferParams(List<String> pregoods_ids) {
        this.pregoods_ids = pregoods_ids;
    }

    public List<String> getPregoods_ids() {
        return pregoods_ids;
    }

    public void setPregoods_ids(List<String> pregoods_ids) {
        this.pregoods_ids = pregoods_ids;
    }
}
