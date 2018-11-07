package com.topjet.common.order_detail.view.activity;

import com.topjet.common.R;
import com.topjet.common.base.view.activity.BaseWebViewActivity;
import com.topjet.common.config.Config;
import com.topjet.common.order_detail.modle.js_interface.RefundJSInterface;
import com.topjet.common.order_detail.presenter.RefundPresenter;

/**
 * Created by yy on 2017/10/12.
 * <p>
 * 退款页面
 */

public class RefundActivity extends BaseWebViewActivity<RefundPresenter> implements RefundView {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common_webview;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new RefundPresenter(this, mContext);
    }

    @Override
    protected void initView() {
        super.initView();
        // 设置H5 交互接口
        mWebView.addJavascriptInterface(new RefundJSInterface(this, mPresenter.refundInfo, mWebView), "App");
    }

    @Override
    public String getUrl() {
        return Config.getRefundUrl();
    }
}
