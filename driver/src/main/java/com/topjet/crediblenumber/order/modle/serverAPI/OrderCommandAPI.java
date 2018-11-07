package com.topjet.crediblenumber.order.modle.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.crediblenumber.order.modle.params.MyOrderParams;
import com.topjet.crediblenumber.order.modle.response.MyOrderResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by yy on 2017/7/25.
 * 货源相关
 */

public interface OrderCommandAPI {
    // 司机-我的订单列表
    String GET_MY_ORDER_LIST_DATA = "driverorder.driverorderlist";
    @POST("order-service/driverorder/driverorderlist")
    Observable<BaseResponse<MyOrderResponse>> getMyOrderListData(@Body CommonParams<MyOrderParams> commonParams);


}
