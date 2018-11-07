package com.topjet.common.order_detail.modle.state;

import android.text.TextUtils;
import android.view.View;

import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.common.order_detail.modle.response.OrderInfoResponse;

/**
 * creator: zhulunjun
 * time:    2017/10/19
 * describe: 订单详情不同状态，底部按钮的相关显示 货主
 */

public class StateBtnMsgShipperProvider {

    private static StateBtnMsgShipperProvider instance = null;

    public synchronized static StateBtnMsgShipperProvider getInstance() {
        if (instance == null) {
            instance = new StateBtnMsgShipperProvider();
        }
        return instance;
    }

    /**
     * 设置底部按钮，文字显示
     * 根据不同的订单状态
     * 《订单详情》：
     * "修改支付方式", "添加熟车"
     * "重新找车", "托管运费", "发送提货码", "货物签收", "我要评价"  "查看回评"
     * <p>
     * 《货源详情》：
     * "修改订单", "重新找车"  "删除订单"
     * "查看报价", "发布货源"  "复制订单"
     *
     * @param orderState
     */
    public String[] oneStrShippers = {"修改支付方式", "添加熟车", "修改订单", "重新找车", "删除订单", "删除熟车"};
    public String[] twoStrShippers = {"重新找车", "托管运费", "发送提货码", "货物签收", "我要评价", "查看报价", "发布货源", "查看回评", "复制订单"};

    public String[] getBottomBtnTextOrder(int state, OrderInfoResponse orderInfo) {
        String oneStrShipper = "";
        String twoStrShipper = "";


        switch (state) {
            case OrderState.ORDER_CANCEL: // 取消

                break;
            case OrderState.ORDER_CONFIRM: // 待承运
                oneStrShipper = oneStrShippers[0]; // 修改支付方式
                twoStrShipper = twoStrShippers[0]; // 重新找车
                break;
            case OrderState.ORDER_UNPAID_DEPOSIT: // 待付定金 非订单状态，报价中

                break;
            case OrderState.ORDER_UNPAID_FREIGHT: // 待付运费
                oneStrShipper = "";
                twoStrShipper = twoStrShippers[1]; // 托管运费
                break;
            case OrderState.ORDER_PICK_UP_GOODS: // 待提货
                oneStrShipper = "";
                twoStrShipper = twoStrShippers[2]; // 发送提货码
                break;
            case OrderState.ORDER_DELIVERING: // 配送中，承运中
                oneStrShipper = "";
                twoStrShipper = twoStrShippers[3]; // 货物签收
                break;
            case OrderState.ORDER_STATUS_IS_THE_CARRIER: // 已承运/待评价
            case OrderState.ORDER_STATUS_OWNERUNEVALUATE: // 司机已评货主未评价
                if (orderInfo.getIs_familiar_truck()) {
                    oneStrShipper = oneStrShippers[5]; //  删除熟车
                } else {
                    oneStrShipper = oneStrShippers[1]; // 添加熟车
                }
                twoStrShipper = twoStrShippers[4]; // 我要评价
                break;
            case OrderState.ORDER_STATUS_DRIVERUNEVALUATE: //货主已评司机未评价
            case OrderState.ORDER_STATUS_EVALUATE: //双方已评价
                if (orderInfo.getIs_familiar_truck()) {
                    oneStrShipper = oneStrShippers[5]; //  删除熟车
                } else {
                    oneStrShipper = oneStrShippers[1]; // 添加熟车
                }
                twoStrShipper = twoStrShippers[7]; // 查看回评
                break;
            case OrderState.ORDER_STATUS_REFUND: // 已退款
                oneStrShipper = oneStrShippers[4]; // 删除订单
                twoStrShipper = twoStrShippers[8]; // 复制订单
                break;
            default:
                break;
        }
        return new String[]{oneStrShipper, twoStrShipper};

    }

    /**
     * 货源
     *
     * @param state 货源状态
     * @return
     */
    public String[] getBottomBtnTextGoods(int state) {
        String oneStrShipper = "";
        String twoStrShipper = "";
        switch (state) {
            case GoodsState.GOODS_STATUS_NEW: // 新货源
                oneStrShipper = oneStrShippers[2]; // 修改订单
                twoStrShipper = twoStrShippers[5]; // 查看报价
                break;
            case GoodsState.GOODS_STATUS_DRIVER_ABANDON: // 司机拒绝承接
                oneStrShipper = oneStrShippers[3]; // 重现找车
                twoStrShipper = twoStrShippers[6]; // 发布货源
                break;
            default:
                // 如果货源状态大于2，显示 复制订单 复制订单
                oneStrShipper = oneStrShippers[4]; // 删除订单
                twoStrShipper = twoStrShippers[8]; // 复制订单
                break;

        }
        return new String[]{oneStrShipper, twoStrShipper};
    }
}
