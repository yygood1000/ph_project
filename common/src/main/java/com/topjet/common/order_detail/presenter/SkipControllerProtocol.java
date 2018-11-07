package com.topjet.common.order_detail.presenter;

import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.order_detail.modle.extra.ProtocolExtra;
import com.topjet.common.order_detail.modle.params.UpdatePayTypeParams;
import com.topjet.common.order_detail.view.activity.ProtocolActivity;

/**
 * Created by yy on 2017/9/25.
 * <p>
 * 跳转控制类
 * <p>
 * 跳转交易协议分5种情况
 * 1.货主版 点击确认成交，无支付明细弹窗，直接跳转交易协议
 * 2.货主版 点击确认成交，有支付明细弹窗，跳转交易协议
 * 3.货主版 点击修改支付方式,有支付明细弹窗，跳转交易协议
 * 4.司机版 点击我要承接，直接跳转交易协议
 * 5.货主版 点击发货，直接跳转交易协议，点击同意，发货
 * 跳转查看合同有1种情况
 * 1.司机/货主版 订单详情，点击查看合同
 */

public class SkipControllerProtocol {
    /**
     * 司机版 点击我要承接，直接跳转交易协议
     * 页面位于：司机版 报价列表
     */
    public static void skipAcceptOrder(MvpActivity mActivity, String goodsId, String tag) {
        skipToProtocol(mActivity,
                ProtocolExtra.TYPE_ACCEPT_ORDER, goodsId,
                "", "",
                "", null,
                "", "", "",
                "", "", "", tag);
    }

    /**
     * 货主版 点击确认成交，无支付明细弹窗，直接跳转交易协议
     * 页面位于：货主版，报价列表页面
     */
    public static void skipHasNotDialog(MvpActivity mActivity,
                                        String goodsId, String preGoodsId, String preGoodsVersion,
                                        String freight_fee, UpdatePayTypeParams params,
                                        String truckType, String truckLength, String driverPlateNumber,
                                        String driverName, String driverMobile, String driverIdCard, String tag) {

        skipToProtocol(mActivity,
                ProtocolExtra.TYPE_CONFIRM_HAS_NOT_DIALOG, goodsId,
                preGoodsId, preGoodsVersion,
                freight_fee, params,
                truckType, truckLength, driverPlateNumber,
                driverName, driverMobile, driverIdCard, tag);
    }

    /**
     * 货主版 点击确认成交，有支付明细弹窗，跳转交易协议
     * 页面位于：货主版，报价列表页面
     */
    public static void skipHasDialog(MvpActivity mActivity,
                                     String goodsId, String preGoodsId, String preGoodsVersion,
                                     String freight_fee, UpdatePayTypeParams params,
                                     String truckType, String truckLength, String driverPlateNumber,
                                     String driverName, String driverMobile, String driverIdCard, String tag) {

        skipToProtocol(mActivity,
                ProtocolExtra.TYPE_CONFIRM_HAS_DIALOG, goodsId,
                preGoodsId, preGoodsVersion,
                freight_fee, params,
                truckType, truckLength, driverPlateNumber,
                driverName, driverMobile, driverIdCard, tag);
    }

    /**
     * 货主版 点击修改支付方式,有支付明细弹窗，跳转交易协议
     * 页面位于：货主版，订单详情页面。
     */
    public static void skipAlterPayType(MvpActivity mActivity, String goodsId, UpdatePayTypeParams params, String tag) {
        skipToProtocol(mActivity,
                ProtocolExtra.TYPE_ALTER_PAY_TYPE, goodsId,
                "", "",
                "", params,
                "", "", "",
                "", "", "", tag);
    }

    /**
     * 查看合同,只需要传一个货源ID，所有数据请求接口获取
     */
    public static void skipCheckProtocol(MvpActivity mActivity, String goodsId) {
        skipToProtocol(mActivity,
                ProtocolExtra.TYPE_CHECK_PROTOCOL, goodsId,
                "", "",
                "", null,
                "", "", "",
                "", "", "", "");
    }

    /**
     * 发货界面进来的，查看合同,只需要传一个货源ID，所有数据请求接口获取
     * 货主版 点击发货，直接跳转交易协议
     * 页面位于：货主版，发货页面
     */
    public static void skipDeliverGoods(MvpActivity mActivity, String goodsId, String tag) {
        skipToProtocol(mActivity,
                ProtocolExtra.TYPE_DELIVER_GOODS, goodsId,
                "", "",
                "", null,
                "", "", "",
                "", "", "", tag);
    }


    /**
     * 总的跳转方法
     *
     * @param type              页面入口
     * @param goodsId           货源Id
     * @param preGoodsId        报价单id
     * @param preGoodsVersion   报价单version
     * @param freight_fee       总运费
     * @param params            修改支付方式的参数
     * @param truckType         车型
     * @param truckLength       车长
     * @param driverPlateNumber 车牌
     * @param driverName        司机姓名
     * @param driverMobile      司机电话
     * @param driverIdCard      司机身份证
     */
    private static void skipToProtocol(
            MvpActivity mActivity,
            int type, String goodsId,
            String preGoodsId, String preGoodsVersion,
            String freight_fee, UpdatePayTypeParams params,
            String truckType, String truckLength, String driverPlateNumber,
            String driverName, String driverMobile, String driverIdCard, String tag) {

        ProtocolExtra extra = new ProtocolExtra();
        extra.setPageType(type);
        extra.setGoods_id(goodsId);
        extra.setPreGoodsId(preGoodsId);
        extra.setPreGoodsVersion(preGoodsVersion);
        extra.setFreight_fee(freight_fee);
        extra.setPayTypeParams(params);
        extra.setTruck_type(truckType);
        extra.setTruck_length(truckLength);
        extra.setDriver_license_plate_number(driverPlateNumber);
        extra.setDriver_name(driverName);
        extra.setDriver_mobile(driverMobile);
        extra.setDriver_id_card(driverIdCard);
        extra.setTag(tag);

        mActivity.turnToActivity(ProtocolActivity.class, extra);
    }


      /* ==========================交易协议 跳转相关end==========================*/
}
