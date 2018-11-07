package com.topjet.common.order_detail.modle.js_interface;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.topjet.common.base.model.BaseJsInterface;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.order_detail.modle.bean.H5RefundInfo;
import com.topjet.common.utils.DataFormatFactory;

/**
 * Created by yy on 2017/10/12.
 * 退款JS接口
 */

public class RefundJSInterface extends BaseJsInterface {
    private H5RefundInfo refundInfo;

    public RefundJSInterface(MvpActivity mActivity, H5RefundInfo refundInfo, WebView webView) {
        super(mActivity, webView);
        this.refundInfo = refundInfo;
    }

    /**
     * 用于H5获取退款显示参数
     */
    @JavascriptInterface
    public String obtainOrderParameter() {
        return DataFormatFactory.toJson(refundInfo);
    }
}
