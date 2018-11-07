package com.topjet.common.order_detail.modle.js_interface;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.topjet.common.base.model.BaseJsInterface;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.order_detail.view.activity.ProtocolActivity;

/**
 * Created by yy on 2017/10/12.
 * 协议JS接口
 */

public class ProtocolJSInterface extends BaseJsInterface {

    public ProtocolJSInterface(MvpActivity mActivity, WebView webView) {
        super(mActivity, webView);
    }

    /**
     * 设置承接车辆
     */
    @JavascriptInterface
    public void setTruckId(final String truckId) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ProtocolActivity) mActivity).setTruckId(truckId);
            }
        });
    }
}
