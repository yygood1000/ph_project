package com.topjet.shipper.order.modle.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.order_detail.modle.bean.RefundInfo;
import com.topjet.common.order_detail.modle.params.OrderIdParams;
import com.topjet.shipper.order.modle.params.OrderListParams;
import com.topjet.shipper.order.modle.response.MyOrderResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by  yy on 2017/7/25.
 * 订单相关
 */

public interface OrderCommandAPI {
    // 货主-我的订单列表
    String GET_MY_ORDER_LIST_DATA = "ownerorder.ownermyorderlist";
    @POST("order-service/ownerorder/ownermyorderlist")
    Observable<BaseResponse<MyOrderResponse>> getMyOrderListData(@Body CommonParams<OrderListParams> commonParams);

    // 货主-历史订单列表
    String GET_HISTORY_ORDER_LIST_DATA = "ownerorder.ownermyorderlisthistory";
    @POST("order-service/ownerorder/ownermyorderlisthistory")
    Observable<BaseResponse<MyOrderResponse>> getHistoryOrderListData(@Body CommonParams<OrderListParams> commonParams);

    // 操作订单（例如：发送提货码）查看退款信息
    String LOOK_REFUND = "refund.lookrefund";
    @POST("order-service/refund/lookrefund")
    Observable<BaseResponse<RefundInfo>> lookRefund(@Body CommonParams<OrderIdParams> commonParams);
}
