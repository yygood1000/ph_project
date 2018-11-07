package com.topjet.common.common.modle.js_interface;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.model.BaseJsInterface;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CMemoryData;

/**
 * Created by yy on 2017/10/12.
 * 退款JS接口
 */

public class CommentJSInterface extends BaseJsInterface {

    public CommentJSInterface(MvpActivity mActivity, WebView webView) {
        super(mActivity, webView);
    }

    /**
     * 评论列表页 跳转订单详情页面
     */
    @JavascriptInterface
    public void turnToOrderDetail(final String orderId) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (CMemoryData.isDriver()) {
                    CommonProvider.getInstance().getJumpDriverProvider().jumpOrderDetail(mActivity, orderId);
                } else {
                    CommonProvider.getInstance().getJumpShipperProvider().jumpOrderDetail(mActivity, orderId);
                }
            }
        });
    }
}
