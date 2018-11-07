package com.topjet.common.order_detail.modle.state;

import com.topjet.common.order_detail.modle.bean.RefundInfo;
import com.topjet.common.order_detail.modle.response.GoodsInfoResponse;
import com.topjet.common.order_detail.modle.response.OrderInfoResponse;
import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/10/19
 * describe: 订单详情不同状态，右边菜单按钮的不同显示 司机
 */

public class StateRightMenuShipperProvider extends StateRightMenuProvider {
    private static StateRightMenuShipperProvider instance = null;

    public synchronized static StateRightMenuShipperProvider getInstance() {
        if (instance == null) {
            instance = new StateRightMenuShipperProvider();
        }
        return instance;
    }
    /**
     * 设置右边按钮显示
     * 货源详情： 撤销订单 分享货源
     * 订单详情：
     * topMenuTexts = {"撤销订单", "分享货源", "查看合同", "我要退款", "我要投诉", "退款详情"};
     *
     * @param state
     */
    public String[] getRightMenuOrder(OrderInfoResponse orderInfo, int state) {
        String [] result = null;
        switch (state) {
            case OrderState.ORDER_CANCEL: // 取消

                break;
            case OrderState.ORDER_CONFIRM: // 待承运
                result = new String[]{topMenuTexts[0], topMenuTexts[1]};
            break;
            case OrderState.ORDER_UNPAID_DEPOSIT: // 待付定金 非订单状态，报价中

                break;
            case OrderState.ORDER_UNPAID_FREIGHT: // 待付运费
            case OrderState.ORDER_PICK_UP_GOODS: // 待提货
            case OrderState.ORDER_DELIVERING: // 配送中，承运中
                // 如果有定金退款 或者 运费退款，就显示 退款详情
                // 如果有托管运费，则显示 我要退款
                // 否则就是查看合同
                result = getRefundText(orderInfo.getRefund_info(), orderInfo.getFreight());
            break;
            case OrderState.ORDER_STATUS_IS_THE_CARRIER: // 已承运/待评价
            case OrderState.ORDER_STATUS_DRIVERUNEVALUATE:
            case OrderState.ORDER_STATUS_OWNERUNEVALUATE:
            case OrderState.ORDER_STATUS_EVALUATE:
            case OrderState.ORDER_STATUS_REFUND: // 已退款

                // 如果有运费退款，就显示 退款详情
                // 待评价7之后只会出现退款成功清空，所以只用判断状态3
                if (orderInfo.getRefund_info() != null && orderInfo.getRefund_info().getRefund_status() > 0){
                    result = new String[]{topMenuTexts[2], topMenuTexts[4], topMenuTexts[5]};
                } else {
                    result = new String[]{topMenuTexts[2], topMenuTexts[4]};
                }
                break;
            default:
                result = new String[]{topMenuTexts[2], topMenuTexts[4]};
                break;
        }
        return result;


    }

    /**
     * 货源 右边菜单按钮的显示
     *
     * @param state
     * @return
     */
    public String[] getRightMenuGoods(GoodsInfoResponse orderInfo, int state) {
        // 如果货源状态为3，5 货主撤销 货源过期，不显示右上角的按钮
        if (state == GoodsState.GOODS_STATUS_CANCEL || state == GoodsState.GOODS_STATUS_TIMEOUT) {

            return null;
            // 退款成功，货源状态，需要显示 查看合同，退款详情
        } else if(state == GoodsState.GOODS_STATUS_REFUND
//                && orderInfo.getRefund_info() != null &&
//                orderInfo.getRefund_info().getRefund_status() == RefundState.REFUND_AGREE
                ){
            return new String[]{topMenuTexts[2], topMenuTexts[5]};
        } else {
            return new String[]{topMenuTexts[0], topMenuTexts[1]};
        }
    }

}
