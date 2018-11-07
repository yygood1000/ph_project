package com.topjet.shipper.order.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.order_detail.modle.bean.RefundInfo;
import com.topjet.common.order_detail.modle.params.OrderIdParams;
import com.topjet.shipper.order.modle.params.OrderListParams;
import com.topjet.shipper.order.modle.response.MyOrderResponse;

/**
 * Created by tsj-004 on 2017/7/25.
 * 货源/订单相关请求方法
 */

public class OrderCommand extends BaseCommand<OrderCommandAPI> {

    public OrderCommand(Class<OrderCommandAPI> c, MvpActivity activity) {
        super(c, activity);
    }

    /**
     * 货主-我的订单列表
     */
    public void getMyOrderListData(OrderListParams params,
                                   ObserverOnNextListener<MyOrderResponse> listener) {
        mCommonParams = getParams(OrderCommandAPI.GET_MY_ORDER_LIST_DATA, params);
        handleOnNextObserver(mApiService.getMyOrderListData(mCommonParams), listener, false);
    }

    /**
     * 货主-我的历史订单
     */
    public void getHistoryOrderListData(OrderListParams params,
                                        ObserverOnNextListener<MyOrderResponse> listener) {
        mCommonParams = getParams(OrderCommandAPI.GET_HISTORY_ORDER_LIST_DATA, params);
        handleOnNextObserver(mApiService.getHistoryOrderListData(mCommonParams), listener, false);
    }

    /**
     * 操作订单（例如：发送提货码）查看退款信息
     */
    public void lookRefund(OrderIdParams params,
                           ObserverOnNextListener<RefundInfo> listener) {
        mCommonParams = getParams(OrderCommandAPI.LOOK_REFUND, params);
        handleOnNextObserver(mApiService.lookRefund(mCommonParams), listener);
    }

    /**
     * 异常测试1
     */
    public void error11111(ObserverOnNextListener<MyOrderResponse> listener) {
        mCommonParams = getParams("你是瓜皮");
        handleOnNextObserver(mApiService.getMyOrderListData(mCommonParams), listener);
    }

}
