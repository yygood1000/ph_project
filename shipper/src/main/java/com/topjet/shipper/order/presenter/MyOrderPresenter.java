package com.topjet.shipper.order.presenter;

import android.content.Context;

import com.topjet.common.adv.modle.params.MarqueeParams;
import com.topjet.common.adv.modle.response.GetMarqueeResponse;
import com.topjet.common.adv.modle.serverAPI.AdvCommand;
import com.topjet.common.adv.modle.serverAPI.AdvCommandAPI;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseFragmentApiPresenter;
import com.topjet.common.base.view.fragment.RxFragment;
import com.topjet.common.common.presenter.SkipControllerWallet;
import com.topjet.common.order_detail.modle.bean.MyOrderListItem;
import com.topjet.common.order_detail.modle.bean.RefundInfo;
import com.topjet.common.order_detail.modle.params.GoodsIdParams;
import com.topjet.common.order_detail.modle.params.OrderIdParams;
import com.topjet.common.order_detail.modle.params.UpdatePayTypeParams;
import com.topjet.common.order_detail.modle.response.OrderIdResponse;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.shipper.R;
import com.topjet.shipper.order.modle.params.OrderListParams;
import com.topjet.shipper.order.modle.response.MyOrderResponse;
import com.topjet.shipper.order.modle.serverAPI.OrderCommand;
import com.topjet.shipper.order.view.activity.OrderDetailActivity;
import com.topjet.shipper.order.view.fragment.MyOrderView;

/**
 * Created by yy on 2017/8/14.
 * 我的订单Presenter
 */

public class MyOrderPresenter extends BaseFragmentApiPresenter<MyOrderView<MyOrderListItem>, OrderCommand> {
    public MyOrderPresenter(MyOrderView mView, Context mContext, RxFragment mFragment, OrderCommand mApiCommand) {
        super(mView, mContext, mFragment, mApiCommand);
    }

    /**
     * 获取我的订单方法
     */
    public void getMyOrderListData(int status, int page) {
        OrderListParams params = new OrderListParams(status + "", page + "");
        mApiCommand.getMyOrderListData(params, new ObserverOnNextListener<MyOrderResponse>() {
            @Override
            public void onNext(MyOrderResponse response) {
                mView.loadSuccess(response.getList());
            }

            @Override
            public void onError(String errorCode, String msg) {
                Logger.e("okhttp "+msg);
                mView.loadFail(com.topjet.wallet.utils.ResourceUtils.getString(R.string
                        .request_error_click_to_request));
            }
        });
    }

    /**
     * 获取历史订单方法
     */
    public void getHistoryOrderListData(int status, int page) {
        OrderListParams params = new OrderListParams(status + "", page + "");
        mApiCommand.getHistoryOrderListData(params, new ObserverOnNextListener<MyOrderResponse>() {
            @Override
            public void onNext(MyOrderResponse response) {
                mView.loadSuccess(response.getList());
            }

            @Override
            public void onError(String errorCode, String msg) {
                Logger.e("okhttp "+msg);
                mView.loadFail(ResourceUtils.getString(R.string.request_error_click_to_request));
            }
        });
    }

    /**
     * 通过货源Id获取订单Id
     */
    public void getOrderIdbyGoodsId(final String goodsId) {
        new OrderDetailCommand(OrderDetailCommandApi.class, mActivity)
                .getOrderIdByGoodsId(new GoodsIdParams(goodsId), new ObserverOnResultListener<OrderIdResponse>() {
                    @Override
                    public void onResult(OrderIdResponse response) {
                        if (StringUtils.isEmpty(response.getOrder_id())) {
                            // 跳转货源详情
                            OrderDetailActivity.toGoodsDetail(mActivity, goodsId);
                        } else {
                            OrderDetailActivity.toOrderDetail(mActivity, response.getOrder_id());
                        }
                    }
                });
    }

    /**
     * 发送提货码，先获取获取退款信息，根据退款信息显示弹窗
     */
    public void lookRefund(final String orderId) {

        mApiCommand.lookRefund(new OrderIdParams(orderId), new ObserverOnNextListener<RefundInfo>() {
            @Override
            public void onNext(RefundInfo refundInfo) {
                // 发送提货码
                AutoDialogHelper.showPickupDialog(mActivity, refundInfo.getIs_oneself_refund(), refundInfo
                        .getRefund_status(), new AutoDialogHelper.OnConfirmListener() {
                    @Override
                    public void onClick() {
                        sendPickupCode(orderId);
                    }
                }).show();
            }

            @Override
            public void onError(String errorCode, String msg) {

            }
        });
    }

    /**
     * 发送提货码
     */
    private void sendPickupCode(String orderId) {
        new OrderDetailCommand(OrderDetailCommandApi.class, mActivity)
                .sendPickupCode(new OrderIdParams(orderId), new ObserverOnResultListener<Object>() {
                    @Override
                    public void onResult(Object o) {
                        Toaster.showShortToast(ResourceUtils.getString(R.string.send_success));
                        mView.refresh();
                    }
                });
    }

    /**
     * 确认签收
     * 需要支付密码
     */
    public void confirmReceivingShipper(String orderId, String orderVersion, String payPassword) {
        OrderIdParams params = new OrderIdParams(orderId, orderVersion, payPassword);
        params.setGpsInfo();
        new OrderDetailCommand(OrderDetailCommandApi.class, mActivity)
                .confirmReceivingShipper(params, new ObserverOnResultListener<Object>() {
                    @Override
                    public void onResult(Object o) {
                        Toaster.showShortToast(ResourceUtils.getString(R.string.operate_success));
                        mView.changeOrderListTabLayout(4);
                        mView.refresh();
                    }
                });
    }

    /**
     * 确认签收
     * 不需要支付密码
     */
    public void confirmReceivingShipper(String orderId, String orderVersion) {
        confirmReceivingShipper(orderId, orderVersion, null);
    }

    /**
     * 删除订单
     */
    public void deleteOrder(final String goodsId, final String goodsVersion) {
        AutoDialogHelper.showContent(mContext, "删除后将无法找回，是否确认删除？", new AutoDialogHelper.OnConfirmListener() {
            @Override
            public void onClick() {
                new OrderDetailCommand(OrderDetailCommandApi.class, mActivity)
                        .deleteOrder(new GoodsIdParams(goodsId, goodsVersion), new
                                ObserverOnResultListener<Object>() {
                                    @Override
                                    public void onResult(Object o) {
                                        mView.refresh();
                                    }
                                });
            }
        }).show();
    }

    /**
     * 获取跑马灯广告数据
     */
    public void getMarqueeAdvertisment() {
        new AdvCommand(AdvCommandAPI.class, mActivity)
                .getMarqueeAdvertisment(new MarqueeParams(MarqueeParams.TYPE_SHIPPER),
                        new ObserverOnResultListener<GetMarqueeResponse>() {
                            @Override
                            public void onResult(GetMarqueeResponse response) {
                                if (StringUtils.isNotBlank(response.getMarqueeText())) {
                                    mView.showMarquee(response.getMarqueeText());
                                }
                            }
                        });
    }


    /**
     * 修改支付方式 定价， 托管运费时调用
     */
    public void updatePayType(UpdatePayTypeParams payTypeParams, final String order_id, final String order_version) {
        new OrderDetailCommand(OrderDetailCommandApi.class, mActivity)
                .updatePayType(payTypeParams, new ObserverOnResultListener<Object>() {
                    @Override
                    public void onResult(Object o) {
                        paymentFreight(order_id, order_version);
                    }
                });
    }

    /**
     * 货主-支付运费
     */
    public void paymentFreight(String order_id, String order_version) {
        mActivity.showLoadingDialog();
        SkipControllerWallet.paymentFreight(mActivity, order_id, order_version,
                new SkipControllerWallet.OnPayForResultListener() {
                    @Override
                    public void onSucceed() {
                        mView.refresh();
                    }
                });
    }

}
