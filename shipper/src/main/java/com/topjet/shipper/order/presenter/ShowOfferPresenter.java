package com.topjet.shipper.order.presenter;

import android.content.Context;
import android.view.View;

import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.extra.TabIndex;
import com.topjet.common.order_detail.modle.bean.FreightInfo;
import com.topjet.common.order_detail.modle.bean.ShowOfferList;
import com.topjet.common.order_detail.modle.extra.ClickAgreeBtnEvent;
import com.topjet.common.order_detail.modle.params.CancelGoodsParams;
import com.topjet.common.order_detail.modle.params.GetShowOfferListParams;
import com.topjet.common.order_detail.modle.params.GoodsIdParams;
import com.topjet.common.order_detail.modle.params.OrderIdParams;
import com.topjet.common.order_detail.modle.params.UpdatePayTypeParams;
import com.topjet.common.order_detail.modle.response.GetOfferListResponse;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.presenter.SkipControllerProtocol;
import com.topjet.common.order_detail.view.dialog.CancelGoodsPopup;
import com.topjet.common.order_detail.view.dialog.PartialCostDialog;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.shipper.MainActivity;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.view.activity.DeliverGoodsActivity;
import com.topjet.shipper.order.modle.extra.ShowOfferExtra;
import com.topjet.shipper.order.view.activity.ShowOfferView;

/**
 * creator: zhulunjun
 * time:    2017/9/12
 * describe: 报价列表
 */

public class ShowOfferPresenter extends BaseApiPresenter<ShowOfferView<ShowOfferList>, OrderDetailCommand> {
    private UpdatePayTypeParams clickItemPayTypeParams;
    private ShowOfferList clickItem;

    public ShowOfferPresenter(ShowOfferView<ShowOfferList> mView, Context mContext, OrderDetailCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    private ShowOfferExtra extra;

    @Override
    public void onCreate() {
        super.onCreate();
        extra = (ShowOfferExtra) mActivity.getIntentExtra(ShowOfferExtra.getExtraName());
        if (extra == null) {
            mActivity.finishPage();
        }
    }



    /**
     * 货主-查看报价列表
     */
    public void showOfferList(int page, GetShowOfferListParams params) {
        params.setPage(page + "");
        params.setGoods_id(extra.getGoodsId());
        mApiCommand.showOfferList(params, new ObserverOnNextListener<GetOfferListResponse>() {
            @Override
            public void onNext(GetOfferListResponse getOfferListResponse) {
                if (getOfferListResponse != null) {
                    mView.loadSuccess(getOfferListResponse.getPre_order_list_response_list());
                }
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });
    }

    /**
     * 确认成交
     * 1.先判断支付方式是否是提付费部分，弹窗提示
     * 2.然后跳转到交易协议
     * 3.回来之后再掉接口
     * 4.判断是否已经指派过了，需要提示先取消，在指派
     */

    public void confirmClick(final ShowOfferList item) {
        clickItem = item;
        if (extra.isAhead()) {// 弹窗
            FreightInfo freightInfo = new FreightInfo();
            freightInfo.setFreight_fee(item.getTransport_fee());
            new PartialCostDialog(mContext).showSetCost(freightInfo,
                    new PartialCostDialog.OnConfirmClick() {
                        @Override
                        public void confirmClick(final UpdatePayTypeParams payTypeParams) {
                            clickItemPayTypeParams = payTypeParams;
                            // 交易协议,有确认支付明细
                            SkipControllerProtocol.skipHasDialog(mActivity,
                                    item.getGoods_id(), item.getPre_order_id(), item.getPre_order_version(),
                                    item.getTransport_fee(), payTypeParams,
                                    item.getDriver_truck_type(), item.getDriver_truck_length(),
                                    item.getPlate_no(), item.getDriver_name(), item.getDriver_mobile(), item
                                            .getDriverIdCard(), TAG);
                        }
                    });
        } else { // 交易协议,无确认支付明细
            Logger.d("oye", "skipHasNotDialog");
            SkipControllerProtocol.skipHasNotDialog(mActivity,
                    item.getGoods_id(), item.getPre_order_id(), item.getPre_order_version(),
                    item.getTransport_fee(), creatPayTypeParams(item),
                    item.getDriver_truck_type(), item.getDriver_truck_length(),
                    item.getPlate_no(), item.getDriver_name(), item.getDriver_mobile(), item.getDriverIdCard(), TAG);
        }
    }

    /**
     * 构建传递给协议页的费用信息
     */
    private UpdatePayTypeParams creatPayTypeParams(ShowOfferList item) {
        // 1: 货到付款 2: 提付全款 3:提付部分运费 4:回单付运费'
        UpdatePayTypeParams payTypeParams = new UpdatePayTypeParams();
        switch (item.getPay_style()) {
            case "1":
                payTypeParams.setDelivery_fee(item.getTransport_fee());
                break;
            case "2":
                payTypeParams.setAhead_fee(item.getTransport_fee());
                break;
            case "4":
                payTypeParams.setBack_fee(item.getTransport_fee());
                break;
        }
        return payTypeParams;
    }


    /**
     * 协议页面点击同意
     * 进行确认成交接口请求
     */
    @Subscribe
    public void onEvent(ClickAgreeBtnEvent event) {
        if (event.getTag().equalsIgnoreCase(TAG)) {
            // 确认成交
            confirmDeal(clickItem.getGoods_id(), clickItem.getPre_order_id(), clickItem.getPre_order_version(),
                    clickItemPayTypeParams);
        }
    }


    /**
     * 货主-确认成交
     */
    private void confirmDeal(String goodsId, String preGoodsId, String preGoodsVersion, UpdatePayTypeParams
            payTypeParams) {
        GoodsIdParams params = new GoodsIdParams(goodsId);
        params.setPre_goods_id(preGoodsId);
        params.setPre_goods_version(preGoodsVersion);
        if (payTypeParams != null) {
            params.setOrder_pay_style(payTypeParams);
        }
        // 设置接口中的定位信息
        params.setGpsInfo();
        mApiCommand.confirmDeal(params, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                mView.showToast("确认成功");
                mActivity.turnToActivity(MainActivity.class,
                        new TabIndex(TabIndex.MY_ORDER_PAGE, TabIndex.MY_ORDER, TabIndex.WAIT_DEAL));
                mView.finishPage();
//                // 这里要发一个EvenBus 关闭订单详情页
//                BusManager.getBus().post(new CancelOrderDetailEvent(true));
            }
        });
    }

    /**
     * 取消交易
     * 1.先弹窗提示，是否取消
     * 2.调接口取消
     */
    public void cancelClick(View v, final ShowOfferList item) {
        String message = StringUtils.format(R.string.dialog_cancel_deal, item.getTransport_fee());
        AutoDialogHelper.showContent(mContext, message, new AutoDialogHelper.OnConfirmListener() {
            @Override
            public void onClick() {
                cancelDeal(item.getOrder_id(), item.getPre_order_id(), item.getPre_order_version());
            }
        }).show();
    }

    /**
     * 货主-取消交易
     */
    private void cancelDeal(String orderId, String preGoodsId, String preGoodsVersion) {
        OrderIdParams params = new OrderIdParams(orderId);
        params.setPre_goods_id(preGoodsId);
        params.setPre_goods_version(preGoodsVersion);
        mApiCommand.cancelDeal(params, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                mView.clearConfirmOfferItem();
                mView.refresh();
            }
        });
    }

    /**
     * 货主-撤销货源
     */
    public void cancelGoods() {
        CancelGoodsPopup.showPopWindow(mActivity, new CancelGoodsPopup.OnConfirmListener() {
            @Override
            public void onClickConfirm(int position, String remark) {
                if (remark.equals(CancelGoodsPopup.REASON_3)) {
                    mApiCommand.cancelGoods(new CancelGoodsParams(extra.getGoodsId(), extra.getGoodsVersion(),
                            remark), new
                            ObserverOnResultListener<Object>() {
                                @Override
                                public void onResult(Object o) {
                                    // 关闭页面
                                    mView.finishPage();
                                }
                            });
                } else {
                    DeliverGoodsActivity.turnToDeliverGoodsForCopy(mActivity, extra.getGoodsId());
                }
            }
        });
    }
}
