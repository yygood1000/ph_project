package com.topjet.common.order_detail.modle.params;

/**
 * creator: zhulunjun
 * time:    2017/9/11
 * describe: 查看报价列表
 */

public class GetShowOfferListParams {
    private String order_time;	//时间排序	否	String	1: 正序 2:倒序
    private String order_comment_level;	// 好评度排序	否	String	:-----
    private String order_fee;	// 报价排序	否	String	:-----
    private String goods_id;	// 货源id	是	String	:-----
    private String page;	// 分页	否	String	初始值为1

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_comment_level() {
        return order_comment_level;
    }

    public void setOrder_comment_level(String order_comment_level) {
        this.order_comment_level = order_comment_level;
    }

    public String getOrder_fee() {
        return order_fee;
    }

    public void setOrder_fee(String order_fee) {
        this.order_fee = order_fee;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
