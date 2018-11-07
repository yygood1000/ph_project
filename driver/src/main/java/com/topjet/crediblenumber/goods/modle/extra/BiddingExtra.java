package com.topjet.crediblenumber.goods.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * Created by yy on 2017/9/7.
 * <p>
 * 报价弹窗页面，当进入页面时修改报价时 需要带入 报价以及定金
 */

public class BiddingExtra extends BaseExtra {

    private String freight;// 运费
    private String deposit;// 定金
    private String pre_goods_id;// 货源id
    private String pre_goods_version;// 货源版本
    private String goods_id;// 	报价单id
    private String goods_version;// 报价单版本
    private boolean isAlter;// 是否是修改报价

    /**
     * 既有运费又有定金时使用该构造方法
     */
    public BiddingExtra() {
    }

    public String getFreight() {
        return freight;
    }

    public String getDeposit() {
        return deposit;
    }

    public String getPre_goods_id() {
        return pre_goods_id;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public void setPre_goods_id(String pre_goods_id) {
        this.pre_goods_id = pre_goods_id;
    }

    public String getPre_goods_version() {
        return pre_goods_version;
    }

    public void setPre_goods_version(String pre_goods_version) {
        this.pre_goods_version = pre_goods_version;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_version() {
        return goods_version;
    }

    public void setGoods_version(String goods_version) {
        this.goods_version = goods_version;
    }

    public boolean isAlter() {
        return isAlter;
    }

    public void setAlter(boolean alter) {
        isAlter = alter;
    }
}
