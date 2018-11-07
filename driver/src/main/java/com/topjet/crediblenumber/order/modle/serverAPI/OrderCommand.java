package com.topjet.crediblenumber.order.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.crediblenumber.order.modle.params.MyOrderParams;
import com.topjet.common.order_detail.modle.params.ShareGoodsParams;
import com.topjet.crediblenumber.order.modle.response.MyOrderResponse;
import com.topjet.common.order_detail.modle.response.ShareGoodsListResponse;
import com.topjet.common.order_detail.modle.response.ShareGoodsResponse;

/**
 * Created by yy on 2017/7/25.
 * 司机版 订单相关
 */

public class OrderCommand extends BaseCommand<OrderCommandAPI> {

    public OrderCommand(Class<OrderCommandAPI> c, MvpActivity mActivity) {
        super(c, mActivity);
    }

    /**
     * 司机-我的订单列表
     */
    public void getMyOrderListData(MyOrderParams params,
                                   ObserverOnNextListener<MyOrderResponse> listener) {

        mCommonParams = getParams(OrderCommandAPI.GET_MY_ORDER_LIST_DATA, params);
        handleOnNextObserver(mApiService.getMyOrderListData(mCommonParams), listener, false);
    }


}