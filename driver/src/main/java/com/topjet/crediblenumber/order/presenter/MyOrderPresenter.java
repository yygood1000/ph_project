package com.topjet.crediblenumber.order.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseFragmentApiPresenter;
import com.topjet.common.base.view.fragment.RxFragment;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.bean.MyOrderListItem;
import com.topjet.common.order_detail.modle.params.AccepyOrderParams;
import com.topjet.common.order_detail.modle.params.OrderIdParams;
import com.topjet.common.order_detail.modle.params.ShareGoodsParams;
import com.topjet.common.order_detail.modle.response.MsgResponse;
import com.topjet.common.order_detail.modle.response.OrderStateResponse;
import com.topjet.common.order_detail.modle.response.ShareGoodsResponse;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;
import com.topjet.common.order_detail.modle.state.FreightState;
import com.topjet.common.order_detail.view.ShareImageProvider;
import com.topjet.common.utils.ShareHelper;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.order.modle.params.MyOrderParams;
import com.topjet.crediblenumber.order.modle.response.MyOrderResponse;
import com.topjet.crediblenumber.order.modle.serverAPI.OrderCommand;
import com.topjet.crediblenumber.order.view.fragment.MyOrderFragmentView;

/**
 * Created by yy on 2017/8/14.
 * 司机版 我的订单Presenter
 */

public class MyOrderPresenter extends BaseFragmentApiPresenter<MyOrderFragmentView<MyOrderListItem>, OrderCommand> {
    private String clickOrderId;

    public MyOrderPresenter(MyOrderFragmentView<MyOrderListItem> mView, Context mContext, RxFragment mFragment,
                            OrderCommand
                                    mApiCommand) {
        super(mView, mContext, mFragment, mApiCommand);
    }


    /**
     * 获取我的订单方法
     */
    public void getMyOrderListData(int status, int page) {
        MyOrderParams params = new MyOrderParams(status + "", page + "");

        mApiCommand.getMyOrderListData(params, new ObserverOnNextListener<MyOrderResponse>() {
            @Override
            public void onNext(MyOrderResponse myOrderResponse) {
                if (myOrderResponse != null) {
                    mView.loadSuccess(myOrderResponse.getList());
                }
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(com.topjet.wallet.utils.ResourceUtils.getString(R.string
                        .request_error));
            }
        });
    }


    /**
     * 确认提货
     */
    public void uploadPickUpCode(String orderId, String orderVersion, String pickUpCode) {
        OrderIdParams params = new OrderIdParams(orderId, orderVersion);
        params.setPickup_code(pickUpCode);
        params.setGpsInfo();
        new OrderDetailCommand(OrderDetailCommandApi.class, mActivity)
                .confirmPickUpGoods(params, new ObserverOnResultListener<MsgResponse>() {
                    @Override
                    public void onResult(MsgResponse o) {
                        if (o.getCode()) {
                            Toaster.showLongToast("提货成功，\n配送途中注意安全哦");
                            mView.refresh();
                        } else {
                            Toaster.showShortToast(o.getMsg());
                        }
                    }
                });
    }

    /**
     * 确认签收
     */
    public void uploadSignCode(String orderId, String orderVersion, String uploadCode, final int delivery_fee, final int
            delivery_fee_status) {
        OrderIdParams params = new OrderIdParams(orderId, orderVersion);
        params.setUnload_code(uploadCode);
        params.setGpsInfo();
        new OrderDetailCommand(OrderDetailCommandApi.class, mActivity)
                .confirmReceivingDriver(params, new ObserverOnResultListener<MsgResponse>() {
                    @Override
                    public void onResult(MsgResponse o) {
                        if (o.getCode()) {
                            if (delivery_fee > 0 && delivery_fee_status > FreightState.TRUSTEESHIP_NO) {
                                Toaster.showLongToast("签收成功，\n运费已转到您的钱包中");
                                mView.showGif();
                            } else {
                                Toaster.showLongToast("签收成功，\n本次交易顺利完成");
                            }
                            mView.changePage(3);
                        } else {
                            Toaster.showShortToast(o.getMsg());
                        }
                    }
                });
    }

    /**
     * 获取分享内容
     *
     * @param id 分享的货源id
     */
    public void shareGoods(String id) {
        clickOrderId = id;
        ShareGoodsParams params = new ShareGoodsParams();
        params.setOwner_id(CMemoryData.getUserId());
        params.setGoods_id(id);

        new OrderDetailCommand(OrderDetailCommandApi.class, mActivity).shareGoodsList(params, new
                ObserverOnResultListener<ShareGoodsResponse>() {
                    @Override
                    public void onResult(ShareGoodsResponse shareGoodsResponse) {
                        if (shareGoodsResponse != null) {
                            // 根据获取的信息 设置分享图片信息
                            String path = new ShareImageProvider(mActivity).getShareImage(shareGoodsResponse);
                            if (StringUtils.isEmpty(path)) {
                                Toaster.showLongToast("分享失败");
                                return;
                            }
                            mView.shareImage(path);
                        }
                    }
                });
    }

    /**
     * 分享的回调
     */
    public ShareHelper.OnShareComplete shareComplete = new ShareHelper.OnShareComplete() {
        @Override
        public void shareComple(String s) {
            recordShareGoods(clickOrderId, ShareGoodsParams.SUCCESS);
        }

        @Override
        public void shareError() {
            recordShareGoods(clickOrderId, ShareGoodsParams.FAIL);
        }
    };

    /**
     * 记录分享状态
     */
    private void recordShareGoods(String id, String flag) {
        ShareGoodsParams params = new ShareGoodsParams();
        params.setShare_status(flag);
        params.setGoods_id(id);
        new OrderDetailCommand(OrderDetailCommandApi.class, mActivity).recordShareGoods(params, new
                ObserverOnResultListener<Object>() {
                    @Override
                    public void onResult(Object o) {

                    }
                });
    }

    /**
     * 司机-确认承接
     */
    public void accepyOrder(String orderId, String orderVersion, String truckId) {
        AccepyOrderParams params = new AccepyOrderParams(orderId, orderVersion, truckId);
        params.setGpsInfo();
        new OrderDetailCommand(OrderDetailCommandApi.class, mActivity).acceptOrder(params, new
                ObserverOnResultListener<OrderStateResponse>() {
                    @Override
                    public void onResult(OrderStateResponse o) {
                        mView.refresh();
                        Toaster.showShortToast("确认成功");
                    }
                });
    }
}
