package com.topjet.common.order_detail.modle.state;

import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.bean.RefundInfo;
import com.topjet.common.order_detail.modle.response.OrderInfoResponse;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/10/19
 * describe: 根据不同的订单状态显示不同的顶部提示
 * 1.订单状态
 * 2.货源状态
 * 3.运费状态
 * 4.退款状态
 */

public class StateTopMsgProvider {

    private static StateTopMsgProvider instance = null;

    public synchronized static StateTopMsgProvider getInstance() {
        if (instance == null) {
            instance = new StateTopMsgProvider();
        }
        return instance;
    }


    /**
     * 获取顶部提示
     * 订单
     *
     * @param orderState
     * @return
     */
    public String getTopMessage(int orderState, OrderInfoResponse data) {
        String resultShipper = "";
        String resultDriver = "";
        switch (orderState) {
            case OrderState.ORDER_CANCEL: // 取消
                resultShipper = "";
                break;
            case OrderState.ORDER_CONFIRM: // 待承运
                resultShipper = "请等待司机确认承接";
                resultDriver = "货主已将订单指派给您，请尽快确认承接！";
                break;
            case OrderState.ORDER_UNPAID_DEPOSIT: // 待付定金 非订单状态，报价中
                resultShipper = "";
                resultDriver = "";
                break;
            case OrderState.ORDER_UNPAID_FREIGHT: // 待付运费
                resultShipper = "请尽快托管运费，560钱包保障资金安全";
                resultDriver = "货主正在托管运费，请确认运费托管成功后再去提货";
                break;
            case OrderState.ORDER_PICK_UP_GOODS: // 待提货
                resultShipper = "确认司机前来提货，提货码为" + data.getPickup_code();
                resultDriver = "向货主或发货人索取提货码，完成提货。";
                break;
            case OrderState.ORDER_DELIVERING: // 配送中，承运中
                resultShipper = "货物配送中，请确认司机送达后签收！ 签收码为" + data.getUnload_code();
                resultDriver = "签收时需要签收码，请现场向收货人索取，或拨打电话";
                break;
            case OrderState.ORDER_STATUS_IS_THE_CARRIER: // 已承运/待评价
                resultShipper = "交易已完成，快去对司机做出评价吧！";
                resultDriver = "交易已完成，快去对货主做出评价吧！";
                break;
            case OrderState.ORDER_STATUS_DRIVERUNEVALUATE: // 货主已评司机未评价
                resultShipper = "感谢您的评价，560竭诚为您服务！";
                resultDriver = "交易已完成，快去对货主做出评价吧！";
                break;
            case OrderState.ORDER_STATUS_OWNERUNEVALUATE: // 司机已评货主未评价
                resultShipper = "交易已完成，快去对司机做出评价吧！";
                resultDriver = "感谢您的评价，560竭诚为您服务！";
                break;
            case OrderState.ORDER_STATUS_EVALUATE: // 双方已评价
                resultShipper = resultDriver = "感谢您的评价，560竭诚为您服务！";
                break;
            case OrderState.ORDER_STATUS_REFUND: //  已退款成功
                resultShipper = resultDriver = "退款成功，退款金额+" + data.getRefund_info().getRefund_money() + "元。";
                break;
            default:
                resultShipper = resultDriver = "感谢您的评价，560竭诚为您服务！";
                break;
        }

        // 退款的处理
        if (data.getRefund_info() != null) {
            // 优先判断客服介入退款
            // 1.发起方申请客服介入 2.接收方申请客服介入 3.客服同意退款 4.客服关闭退款
            // 状态3,4都不处理
            if (StringUtils.isNotBlank(data.getRefund_info().getCs_refund_status())) {
                // 1，2客服介入，照常显示退款信息
                if((data.getRefund_info().getCs_refund_status().equals("3") ||
                        data.getRefund_info().getCs_refund_status().equals("4"))){
                    // 客服同意，关闭 都显示之前的状态 不处理
                } else if (data.getRefund_info().getRefund_status() != 0) {
                    // 判断 提货后退款状态, 需要判断refundInfo里面的状态
                    return getTopMessageRefund(data.getRefund_info(), resultShipper, resultDriver);
                }
            }

        }


        Logger.d("TopMessageRefund 11" + resultDriver);
        if (CMemoryData.isDriver()) {
            return resultDriver;
        } else {
            return resultShipper;
        }
    }


    /**
     * 获取顶部提示
     * 货源
     * 只有货主有这个状态
     *
     * @return
     */
    public String getTopMessage(int goodsState) {
        String resultShipper = "";
        switch (goodsState) {
            case GoodsState.GOODS_STATUS_CANCEL: // 货主撤销
                resultShipper = "订单已撤销。";
                break;
            case GoodsState.GOODS_STATUS_DRIVER_ABANDON: // 司机放弃订单
                resultShipper = "您指派的司机未接单，请重新发布";
                break;
            case GoodsState.GOODS_STATUS_TIMEOUT: // 货源过期
                resultShipper = "订单已过期。";
                break;
            case GoodsState.GOODS_STATUS_REFUND: // 订单退款
                resultShipper = "退款成功，订单已撤销。";
                break;
            default:
                break;
        }

        return resultShipper;
    }


    /**
     * 获取运费支付状态显示
     * FREIGHTFEE_STATUS_NOTHOSTING	0	0.不托管
     * FREIGHTFEE_STATUS_YETHOSTING	1	1.未托管
     * FREIGHTFEE_STATUS_NON_PAYMENT	2	2.未支付
     * FREIGHTFEE_STATUS_HAVA_TO_PEY	3	3.已支付
     *
     * @param orderState
     * @return
     */
    public int getFreightState(int orderState) {
        int resultRes;
        switch (orderState) {
            case FreightState.TRUSTEESHIP_NO:
                resultRes = -1;
                break;
            case FreightState.TRUSTEESHIP_WAIT:
                resultRes = R.string.state_wait;
                break;
            case FreightState.PAY_ALREADY:
                resultRes = R.string.state_managed;
                break;
            case FreightState.PAY_END:
                resultRes = R.string.state_paid;
                break;
            default:
                resultRes = -1;
                break;
        }
        return resultRes;


    }


    /**
     * 退款状态不同的顶部显示
     * 1.发起方申请退款 2.发起方取消退款 3.接收方同意退款 4.接收方拒绝退款 5.退款失效
     *
     * @param refundInfo
     * @return
     */
    public String getTopMessageRefund(RefundInfo refundInfo, String resultShipper, String resultDriver) {

        switch (refundInfo.getRefund_status()) {
            case RefundState.REFUND_START: // 发起方申请退款
                if (refundInfo.getIs_oneself_refund().equals("0")) { // 自己发起的
                    resultDriver = resultShipper = "您已申请退款，请等待对方确认！";
                } else {
                    if (CMemoryData.isDriver()) {
                        resultDriver = "货主发起了退款申请，退款金额：" + refundInfo.getRefund_money() + "元，请及时处理！";
                    } else {
                        resultShipper = "司机发起了退款申请，退款金额：" + refundInfo.getRefund_money() + "元，请及时处理！";
                    }
                }
                break;
            case RefundState.REFUND_CANCEL: // 发起方取消退款

                break;
            case RefundState.REFUND_AGREE: // 接收方同意退款
                resultShipper = resultDriver = "退款成功，退款金额：" + refundInfo.getRefund_money() + "元！";
                break;
            case RefundState.REFUND_REJECT: // 接收方拒绝退款
//                if (refundInfo.getIs_oneself_refund().equals("0")) { // 自己发起的
//                    resultShipper = resultDriver = "您申请的退款已被拒绝，请在退款详情中查看。";
//                }
                break;
            case RefundState.REFUND_LOSE: // 退款失效

                break;

        }
        Logger.d("TopMessageRefund 22" + resultDriver + " " + refundInfo.getRefund_status());
        if (CMemoryData.isDriver()) {
            return resultDriver;
        } else {
            return resultShipper;
        }
    }
}
