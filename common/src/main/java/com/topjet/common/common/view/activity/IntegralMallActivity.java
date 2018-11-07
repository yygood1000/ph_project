package com.topjet.common.common.view.activity;


import com.topjet.common.R;
import com.topjet.common.base.view.activity.BaseWebViewActivity;
import com.topjet.common.common.presenter.IntegralMallPresenter;
import com.topjet.common.order_detail.modle.js_interface.IntegralJSInterface;

/**
 * creator: zhulunjun
 * time:    2017/10/31
 * describe:积分商城
 */

public class IntegralMallActivity extends BaseWebViewActivity<IntegralMallPresenter> implements IntegerMallView {

    @Override
    public String getUrl() {
        return mPresenter.getUrl();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new IntegralMallPresenter(this, mContext);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common_webview;
    }

    @Override
    protected void initView() {
        super.initView();
        // 设置H5 交互接口
        mWebView.addJavascriptInterface(new IntegralJSInterface(this, mWebView), "App");
    }
}
