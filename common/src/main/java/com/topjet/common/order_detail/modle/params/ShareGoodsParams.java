package com.topjet.common.order_detail.modle.params;

import java.util.ArrayList;
import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/10/24
 * describe: 分享货源参数
 */

public class ShareGoodsParams {

    private List<String> goods_ids;
    private String goods_id; // 单个id
    private String share_status; // 是否分享成功 0否1是

    public static final String SUCCESS = "1";
    public static final String FAIL = "0";

    // 货源列表
    private String owner_id;	// 货主id	是	String
    private String page;	// 页数	是	String	初始值为1

    public List<String> getGoods_ids() {
        return goods_ids;
    }

    /**
     * 设置单个id
     * @param goods_id
     */
    public void setGoods_id(String goods_id) {
        List<String> ids = new ArrayList<>();
        ids.add(goods_id);
        this.goods_ids = ids;
    }

    public void setGoods_ids(List<String> goods_ids) {
        this.goods_ids = goods_ids;
    }

    public String getShare_status() {
        return share_status;
    }

    public void setShare_status(String share_status) {
        this.share_status = share_status;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
