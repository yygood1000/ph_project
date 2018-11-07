package com.topjet.common.order_detail.modle.state;

import android.text.TextUtils;

import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/10/19
 * describe: 订单详情不同状态，底部按钮的相关显示 司机
 */

public class StateBtnMsgDriverProvider {

    private static StateBtnMsgDriverProvider instance = null;

    public synchronized static StateBtnMsgDriverProvider getInstance(){
        if(instance == null){
            instance = new StateBtnMsgDriverProvider();
        }
        return instance;
    }

    /**
     * 设置底部按钮，文字显示
     * 根据不同的订单状态
     * 《订单详情》：
     * "放弃承接", "地图导航" "我要分享"
     * "我要承接", "确认提货", "确认签收", "我要评价" "查看回评"
     * <p>
     * 《货源详情》：
     * "撤销报价"
     * "我要接单", "修改报价"
     *
     * @param orderState
     */
    public String[] oneStrDrivers = {"放弃承接", "地图导航", "撤销报价", "我要分享"};
    public String[] twoStrDrivers = {"我要承接", "确认提货", "确认签收", "我要评价", "我要接单", "修改报价", "查看回评"};

    public String[] getBottomBtnTextOrder(int orderState) {
        String oneStrDriver = "";
        String twoStrDriver = "";
        switch (orderState) {
            case OrderState.ORDER_CANCEL: // 取消

                break;
            case OrderState.ORDER_CONFIRM: // 待承运
                oneStrDriver = oneStrDrivers[0]; // 放弃承接
                twoStrDriver = twoStrDrivers[0]; // 我要承接
                break;
            case OrderState.ORDER_UNPAID_DEPOSIT: // 待付定金 非订单状态，报价中

                break;
            case OrderState.ORDER_UNPAID_FREIGHT: // 待付运费
                oneStrDriver = oneStrDrivers[1]; // 地图导航
                twoStrDriver = twoStrDrivers[1]; // 确认提货
                break;
            case OrderState.ORDER_PICK_UP_GOODS: // 待提货
                oneStrDriver = oneStrDrivers[1]; // 地图导航
                twoStrDriver = twoStrDrivers[1]; // 确认提货
                break;
            case OrderState.ORDER_DELIVERING: // 配送中，承运中
                oneStrDriver = oneStrDrivers[1]; // 地图导航
                twoStrDriver = twoStrDrivers[2]; // 确认签收
                break;
            case OrderState.ORDER_STATUS_IS_THE_CARRIER: // 已承运/待评价
            case OrderState.ORDER_STATUS_DRIVERUNEVALUATE: //货主已评司机未评价
                oneStrDriver = "";
                twoStrDriver = twoStrDrivers[3]; // 我要评价
                break;

            case OrderState.ORDER_STATUS_OWNERUNEVALUATE: // 司机已评货主未评价
            case OrderState.ORDER_STATUS_EVALUATE: //双方已评价
                oneStrDriver = oneStrDrivers[3]; // 我要分享
                twoStrDriver = twoStrDrivers[6]; // 查看回评
                break;
            case OrderState.ORDER_STATUS_REFUND: // 已退款
                oneStrDriver = oneStrDrivers[3]; // 我要分享
                twoStrDriver = twoStrDrivers[6]; // 查看回评
                break;
            default:
                break;
        }

        return new String[]{oneStrDriver, twoStrDriver};

    }

    /**
     * 货源显示
     * @param isOffer 是否已报价
     * @return
     */
    public String[] getBottomBtnTextGoods(String isOffer) {
        String oneStrDriver = "";
        String twoStrDriver = "";
        if (StringUtils.isNotBlank(isOffer) && isOffer.equals("1")) { // 已报价
            oneStrDriver = oneStrDrivers[2]; // 撤销报价
            twoStrDriver = twoStrDrivers[5]; // 修改报价
        }else {
            oneStrDriver = "";
            twoStrDriver = twoStrDrivers[4];
        }

        return new String[]{oneStrDriver, twoStrDriver};
    }
}
