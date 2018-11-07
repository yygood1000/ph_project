package com.topjet.common.order_detail.modle.response;

import com.topjet.common.order_detail.modle.bean.ShareGoodsBean;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/10/24
 * describe: 分享的货源信息
 */

public class ShareGoodsResponse {

    private String share_time;    // 分享时间	string
    private String owner_name;    // 货主姓名	string
    private String owner_mobile;    // 货主手机号	string
    private String owner_icon_key;    // 货主头像key	string
    private String owner_icon_url;    // 货主头像URL	string
    private List<ShareGoodsBean> share_goods_lists;    // 分享的货源信息集合	List	结构见下表


    public String getShare_time() {
        return share_time;
    }

    public void setShare_time(String share_time) {
        this.share_time = share_time;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_mobile() {
        return owner_mobile;
    }

    public void setOwner_mobile(String owner_mobile) {
        this.owner_mobile = owner_mobile;
    }

    public String getOwner_icon_key() {
        return owner_icon_key;
    }

    public void setOwner_icon_key(String owner_icon_key) {
        this.owner_icon_key = owner_icon_key;
    }

    public String getOwner_icon_url() {
        return owner_icon_url;
    }

    public void setOwner_icon_url(String owner_icon_url) {
        this.owner_icon_url = owner_icon_url;
    }

    public List<ShareGoodsBean> getShare_goods_lists() {
        return share_goods_lists;
    }

    public void setShare_goods_lists(List<ShareGoodsBean> share_goods_lists) {
        this.share_goods_lists = share_goods_lists;
    }
}
