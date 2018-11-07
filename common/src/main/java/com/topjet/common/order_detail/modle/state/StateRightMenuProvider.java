package com.topjet.common.order_detail.modle.state;

import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.bean.FreightInfo;
import com.topjet.common.order_detail.modle.bean.RefundInfo;

/**
 * creator: zhulunjun
 * time:    2017/10/19
 * describe: 订单详情不同状态，右边菜单按钮的不同显示
 */

public class StateRightMenuProvider {

    private static StateRightMenuProvider instance = null;

    public synchronized static StateRightMenuProvider getInstance() {
        if (instance == null) {
            instance = new StateRightMenuProvider();
        }
        return instance;
    }
    /**
     * 顶部菜单显示的文本
     */
    public String[] topMenuTexts = {"撤销订单", "分享货源", "查看合同", "我要退款", "我要投诉", "退款详情"};

    /**
     * 判断退款状态
     * 如果有退款信息就显示退款详情
     * 如果没有退款信息，有支付信息就显示我要退款
     *
     * @param refundInfo 退款信息
     * @param payState   定价 支付状态
     */
    public String [] getRefundText(RefundInfo refundInfo, FreightInfo payState, int orderState) {
        if (refundInfo.getRefund_status() == 0 ||
                refundInfo.getRefund_status() == RefundState.REFUND_CANCEL ||
                refundInfo.getCs_refund_status().equals("4")) { // 没有退款信息, 客服关闭退款，也要显示我要退款
            // 0.不托管 1.未托管 2.未支付 3.已支付
            // 2.未支付 表示 钱已经付给平台 不是0就是已托管
            // 判断钱是否已付给平台
            // 司机已承运状态时，定金已支付给货主，无法再退定金，只能查看退款 货主不需要判断订单状态
            if (getIsShowRefund(payState, orderState)) {
                return new String[]{topMenuTexts[2], topMenuTexts[3]}; // 我要退款,查看合同
            }
        } else { // 有退款信息，判断是否显示退款详情
            // 1.发起方申请退款 2.发起方取消退款 3.接收方同意退款 4.接收方拒绝退款 5.退款失效
            // 1.状态需要显示退款状态
            // 客服同意退款，也要显示退款详情
            int refundState = refundInfo.getRefund_status();
            if (refundState == RefundState.REFUND_START ||
                    refundInfo.getCs_refund_status().equals("3")) {
                return new String[]{topMenuTexts[2], topMenuTexts[5]};// 退款详情,查看合同
            } else if(refundState == RefundState.REFUND_REJECT){
                // 4.状态分两种情况
                // 拒绝退款后，如果有钱可以退，可以申请退款，否则显示退款详情
                // 主动拒绝退款后，
                // 订单详情页无退款详情入口
                // ，即不可查看自己填写的拒绝退款内容
                // ，若该用户可申请退款
                // ，则显示我要退款入口，否则不显示。
                if(getIsShowRefund(payState, orderState)){
                    if(refundInfo.getIs_oneself_refund().equals("0")) { // 如果自身退款，就显示退款详情
                        return new String[]{topMenuTexts[2], topMenuTexts[5]}; // 查看合同,退款详情
                    } else {
                        return new String[]{topMenuTexts[2], topMenuTexts[3]}; // 查看合同,我要退款
                    }
                } else {
                    return new String[]{topMenuTexts[2]}; // 查看合同
                }
            }
        }

        return new String[]{topMenuTexts[2]};
    }

    /**
     * 判断是否显示退款按钮
     * @param payState 支付状态信息
     * @param orderState 订单状态
     * @return 是否显示
     */
    private boolean getIsShowRefund(FreightInfo payState, int orderState){
        if(CMemoryData.isDriver()) {
            // 司机 定金未支付，并且订单状态小于6 金额必须大于0
            return (payState.getAgency_status() == FreightState.PAY_ALREADY && payState.getAgency_fee() > 0) &&
                    orderState < OrderState.ORDER_DELIVERING;
        } else {
            // 货主 提付费状态未支付 货主到付费状态为未支付 金额必须大于0
            return (payState.getAhead_fee_status() == FreightState.PAY_ALREADY && payState.getAhead_fee() > 0)||
                    (payState.getDelivery_fee_status() == FreightState.PAY_ALREADY && payState.getDelivery_fee() > 0);
        }
    }

    /**
     * 给货主调用的
     * 货主的退款显示不受订单状态的影响
     * 所以订单状态传-1
     * @param refundInfo
     * @param payState
     * @return
     */
    public String [] getRefundText(RefundInfo refundInfo, FreightInfo payState) {
        return getRefundText(refundInfo, payState, -1);
    }

}
