package com.topjet.common.common.view.activity;

import com.topjet.common.R;
import com.topjet.common.base.view.activity.BaseWebViewActivity;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.common.presenter.MyIntegralPresenter;
import com.topjet.common.order_detail.modle.js_interface.IntegralJSInterface;

/**
 * Created by yy on 2017/10/30.
 * <p>
 * 我的积分
 */

public class MyIntegralActivity extends BaseWebViewActivity<MyIntegralPresenter> implements IView {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common_webview;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MyIntegralPresenter(this, mContext);
    }

    @Override
    protected void initView() {
        super.initView();
        // 设置H5 交互接口
        mWebView.addJavascriptInterface(new IntegralJSInterface(this, mWebView), "App");
    }

    @Override
    public String getUrl() {
        return "http://192.168.20.122:8087/";
    }
}
