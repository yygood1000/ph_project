package com.topjet.common.common.view.activity;


import com.topjet.common.R;
import com.topjet.common.base.model.BaseJsInterface;
import com.topjet.common.base.view.activity.BaseWebViewActivity;
import com.topjet.common.common.presenter.IntegralMallPresenter;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.Config;
import com.topjet.common.utils.SystemUtils;

/**
 * creator: yy
 */

public class HelpCenterActivity extends BaseWebViewActivity<IntegralMallPresenter> implements IntegerMallView {

    @Override
    protected void initPresenter() {
        mPresenter = new IntegralMallPresenter(this, mContext);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common_webview;
    }

    @Override
    public String getUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(Config.getHelpCenterUrl());
        // 拼接APP类型,注：司机：2 货主：1(与3.0接口相反)
        if (CMemoryData.isDriver()) {
            builder.append("?apptype=2");
        } else {
            builder.append("?apptype=1");
        }
        // 拼接APP版本
        builder.append("&version=")
                .append(SystemUtils.getVersionName(this));
        return builder.toString();
    }

    @Override
    protected void initView() {
        super.initView();
        // 设置H5 交互接口
        mWebView.addJavascriptInterface(new BaseJsInterface(this,mWebView), "App");
    }
}
