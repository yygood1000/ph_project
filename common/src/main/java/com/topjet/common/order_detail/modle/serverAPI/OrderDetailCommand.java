package com.topjet.common.order_detail.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.order_detail.modle.params.CancelGoodsParams;
import com.topjet.common.order_detail.modle.params.CancelOfferParams;
import com.topjet.common.order_detail.modle.params.GetMyOfferListParams;
import com.topjet.common.order_detail.modle.params.GetShowOfferListParams;
import com.topjet.common.order_detail.modle.params.GoodsIdParams;
import com.topjet.common.order_detail.modle.params.OrderIdParams;
import com.topjet.common.order_detail.modle.params.ShareGoodsParams;
import com.topjet.common.order_detail.modle.params.UpdatePayTypeParams;
import com.topjet.common.order_detail.modle.response.GetCallListResponse;
import com.topjet.common.order_detail.modle.response.GetMyOfferListResponse;
import com.topjet.common.order_detail.modle.response.GetOfferListResponse;
import com.topjet.common.order_detail.modle.response.GetOfferNumResponse;
import com.topjet.common.order_detail.modle.response.GetShowGoodsListResponse;
import com.topjet.common.order_detail.modle.response.GoodsInfoResponse;
import com.topjet.common.order_detail.modle.response.MsgResponse;
import com.topjet.common.order_detail.modle.response.OrderIdResponse;
import com.topjet.common.order_detail.modle.response.OrderInfoResponse;
import com.topjet.common.order_detail.modle.response.OrderStateResponse;
import com.topjet.common.order_detail.modle.response.PaymentResponse;
import com.topjet.common.order_detail.modle.response.ProtocolResponse;
import com.topjet.common.order_detail.modle.response.ShareGoodsListResponse;
import com.topjet.common.order_detail.modle.response.ShareGoodsResponse;
import com.topjet.common.order_detail.modle.response.ValidGoodsResponse;


/**
 * creator: zhulunjun
 * time:    2017/9/5
 * describe: 订单详情相关的请求
 * 货源详情
 */

public class OrderDetailCommand extends BaseCommand<OrderDetailCommandApi> {

    public OrderDetailCommand(Class<OrderDetailCommandApi> c, MvpActivity activity) {
        super(c, activity);
    }

    public OrderDetailCommand() {
        super(OrderDetailCommandApi.class);
    }

    /**
     * 货主版货源详情
     */
    public void getGoodsInfoShipper(GoodsIdParams params, ObserverOnResultListener<GoodsInfoResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.GOODS_DETAIL_SHIPPER, params);
        handleOnResultObserver(mApiService.getGoodsInfoShipper(mCommonParams), listener, false);

    }

    /**
     * 司机版货源详情
     */
    public void getGoodsInfoDriver(GoodsIdParams params, ObserverOnResultListener<GoodsInfoResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.GOODS_DETAIL_DRIVER, params);
        handleOnResultObserver(mApiService.getGoodsInfoDriver(mCommonParams), listener, false);

    }

    /**
     * 货主版订单详情
     */
    public void getOrderInfoShipper(OrderIdParams params, ObserverOnResultListener<OrderInfoResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.ORDER_DETAIL_SHIPPER, params);
        handleOnResultObserver(mApiService.getOrderInfoShipper(mCommonParams), listener, false);

    }

    /**
     * 司机版订单详情
     */
    public void getOrderInfoDriver(OrderIdParams params, ObserverOnResultListener<OrderInfoResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.ORDER_DETAIL_DRIVER, params);
        handleOnResultObserver(mApiService.getOrderInfoDriver(mCommonParams), listener, false);

    }

    /**
     * 货主-撤销货源
     */
    public void cancelGoods(CancelGoodsParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.CANCEL_GOODS, params);
        handleOnResultObserver(mApiService.cancelGoods(mCommonParams), listener);

    }

    /**
     * 货主-刷新货源
     */
    public void refreshGoods(GoodsIdParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.REFRESH_GOODS, params);
        handleOnResultObserver(mApiService.refreshGoods(mCommonParams), listener);

    }

    /**
     * 司机-我的报价列表
     */
    public void myOfferList(GetMyOfferListParams params, ObserverOnNextListener<GetMyOfferListResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.MY_OFFER_LIST, params);
        handleOnNextObserver(mApiService.myOfferList(mCommonParams), listener, false);


    }

    /**
     * 货主-支付运费
     */
    public void paymentFreight(OrderIdParams params, ObserverOnNextListener<PaymentResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.PAYMENT_FREIGHT, params);
        handleOnNextObserver(mApiService.paymentFreight(mCommonParams), listener, false);

    }

    /**
     * 货主-查看报价列表
     */
    public void showOfferList(GetShowOfferListParams params, ObserverOnNextListener<GetOfferListResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.SHOW_PRE_OFFER_LIST, params);
        handleOnNextObserver(mApiService.showOfferList(mCommonParams), listener, false);

    }

    /**
     * 货主-确认成交
     */
    public void confirmDeal(GoodsIdParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.CONFIRM_DEAL, params);
        handleOnResultObserver(mApiService.confirmDeal(mCommonParams), listener);

    }

    /**
     * 货主-取消交易
     */
    public void cancelDeal(OrderIdParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.CANCEL_DEAL, params);
        handleOnResultObserver(mApiService.cancelDeal(mCommonParams), listener);
    }

    /**
     * 货主-发送提货码
     */
    public void sendPickupCode(OrderIdParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.SEND_PICK_UP_CODE, params);
        handleOnResultObserver(mApiService.sendPickupCode(mCommonParams), listener);
    }

    /**
     * 司机-撤销报价
     */
    public void cancelOffer(CancelOfferParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.CANCEL_OFFER, params);
        handleOnResultObserver(mApiService.cancelOffer(mCommonParams), listener);

    }

    /**
     * 司机-确认提货
     */
    public void confirmPickUpGoods(OrderIdParams params, ObserverOnResultListener<MsgResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.CONFIRM_PICK_UP_GOODS, params);
        handleOnResultObserver(mApiService.confirmPickUpGoods(mCommonParams), listener);

    }

    /**
     * 司机-确认签收
     */
    public void confirmReceivingDriver(OrderIdParams params, ObserverOnResultListener<MsgResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.CONFIRM_RECEIVING_DRIVRE, params);
        handleOnResultObserver(mApiService.confirmReceivingDriver(mCommonParams), listener);

    }

    /**
     * 货主-确认签收
     */
    public void confirmReceivingShipper(OrderIdParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.CONFIRM_RECEIVING_SHIPPER, params);
        handleOnResultObserver(mApiService.confirmReceivingShipper(mCommonParams), listener);

    }

    /**
     * 司机-查询该货源的有效报价数
     */
    public void selectOfferNum(GoodsIdParams params, ObserverOnNextListener<GetOfferNumResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.SELECT_OFFER_NUM, params);
        handleOnNextObserver(mApiService.selectOfferNum(mCommonParams), listener);

    }

    /**
     * 司机-确认承接
     */
    public void acceptOrder(OrderIdParams params, ObserverOnResultListener<OrderStateResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.ACCEPT_ORDER, params);
        handleOnResultObserver(mApiService.acceptOrder(mCommonParams), listener);

    }

    /**
     * 司机-放弃承接
     */
    public void giveUpOrder(OrderIdParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.GIVE_UP_ORDER, params);
        handleOnResultObserver(mApiService.giveUpOrder(mCommonParams), listener);

    }

    /**
     * 货主-通过货源ID查询订单ID
     */
    public void getOrderIdByGoodsId(GoodsIdParams params, ObserverOnResultListener<OrderIdResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.SELECT_ORDER_ID_BY_GOODS_ID, params);
        handleOnResultObserver(mApiService.getOrderIdByGoodsId(mCommonParams), listener);
    }

    /**
     * 合同信息
     */
    public void getProtocolInfo(GoodsIdParams params, ObserverOnNextListener<ProtocolResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.GET_PROTOCOL_INFO, params);
        handleOnNextObserver(mApiService.getProtocolInfo(mCommonParams), listener);
    }

    /**
     * 货主-查看过货源的司机列表
     */
    public void getShowGoodsList(GoodsIdParams params,
                                 ObserverOnResultListener<GetShowGoodsListResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.SHOW_GOODS_LIST, params);
        handleOnResultObserver(mApiService.getShowGoodsList(mCommonParams), listener, false);
    }

    /**
     * 货主-拨打电话记录列表
     */
    public void getCallPhoneList(GoodsIdParams params, ObserverOnResultListener<GetCallListResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.CALL_PHONE_LIST, params);
        handleOnResultObserver(mApiService.getCallPhoneList(mCommonParams), listener, false);
    }

    /**
     * 分享货源 多个，单个，传一个id就行
     *
     * @param params   参数
     * @param listener 监听
     */
    public void shareGoodsList(ShareGoodsParams params, ObserverOnResultListener<ShareGoodsResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.SHARE_GOODS_LIST, params);
        handleOnResultObserver(mApiService.shareGoodsList(mCommonParams), listener);
    }

    /**
     * 记录货源是否分享成功
     *
     * @param params   参数
     * @param listener 监听
     */
    public void recordShareGoods(ShareGoodsParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.RECORD_SHARE_GOODS, params);
        handleOnResultObserver(mApiService.recordShareGoods(mCommonParams), listener);
    }


    /**
     * 分享货源的列表
     *
     * @param params   参数
     * @param listener 监听
     */
    public void goodsListByShare(ShareGoodsParams params, ObserverOnNextListener<ShareGoodsListResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.GOODS_LIST_BY_SHARE, params);
        handleOnNextObserver(mApiService.goodsListByShare(mCommonParams), listener, false);
    }

    /**
     * 修改支付方式 定价
     *
     * @param params   参数
     * @param listener 监听
     */
    public void updatePayType(UpdatePayTypeParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.UPDATE_PAY_TYPE, params);
        handleOnResultObserver(mApiService.updatePayType(mCommonParams), listener);
    }

    /**
     * 修改支付方式 报价
     *
     * @param params   参数
     * @param listener 监听
     */
    public void updatePayTypeOrder(UpdatePayTypeParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.UPDATE_PAY_TYPE_ORDER, params);
        handleOnResultObserver(mApiService.updatePayTypeOrder(mCommonParams), listener);
    }

    /**
     * 删除订单
     */
    public void deleteOrder(GoodsIdParams params, ObserverOnResultListener<Object> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.DELETE_ORDER, params);
        handleOnResultObserver(mApiService.deleteOrder(mCommonParams), listener);
    }

    /**
     * 查询货源是否有效
     */
    public void selectValidGoods(GoodsIdParams params, ObserverOnResultListener<ValidGoodsResponse> listener) {
        mCommonParams = getParams(OrderDetailCommandApi.SELECT_VALID_GOODS, params);
        handleOnResultObserver(mApiService.selectValidGoods(mCommonParams), listener);
    }
}
