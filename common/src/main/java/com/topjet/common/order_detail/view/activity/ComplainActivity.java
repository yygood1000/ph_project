package com.topjet.common.order_detail.view.activity;

import com.topjet.common.R;
import com.topjet.common.base.model.BaseJsInterface;
import com.topjet.common.base.view.activity.BaseWebViewActivity;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.order_detail.modle.extra.ComplainExtra;
import com.topjet.common.order_detail.presenter.ComplainPresenter;
import com.topjet.common.utils.Logger;

/**
 * Created by yy on 2017/11/7.
 * 我要投诉
 */

public class ComplainActivity extends BaseWebViewActivity<ComplainPresenter> implements IView {
    @Override
    protected void initPresenter() {
        mPresenter = new ComplainPresenter(mContext);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common_webview;
    }

    @Override
    protected void initView() {
        super.initView();
        // 设置H5 交互接口
        mWebView.addJavascriptInterface(new BaseJsInterface(this,mWebView), "App");
    }

    @Override
    public String getUrl() {
        Logger.i("oye", "url == " + mPresenter.getComplainUrl());
        return mPresenter.getComplainUrl();
    }

    public static void turnToComplainActiivty(MvpActivity mActivity, String orderId, String orderVersion) {
        ComplainExtra extra = new ComplainExtra(orderId, orderVersion);
        mActivity.turnToActivity(ComplainActivity.class, extra);
    }
}
