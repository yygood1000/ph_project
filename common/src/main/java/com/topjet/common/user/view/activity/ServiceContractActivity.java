package com.topjet.common.user.view.activity;


import com.topjet.common.R;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.MyWebView;

/**
 * 注册界面 服务协议页面
 */
public class ServiceContractActivity extends MvpActivity implements IView{

    private MyWebView webView;

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText("服务协议");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new BasePresenter(this,this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_service_contract;
    }


    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        webView = (MyWebView) findViewById(R.id.webview);
        initWebView();
    }


    private void initWebView() {
        webView.loadUrl("file:///android_asset/ym3.html");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        webView.goBack();
    }
}