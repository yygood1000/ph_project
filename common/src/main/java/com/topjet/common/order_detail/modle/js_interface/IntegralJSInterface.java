package com.topjet.common.order_detail.modle.js_interface;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.amap.api.location.AMapLocation;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.model.BaseJsInterface;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.extra.TabIndex;
import com.topjet.common.common.presenter.SkipControllerWallet;
import com.topjet.common.common.view.activity.GuideActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.user.modle.extra.GoToAuthenticationExtra;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.Toaster;
import com.topjet.wallet.model.WalletPayOrderInfo;


/**
 * Created by yy on 2017/10/12.
 * 积分JS接口
 */

public class IntegralJSInterface extends BaseJsInterface {
    private WebView mWebView;

    public IntegralJSInterface(MvpActivity mActivity, WebView mWebView) {
        super(mActivity, mWebView);
        this.mActivity = mActivity;
        this.mWebView = mWebView;
    }

    /**
     * 调用js的方法
     */
    public void loadUrl(final String url) {
        if (mWebView != null) {
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(url);
                }
            });
        }
    }

    /**
     * 客户端判断
     */
    @JavascriptInterface
    public boolean hyg_isapp() {
        return true;
    }

    /**
     * 钱包支付（积分商城）
     * info返回值:
     * 1.@"comment" ,支付账单的描述
     * 2@“billno”  ，支付账单号
     * 3@“tradername”， 支付对象名称
     * 4@“tradericonUrl”， 支付对象头像
     * 5@“payamount”， 支付金额
     * 6 @“remark”， 支付备注
     * 7@“ownerMobile”， 用户手机号
     * 8@“”， shopid
     */
    @JavascriptInterface
    public void scoreMallPay(final String[] info) {
        String serialNo = "OUT" + info[1];
        WalletPayOrderInfo params = getWalletParams(info[1], info[4], info[5], info[0], serialNo);
        SkipControllerWallet.jumpToPayForMall(mActivity, params, new SkipControllerWallet.OnPayForResultListener() {
            @Override
            public void onSucceed() {
                loadUrl("javascript:exchangeSuc('" + info[7] + "','" + info[4] + "')");
            }
        });
    }

    /**
     * 获取跳转钱包的参数
     *
     * @param billNo   支付订单号
     * @param money    金额
     * @param reMark   备注
     * @param comment  描述
     * @param serialNo 运单号（不能为空所以必须得有参数）
     * @return 请求体
     */
    public WalletPayOrderInfo getWalletParams(String billNo, String money, String reMark,
                                              String comment, String serialNo) {
        WalletPayOrderInfo params = new WalletPayOrderInfo();
        params.setBusinessOrderId(billNo);
        params.setType("200");
        params.setOrderMoney(money);
        params.setTransactionType("1");
        params.setComment(comment);
        params.setTransportOrderId(serialNo);
        params.setReMark(reMark);
        params.setOutUserId(CMemoryData.getUserId());
        return params;
    }

    /**
     * 位置信息
     */
    @JavascriptInterface
    public void getLocation() {
        getNowLocation();
    }

    /**
     * 获取位置信息
     */

    private void getNowLocation() {

        LocationUtils.location(mActivity, new LocationUtils.OnLocationListener() {
            @Override
            public void onLocated(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == LocationUtils.LOCATE_SUCCEED) {
                    String addressDetail = aMapLocation.getAddress();
                    String lat = aMapLocation.getLatitude() + "";
                    String lng = aMapLocation.getLongitude() + "";
                    final String s = "javascript:returnLocation('" + lng + "','" + lat + "','" + addressDetail + "'," +
                            "'" + addressDetail + "')";
                    Logger.i("TTT", "getNowLocation:::" + s);
                    loadUrl(s);
                }
            }

            @Override
            public void onLocationPermissionfaild() {
                Toaster.showLongToast("获取位置信息失败");
            }
        });

    }


    /**
     * 2.4版本新增js交互方法
     */

    //------------------------------发货版-----------------------------

    /**
     * 跳转我要发货
     */
    @JavascriptInterface
    public void postGoods() throws ClassNotFoundException {
        CommonProvider.getInstance().getJumpShipperProvider().jumpDeliverGoods(mActivity);
    }

    /**
     * 跳转我的订单（已签收）发货版
     */
    @JavascriptInterface
    public void callShipperMyOrderPage() throws ClassNotFoundException {
        CommonProvider.getInstance()
                .getJumpShipperProvider()
                .jumpMain(mActivity, TabIndex.MY_ORDER_PAGE, TabIndex.HISTROY_ORDER);
    }

    //--------------------------------接货版--------------------------------

    /**
     * 跳转我的订单（已签收）接货版
     */
    @JavascriptInterface
    public void callDriverMyOrderPage() throws ClassNotFoundException {
        CommonProvider.getInstance()
                .getJumpDriverProvider().jumpMain(mActivity, TabIndex.MY_ORDER_PAGE, TabIndex.SING);
    }

    /**
     * 跳转智能找货 接货版
     */
    @JavascriptInterface
    public void goSmartGoods() throws ClassNotFoundException {
        CommonProvider.getInstance().getJumpDriverProvider().jumpMain(mActivity, TabIndex.SMART_SEARCH_GOODS_PAGE);
    }

    /**
     * 跳转车辆认证
     */
    @JavascriptInterface
    public void carCertification() throws ClassNotFoundException {
        CommonProvider.getInstance().getJumpDriverProvider().jumpCarAuthentication(mActivity, GoToAuthenticationExtra
                .IN_TYPE_ADD);
    }

    //-----------------------------------通用方法----------------------------------------------

    /**
     * 跳转实名认证
     */
    @JavascriptInterface
    public void authorCertification() throws ClassNotFoundException {
        if (CMemoryData.isDriver()) {
            CommonProvider.getInstance().getJumpDriverProvider().jumpRealNameAuthentication(mActivity,
                    GoToAuthenticationExtra.IN_TYPE_ADD);
        } else {
            CommonProvider.getInstance().getJumpShipperProvider().jumpRealNameAuthentication(mActivity,
                    GoToAuthenticationExtra.IN_TYPE_ADD);
        }
    }

    /**
     * 跳转身份认证
     */
    @JavascriptInterface
    public void identityCertification() throws ClassNotFoundException {
        if (CMemoryData.isDriver()) {
            CommonProvider.getInstance().getJumpDriverProvider().jumpIdentityAuthentication(mActivity);
        } else {
            CommonProvider.getInstance().getJumpShipperProvider().jumpIdentityAuthentication(mActivity);
        }
    }

    /**
     * 跳转新手引导
     */
    @JavascriptInterface
    public void userHelper() throws ClassNotFoundException {
        mActivity.turnToActivity(GuideActivity.class);
    }


}
