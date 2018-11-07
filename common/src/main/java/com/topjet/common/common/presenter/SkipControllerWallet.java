package com.topjet.common.common.presenter;

import android.app.Activity;
import android.app.Application;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.params.GetPayForInfoParams;
import com.topjet.common.common.modle.response.GetPayForInfoResponse;
import com.topjet.common.common.modle.serverAPI.PayForCommand;
import com.topjet.common.common.modle.serverAPI.PayForCommandAPI;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.order_detail.modle.params.OrderIdParams;
import com.topjet.common.order_detail.modle.response.PaymentResponse;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;
import com.topjet.common.utils.Logger;
import com.topjet.wallet.WalletApp;
import com.topjet.wallet.config.WalletCMemoryData;
import com.topjet.wallet.model.BridgingCenter;
import com.topjet.wallet.model.WalletLoginResult;
import com.topjet.wallet.model.WalletPayOrderInfo;

/**
 * Created by yy on 2017/9/27.
 * <p>
 * 钱包跳转工具类
 */

public class SkipControllerWallet {

    public static String PAY_FOR_FREIGHT = "1";
    public static String PAY_FOR_DEPOSIT = "2";

    /**
     * 支付的回调
     */
    public interface OnPayForResultListener {
        void onSucceed();
    }

    /**
     * 跳转钱包首页
     */
    public static void skipToWalletMain(Activity activity) {
        WalletLoginResult loginResult = new WalletLoginResult();
        loginResult.setUserId(CMemoryData.getUserId());
        loginResult.setMoblie(CMemoryData.getUserMobile());
        new BridgingCenter().OutJumWallet(activity, loginResult);
    }

    /**
     * 跳转账单列表
     */
    public static void skipToBillList(Activity activity, String userId) {
        new BridgingCenter().OrderListJumpWallet(activity, userId);
    }

    /**
     * 支付  运费 接口（获取账单号）
     */
    public static void paymentFreight(final MvpActivity mActivity, String orderId, String orderVersion,
                                      final OnPayForResultListener mListener) {
        OrderIdParams params = new OrderIdParams(orderId, orderVersion);
        params.setGpsInfo();

        new OrderDetailCommand(OrderDetailCommandApi.class, mActivity)
                .paymentFreight(params, new ObserverOnNextListener<PaymentResponse>() {
                    @Override
                    public void onNext(PaymentResponse response) {
                        // 获取运费的支付信息
                        getPayForInfo(mActivity, PAY_FOR_FREIGHT, response.getTotal_bill_no(), false,
                                mListener);
                    }

                    @Override
                    public void onError(String errorCode, String msg) {
                        mActivity.cancelShowLoadingDialog();
                        mActivity.showToast("获取账单号失败，请重试");
                    }
                });
    }

    /**
     * 获取支付信息接口调用
     * <p>
     * 该方法会在调用支付运费接口/带定金报价成功后调用
     *
     * @param mActivity      支付页面的Activity，需要请求服务端，所以需要传入至少是RxActivity
     * @param billNo         账单号
     * @param isShowProgress 支付运费时传入false，支付定金时
     */
    public static void getPayForInfo(final MvpActivity mActivity, final String payType, final String billNo, boolean
            isShowProgress, final OnPayForResultListener mListener) {
        // 请求服务端接口，获取跳转钱包所需要的信息。
        GetPayForInfoParams params = new GetPayForInfoParams(billNo);
        new PayForCommand(PayForCommandAPI.class, mActivity)
                .getPayForInfo(params, isShowProgress, new ObserverOnNextListener<GetPayForInfoResponse>() {
                    @Override
                    public void onNext(GetPayForInfoResponse response) {
                        // 关闭加载框
                        mActivity.cancelShowLoadingDialog();
                        response.setType(payType);// 设置支付类型
                        // 跳转到支付页面
                        jumpToPayFor(mActivity, billNo, response, mListener);
                    }

                    @Override
                    public void onError(String errorCode, String msg) {
                        // 获取支付信息失败
                        mActivity.cancelShowLoadingDialog();
                        mActivity.showToast("获取支付信息失败，请重试");
                    }
                });
    }

    /**
     * 跳转钱包支付页面的方法
     *
     * @param billNo    账单号
     * @param mListener 支付回调监听
     */
    private static void jumpToPayFor(final MvpActivity mActivity, String billNo, final GetPayForInfoResponse
            response, final OnPayForResultListener mListener) {
        WalletPayOrderInfo orderinfo = new WalletPayOrderInfo();
        orderinfo.setBusinessOrderId(billNo);
        orderinfo.setType(response.getType());
        orderinfo.setTransactionType(response.getTransactionType());
        orderinfo.setOrderMoney(response.getOrderMoney());
        orderinfo.setReMark(response.getReMark());
        orderinfo.setTransportOrderId(response.getTransportOrderId());
        orderinfo.setSplitinfo(response.getSplitinfo());
        orderinfo.setOutUserId(CPersisData.getUserID());
        orderinfo.setCreateTime(response.getCreateTime());
        orderinfo.setInUserId(response.getInUserId());
        orderinfo.setHeadImgUrl(response.getInUserIconUrl());
        orderinfo.setHeadImgKey(response.getInUserIconKey());
        orderinfo.setUseName(response.getInUserName());
        orderinfo.setUseMobile(response.getInUserMobile());
        orderinfo.setSex(response.getSex());
        orderinfo.setIsAnonymous(response.getIsAnonymous());
        Logger.i("oye", "orderinfo =====" + orderinfo.toString());
        BridgingCenter bridgingCenter = new BridgingCenter();
        bridgingCenter.PayOrderJumpWallet(mActivity, CPersisData.getUserID(), orderinfo);
        bridgingCenter.setInterFace(new BridgingCenter.MyInterface() {
            @Override
            public void isSuccCallback(int type) {
                //支付成功回调type：1成功 其他失败
                if (mListener != null) {
                    if (type == 1) {
                        mListener.onSucceed();
                    } else {
                        mActivity.showToast("钱包支付失败，请重新支付");
                    }
                }
            }
        });
    }


    /**
     * 积分商城跳转钱包支付
     *
     * @param mListener 支付回调监听
     */
    public static void jumpToPayForMall(final MvpActivity mActivity, final WalletPayOrderInfo
            orderinfo, final OnPayForResultListener mListener) {
        Logger.i("oye", "orderinfo =====" + orderinfo.toString());
        BridgingCenter bridgingCenter = new BridgingCenter();
        bridgingCenter.PayOrderJumpWallet(mActivity, CPersisData.getUserID(), orderinfo);
        bridgingCenter.setInterFace(new BridgingCenter.MyInterface() {
            @Override
            public void isSuccCallback(int type) {
                //支付成功回调type：1成功 其他失败
                if (mListener != null) {
                    if (type == 1) {
                        mListener.onSucceed();
                    } else {
                        mActivity.showToast("钱包支付失败，请重新支付");
                    }
                }
            }
        });
    }

    /**
     * 初始化钱包
     */
    public static void initWallet(Application app) {
        WalletApp.init(app);
        com.topjet.wallet.utils.Toaster.init();
        WalletCMemoryData.setDriver(CMemoryData.isDriver());
    }

}
