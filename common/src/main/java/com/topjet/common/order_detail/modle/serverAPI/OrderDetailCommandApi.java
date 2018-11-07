package com.topjet.common.order_detail.modle.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
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

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * creator: zhulunjun
 * time:    2017/9/5
 * describe: 订单相关接口
 */

public interface OrderDetailCommandApi {

    // 货主

    /**
     * 货主版货源详情
     */
    String GOODS_DETAIL_SHIPPER = "ownergoods.ownergoodsinfo";

    @POST("order-service/ownergoods/ownergoodsinfo")
    Observable<BaseResponse<GoodsInfoResponse>> getGoodsInfoShipper(@Body CommonParams commonParams);


    /**
     * 货主版订单详情
     */
    String ORDER_DETAIL_SHIPPER = "ownergoods.ownerorderinfo";

    @POST("order-service/ownergoods/ownerorderinfo")
    Observable<BaseResponse<OrderInfoResponse>> getOrderInfoShipper(@Body CommonParams commonParams);


    /**
     * 货主-撤销货源
     */
    String CANCEL_GOODS = "ownergoods.cancelgoods";

    @POST("order-service/ownergoods/cancelgoods")
    Observable<BaseResponse<Object>> cancelGoods(@Body CommonParams commonParams);

    /**
     * 货主-刷新货源
     */
    String REFRESH_GOODS = "ownergoods.flushgoods";

    @POST("order-service/ownergoods/flushgoods")
    Observable<BaseResponse<Object>> refreshGoods(@Body CommonParams commonParams);

    /**
     * 货主-支付运费
     */
    String PAYMENT_FREIGHT = "bill.paytransportfee";

    @POST("order-service/bill/paytransportfee")
    Observable<BaseResponse<PaymentResponse>> paymentFreight(@Body CommonParams commonParams);

    /**
     * 货主-查看报价列表
     */
    String SHOW_PRE_OFFER_LIST = "ownerpregoods.pregoodslist";

    @POST("order-service/ownerpregoods/pregoodslist")
    Observable<BaseResponse<GetOfferListResponse>> showOfferList(@Body CommonParams commonParams);

    /**
     * 货主-确认成交
     */
    String CONFIRM_DEAL = "ownerpregoods.affirmdeal";

    @POST("order-service/ownerpregoods/affirmdeal")
    Observable<BaseResponse<Object>> confirmDeal(@Body CommonParams commonParams);

    /**
     * 货主-取消交易
     */
    String CANCEL_DEAL = "ownerpregoods.canceldeal";

    @POST("order-service/ownerpregoods/canceldeal")
    Observable<BaseResponse<Object>> cancelDeal(@Body CommonParams commonParams);

    /**
     * 货主-确认签收
     */
    String CONFIRM_RECEIVING_SHIPPER = "bill.confirmreceivingbyowner";

    @POST("order-service/bill/confirmreceivingbyowner")
    Observable<BaseResponse<Object>> confirmReceivingShipper(@Body CommonParams commonParams);


    // 司机

    /**
     * 司机版货源详情
     */
    String GOODS_DETAIL_DRIVER = "driverorder.drivergoodsinfo";

    @POST("order-service/driverorder/drivergoodsinfo")
    Observable<BaseResponse<GoodsInfoResponse>> getGoodsInfoDriver(@Body CommonParams commonParams);

    /**
     * 司机版订单详情
     */
    String ORDER_DETAIL_DRIVER = "driverorder.driverorderinfo";

    @POST("order-service/driverorder/driverorderinfo")
    Observable<BaseResponse<OrderInfoResponse>> getOrderInfoDriver(@Body CommonParams commonParams);


    /**
     * 司机-我的报价列表
     */
    String MY_OFFER_LIST = "pregoods.drvierofferlist";

    @POST("order-service/pregoods/drvierofferlist")
    Observable<BaseResponse<GetMyOfferListResponse>> myOfferList(@Body CommonParams commonParams);

    /**
     * 司机-撤销报价
     */
    String CANCEL_OFFER = "pregoods.drvierundooffer";

    @POST("order-service/pregoods/drvierundooffer")
    Observable<BaseResponse<Object>> cancelOffer(@Body CommonParams commonParams);

    /**
     * 司机-确认提货
     */
    String CONFIRM_PICK_UP_GOODS = "bill.surepickupgoods";

    @POST("order-service/bill/surepickupgoods")
    Observable<BaseResponse<MsgResponse>> confirmPickUpGoods(@Body CommonParams commonParams);

    /**
     * 司机-确认签收
     */
    String CONFIRM_RECEIVING_DRIVRE = "bill.confirmreceivingbydriver";

    @POST("order-service/bill/confirmreceivingbydriver")
    Observable<BaseResponse<MsgResponse>> confirmReceivingDriver(@Body CommonParams commonParams);


    /**
     * 司机-查询该货源的有效报价数
     */
    String SELECT_OFFER_NUM = "drivergoods.selectcountpregoodsvalid";

    @POST("order-service/drivergoods/selectcountpregoodsvalid")
    Observable<BaseResponse<GetOfferNumResponse>> selectOfferNum(@Body CommonParams commonParams);


    /**
     * 司机-确认承接
     */
    String ACCEPT_ORDER = "driverorder.acceptorder";

    @POST("order-service/driverorder/acceptorder")
    Observable<BaseResponse<OrderStateResponse>> acceptOrder(@Body CommonParams commonParams);

    /**
     * 司机-放弃承接
     */
    String GIVE_UP_ORDER = "driverorder.giveuporder";

    @POST("order-service/driverorder/giveuporder")
    Observable<BaseResponse<Object>> giveUpOrder(@Body CommonParams commonParams);

    /**
     * 货主-发送提货码
     */
    String SEND_PICK_UP_CODE = "ownerorder.sendpickupcode";

    @POST("order-service/ownerorder/sendpickupcode")
    Observable<BaseResponse<Object>> sendPickupCode(@Body CommonParams commonParams);

    /**
     * 货主-通过货源ID查询订单ID
     */
    String SELECT_ORDER_ID_BY_GOODS_ID = "ownerorder.selectorderidbygoodsid";

    @POST("order-service/ownerorder/selectorderidbygoodsid")
    Observable<BaseResponse<OrderIdResponse>> getOrderIdByGoodsId(@Body CommonParams commonParams);

    /**
     * 合同信息
     */
    String GET_PROTOCOL_INFO = "publicgoods.contractinfo";

    @POST("order-service/publicgoods/contractinfo")
    Observable<BaseResponse<ProtocolResponse>> getProtocolInfo(@Body CommonParams commonParams);

    /**
     * 货主-拨打电话记录列表
     */
    String CALL_PHONE_LIST = "userorder.callphonelist";

    @POST("user-service/userorder/callphonelist")
    Observable<BaseResponse<GetCallListResponse>> getCallPhoneList(@Body CommonParams commonParams);

    /**
     * 货主-查看过货源的司机列表
     */
    String SHOW_GOODS_LIST = "goodsshowinfocontroll.checkonsupplyofgoods";

    @POST("order-service/goodsshowinfocontroll/checkonsupplyofgoods")
    Observable<BaseResponse<GetShowGoodsListResponse>> getShowGoodsList(@Body CommonParams commonParams);


    /**
     * 分享货源，多个
     */
    String SHARE_GOODS_LIST = "publicgoods.sharegoodslist";

    @POST("order-service/publicgoods/sharegoodslist")
    Observable<BaseResponse<ShareGoodsResponse>> shareGoodsList(@Body CommonParams<ShareGoodsParams> commonParams);

    /**
     * 分享货源是否成功，记录
     */
    String RECORD_SHARE_GOODS = "publicgoods.recordsharegoods";

    @POST("order-service/publicgoods/recordsharegoods")
    Observable<BaseResponse<Object>> recordShareGoods(@Body CommonParams<ShareGoodsParams> commonParams);

    /**
     * 分享货源的列表
     */
    String GOODS_LIST_BY_SHARE = "publicgoods.sharegoodslistbyownerid";

    @POST("order-service/publicgoods/sharegoodslistbyownerid")
    Observable<BaseResponse<ShareGoodsListResponse>> goodsListByShare(@Body CommonParams<ShareGoodsParams> commonParams);

    /**
     * 货主-修改支付方式(定金流程)
     */
    String UPDATE_PAY_TYPE = "ownerorder.updatepay";

    @POST("order-service/ownerorder/updatepay")
    Observable<BaseResponse<Object>> updatePayType(@Body CommonParams<UpdatePayTypeParams> commonParams);

    /**
     * 货主-修改支付方式(报价流程)
     */
    String UPDATE_PAY_TYPE_ORDER = "ownerorder.updatepaypreorder";

    @POST("order-service/ownerorder/updatepaypreorder")
    Observable<BaseResponse<Object>> updatePayTypeOrder(@Body CommonParams<UpdatePayTypeParams> commonParams);


    /**
     * 货主-删除订单
     */
    String DELETE_ORDER = "ownerorder.deletedorder";

    @POST("order-service/ownerorder/deletedorder")
    Observable<BaseResponse<Object>> deleteOrder(@Body CommonParams<OrderIdParams> commonParams);

    /**
     * 查询货源是否有效
     */
    String SELECT_VALID_GOODS = "usercenter.selectvalidgoods";

    @POST("order-service/usercenter/selectvalidgoods")
    Observable<BaseResponse<ValidGoodsResponse>> selectValidGoods(@Body CommonParams<GoodsIdParams> commonParams);
}
