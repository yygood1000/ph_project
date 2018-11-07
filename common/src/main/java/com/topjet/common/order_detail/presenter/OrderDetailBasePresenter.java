package com.topjet.common.order_detail.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.topjet.common.R;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.params.TruckParams;
import com.topjet.common.common.presenter.SkipControllerWallet;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.db.bean.IMUserBean;
import com.topjet.common.order_detail.modle.extra.OrderExtra;
import com.topjet.common.order_detail.modle.params.AccepyOrderParams;
import com.topjet.common.order_detail.modle.params.CancelGoodsParams;
import com.topjet.common.order_detail.modle.params.CancelOfferParams;
import com.topjet.common.order_detail.modle.params.GoodsIdParams;
import com.topjet.common.order_detail.modle.params.OrderIdParams;
import com.topjet.common.order_detail.modle.params.ShareGoodsParams;
import com.topjet.common.order_detail.modle.params.UpdatePayTypeParams;
import com.topjet.common.order_detail.modle.response.GetCallListResponse;
import com.topjet.common.order_detail.modle.response.GetOfferNumResponse;
import com.topjet.common.order_detail.modle.response.GetShowGoodsListResponse;
import com.topjet.common.order_detail.modle.response.GoodsInfoResponse;
import com.topjet.common.order_detail.modle.response.MsgResponse;
import com.topjet.common.order_detail.modle.response.OrderInfoResponse;
import com.topjet.common.order_detail.modle.response.OrderStateResponse;
import com.topjet.common.order_detail.modle.response.ShareGoodsResponse;
import com.topjet.common.order_detail.modle.response.ValidGoodsResponse;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;
import com.topjet.common.order_detail.modle.state.FreightState;
import com.topjet.common.order_detail.view.ShareImageProvider;
import com.topjet.common.order_detail.view.activity.OrderDetailBaseView;
import com.topjet.common.order_detail.view.dialog.CancelGoodsPopup;
import com.topjet.common.utils.DelayUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;

import java.util.List;


/**
 * creator: zhulunjun
 * time:    2017/9/4
 * describe:订单详情基类的逻辑控制类
 */

public class OrderDetailBasePresenter extends BaseApiPresenter<OrderDetailBaseView, OrderDetailCommand> {

    public String mOrderId;
    public String mGoodsId;
    public int mDataSource = 0;
    public GoodsInfoResponse mGoodsInfo; // 货源详情
    public OrderInfoResponse mOrderInfo; // 订单详情

    public OrderDetailBasePresenter(OrderDetailBaseView mView, Context mContext, OrderDetailCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
        initIntent();
    }

    private void initIntent() {
        OrderExtra extra = (OrderExtra) mActivity.getIntentExtra(OrderExtra.getExtraName());
        mDataSource = extra.getDataSource();
        boolean isNull;
        if (mDataSource == OrderExtra.GOODS_DETAIL) {
            mGoodsId = extra.getOrderId();
            isNull = TextUtils.isEmpty(mGoodsId);
        } else {
            mOrderId = extra.getOrderId();
            isNull = TextUtils.isEmpty(mOrderId);
        }

        if (isNull) {
            Toaster.showShortToast("订单id为空");
            mActivity.finish();
        }
    }

    /**
     * 货源详情 货主
     */
    public void getGoodsInfoShipper() {
        mApiCommand.getGoodsInfoShipper(new GoodsIdParams(mGoodsId), new ObserverOnResultListener<GoodsInfoResponse>() {
            @Override
            public void onResult(GoodsInfoResponse goodsInfoResponse) {
                mGoodsInfo = goodsInfoResponse;
                mView.showGoodsInfo(goodsInfoResponse);
            }
        });
    }


    /**
     * 货源详情 司机
     */
    public void getGoodsInfoDriver() {
        GoodsIdParams params = new GoodsIdParams(mGoodsId);
        if(CMemoryData.getLocationInfo() != null) {
            // 司机的位置信息
            params.setLatitude(CMemoryData.getLocationInfo().getLatitude() + "");
            params.setLongitude(CMemoryData.getLocationInfo().getLongitude() + "");
        }
        mApiCommand.getGoodsInfoDriver(params, new ObserverOnResultListener<GoodsInfoResponse>() {
            @Override
            public void onResult(GoodsInfoResponse goodsInfoResponse) {
                mGoodsInfo = goodsInfoResponse;
                mView.showGoodsInfo(goodsInfoResponse);
            }
        });
    }

    /**
     * 订单详情 司机
     */
    public void getOrderInfoDriver() {

        mApiCommand.getOrderInfoDriver(new OrderIdParams(mOrderId), new ObserverOnResultListener<OrderInfoResponse>() {
            @Override
            public void onResult(OrderInfoResponse orderInfoResponse) {
                mOrderInfo = orderInfoResponse;
                mView.showOrderInfo(orderInfoResponse);
            }
        });
    }

    /**
     * 订单详情 货主
     */
    public void getOrderInfoShipper() {

        mApiCommand.getOrderInfoShipper(new OrderIdParams(mOrderId), new ObserverOnResultListener<OrderInfoResponse>() {
            @Override
            public void onResult(OrderInfoResponse orderInfoResponse) {
                mOrderInfo = orderInfoResponse;
                mView.showOrderInfo(orderInfoResponse);
            }
        });
    }

    /**
     * 货主-撤销货源
     */
    public void cancelGoods(final String goodsId, final String goodsVersion) {
        CancelGoodsPopup.showPopWindow(mActivity, new CancelGoodsPopup.OnConfirmListener() {
            @Override
            public void onClickConfirm(int position, String remark) {
                if (remark.equals(CancelGoodsPopup.REASON_3)) {
                    mApiCommand.cancelGoods(new CancelGoodsParams(goodsId, goodsVersion, remark), new
                            ObserverOnResultListener<Object>() {
                                @Override
                                public void onResult(Object o) {
                                    // 关闭页面
                                    mView.finishPage();
                                }
                            });
                } else {
                    if(mDataSource == OrderExtra.GOODS_DETAIL){
                        // 修改非定向订单
                        CommonProvider.getInstance().getJumpShipperProvider().jumpToDeliverGoodsForEdit(mActivity,goodsId);
                    } else {
                        if(mOrderInfo.getIs_assigned().equals("1")){
                            // 向内存中存储该笔订单的货源ID
                            CMemoryData.setTempGoodsId(goodsId);
                            // 修改定向订单
                            CommonProvider.getInstance().getJumpShipperProvider()
                                    .jumpToDeliverGoodsForEditAssigend(mActivity);
                        }else{
                            // 修改非定向订单
                            CommonProvider.getInstance().getJumpShipperProvider().jumpToDeliverGoodsForEdit(mActivity,goodsId);
                        }
                    }
                }
            }
        });
    }

    /**
     * 货主-刷新货源
     */
    public void refreshGoods(String goodsId, String goodsVersion) {
        mApiCommand.refreshGoods(new GoodsIdParams(goodsId, goodsVersion), new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                // 刷新货源
                getGoodsInfoShipper();
                Toaster.showShortToast(mActivity.getString(R.string.refresh_order_success));
            }
        });
    }

    /**
     * 货主-支付运费
     */
    public void paymentFreight() {
        mActivity.showLoadingDialog();
        SkipControllerWallet.paymentFreight(mActivity, mOrderInfo.getOrder_id(), mOrderInfo.getOrder_version(),
                new SkipControllerWallet.OnPayForResultListener() {
                    @Override
                    public void onSucceed() {
                        // 支付成功
                        getOrderInfoShipper();
                    }
                });
    }

    /**
     * 司机-撤销报价
     */
    public void cancelOffer(List<String> ids) {

        mApiCommand.cancelOffer(new CancelOfferParams(ids), new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                // 刷新货源
                // 弹窗提示, 关闭页面
                Toaster.showShortToast("撤销成功");
                mView.finishPage();
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
                        mView.finishPage();
                        Toaster.showShortToast("确认成功");
                    }
                });
    }

    /**
     * 司机-确认提货
     */
    public void confirmPickUpGoods(String orderId, String pickUpCode, String orderVersion) {
        OrderIdParams params = new OrderIdParams(orderId, orderVersion);
        params.setPickup_code(pickUpCode);
        params.setGpsInfo();
        mApiCommand.confirmPickUpGoods(params, new ObserverOnResultListener<MsgResponse>() {
            @Override
            public void onResult(MsgResponse o) {
                if (o.getCode()) {
                    Toaster.showLongToast("提货成功，\n配送途中注意安全哦");
                    // 刷新货源
                    getOrderInfoDriver();
                } else {
                    Toaster.showShortToast(o.getMsg());
                }
            }
        });
    }


    /**
     * 司机-确认签收
     */
    public void confirmReceivingDriver(String orderId, String uploadCode, String orderVersion) {
        OrderIdParams params = new OrderIdParams(orderId, orderVersion);
        params.setUnload_code(uploadCode);
        params.setGpsInfo();
        mApiCommand.confirmReceivingDriver(params, new ObserverOnResultListener<MsgResponse>() {
            @Override
            public void onResult(MsgResponse o) {
                if (o.getCode()) {
                    if (mOrderInfo.getFreight().getDelivery_fee() > 0
                            && mOrderInfo.getFreight().getDelivery_fee_status() > FreightState.TRUSTEESHIP_NO) {
                        // 这里要显示gif动画
                        mView.showGif();
                        mView.showToast("签收成功，\n运费已转到您的钱包中");
                    } else {
                        mView.showToast("签收成功，\n本次交易顺利完成");
                    }
                    // 刷新货源
                    delayGetOrderData();
                } else {
                    Toaster.showShortToast(o.getMsg());
                }

            }
        });
    }

    /**
     * 延迟获取数据
     */
    private void delayGetOrderData() {
        // 刷新货源, 这里数据更新比较慢，需要延迟
        DelayUtils.delay(1000 * 2, new DelayUtils.OnListener() {
            @Override
            public void onListener() {
                getOrderData();
            }
        });
    }

    /**
     * 货主-确认签收
     * 需要支付密码
     */
    public void confirmReceivingShipper(String orderId, String orderVersion, String payPassword) {
        OrderIdParams params = new OrderIdParams(orderId, orderVersion, payPassword);
        params.setGpsInfo();
        mApiCommand.confirmReceivingShipper(params, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                mView.showToast("签收成功");

                delayGetOrderData();
            }
        });
    }

    /**
     * 货主-确认签收
     * 不需要支付密码
     */
    public void confirmReceivingShipper(String orderId, String orderVersion) {
        confirmReceivingShipper(orderId, orderVersion, null);
    }

    /**
     * 司机-查询该货源的有效报价数
     */
    public void selectOfferNum(String goodsId, ObserverOnNextListener<GetOfferNumResponse> listener) {

        mApiCommand.selectOfferNum(new GoodsIdParams(goodsId), listener);
    }

    /**
     * 司机-放弃订单
     */
    public void giveUpOrder(String orderId, String orderVersion) {

        mApiCommand.giveUpOrder(new OrderIdParams(orderId, orderVersion), new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                // 放弃承接，自动关闭页面
                mView.finishPage();

            }
        });
    }

    /**
     * 货主-查看过货源的司机列表
     */
    public void getShowGoodsList(String goodsId, String goodsVersion, String page,
                                 ObserverOnResultListener<GetShowGoodsListResponse> listener) {
        mApiCommand.getShowGoodsList(new GoodsIdParams(goodsId, goodsVersion, page), listener);
    }

    /**
     * 货主-拨打电话记录列表
     */
    public void getCallPhoneList(String goodsId, String goodsVersion, String page,
                                 ObserverOnResultListener<GetCallListResponse> listener) {
        mApiCommand.getCallPhoneList(new GoodsIdParams(goodsId, goodsVersion, page), listener);
    }

    /**
     * 发送提货码
     */
    public void sendPickupCode(String orderId) {
        new OrderDetailCommand(OrderDetailCommandApi.class, mActivity)
                .sendPickupCode(new OrderIdParams(orderId), new ObserverOnResultListener<Object>() {
                    @Override
                    public void onResult(Object o) {
                        Toaster.showShortToast(ResourceUtils.getString(R.string.send_success));
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderData();
    }

    /**
     * 刷新订单数据
     */
    public void getOrderData() {
        if (CMemoryData.isDriver()) {
            if (mDataSource == OrderExtra.GOODS_DETAIL) {
                getGoodsInfoDriver();
            } else {
                getOrderInfoDriver();
            }
        } else {
            if (mDataSource == OrderExtra.GOODS_DETAIL) {
                getGoodsInfoShipper();
            } else {
                getOrderInfoShipper();
            }
        }
    }

    /**
     * 获取分享内容
     *
     * @param id 分享的货源id
     */
    public void shareGoods(String id) {
        ShareGoodsParams params = new ShareGoodsParams();
        params.setOwner_id(CMemoryData.getUserId());
        params.setGoods_id(id);

        mApiCommand.shareGoodsList(params, new ObserverOnResultListener<ShareGoodsResponse>() {
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
     * 记录分享状态
     *
     * @param flag
     */
    public void recordShareGoods(String id, String flag) {
        ShareGoodsParams params = new ShareGoodsParams();
        params.setShare_status(flag);
        params.setGoods_id(id);
        mApiCommand.recordShareGoods(params, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {


            }
        });
    }

    /**
     * 删除，或者，添加
     *
     * @param carId 车辆id
     * @param flag  添加0，删除1
     */
    public void addOrDeleteCar(String carId, final int flag) {
        CommonProvider.getInstance().getFamiliarCarProvider().addOrDeleteCar(mActivity, carId, flag, new
                ObserverOnResultListener<Object>() {
                    @Override
                    public void onResult(Object o) {
                        if (flag == TruckParams.ADD) {
                            Toaster.showShortToast("添加成功");
                        } else {
                            Toaster.showShortToast("删除成功");
                        }
                        // 刷新订单
                        getOrderData();
                    }
                });
    }

    /**
     * 修改支付方式 定价， 托管运费时调用
     */
    public void updatePayType(UpdatePayTypeParams payTypeParams) {
        mApiCommand.updatePayType(payTypeParams, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                paymentFreight();

            }
        });
    }

    /**
     * 修改支付方式 报价
     */
    public void updatePayTypeOrder(UpdatePayTypeParams payTypeParams) {
        mApiCommand.updatePayTypeOrder(payTypeParams, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                getOrderData();
            }
        });
    }

    /**
     * 删除订单
     */
    public void deleteOrder(final String orderId, final String orderVersion) {
        AutoDialogHelper.showContent(mContext, "删除后将无法找回，是否确认删除？", new AutoDialogHelper.OnConfirmListener() {
            @Override
            public void onClick() {
                mApiCommand.deleteOrder(new GoodsIdParams(orderId, orderVersion), new
                        ObserverOnResultListener<Object>() {
                            @Override
                            public void onResult(Object o) {
                                mView.finishPage();
                            }
                        });
            }
        }).show();


    }


    /**
     * 检查是否可以聊天
     *
     * @param goodsId
     * @param goodsVersion
     */
    public void checkChat(String goodsId, String goodsVersion) {
        mApiCommand.selectValidGoods(new GoodsIdParams(goodsId, goodsVersion), new
                ObserverOnResultListener<ValidGoodsResponse>() {
                    @Override
                    public void onResult(ValidGoodsResponse validGoodsResponse) {
                        if (validGoodsResponse.isVallid()) {
                            CommonProvider.getInstance().getJumpChatPageProvider().jumpChatPage(mActivity,
                                    getImUserBean(),
                                    mGoodsInfo);
                        } else {
                            CommonProvider.getInstance().getJumpChatPageProvider().jumpChatPage(mActivity,
                                    getImUserBean());
                        }
                    }
                });
    }

    /**
     * 获取聊天用户信息
     */
    public IMUserBean getImUserBean() {
        IMUserBean userBean;
        if (mDataSource == OrderExtra.ORDER_DETAIL) {
            if (CMemoryData.isDriver()) {
                // 司机和货主聊
                userBean = mOrderInfo.getIMUserInfo(mOrderInfo.getOwner_info());
            } else {
                // 货主和司机聊
                userBean = mOrderInfo.getIMUserInfo(mOrderInfo.getDriver_info());
            }
        } else {
            userBean = mGoodsInfo.getIMUserInfo(mGoodsInfo.getOwner_info(), mGoodsInfo.getIm_user_info());
        }
        return userBean;
    }

}