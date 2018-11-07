package com.topjet.common.common.view.activity;


import com.topjet.common.R;
import com.topjet.common.base.model.BaseJsInterface;
import com.topjet.common.base.view.activity.BaseWebViewActivity;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.extra.SimpleWebViewExtra;
import com.topjet.common.common.presenter.SimpleWebViewPresenter;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.MyTitleBar;

/**
 * creator: yy
 * describe:只有一个WebView的Activity
 */

public class SimpleWebViewActivity extends BaseWebViewActivity<SimpleWebViewPresenter> implements IView {

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SimpleWebViewPresenter(this, mContext);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common_webview;
    }

    @Override
    public String getUrl() {
        return mPresenter.extra.getUrl();
    }

    @Override
    protected void initView() {
        super.initView();
        // 设置H5 交互接口
        mWebView.addJavascriptInterface(new BaseJsInterface(this,mWebView), "App");
        // 设置页面标题
        if (StringUtils.isNotBlank(mPresenter.extra.getTitle())) {
            getMyTitleBar().setTitleText(mPresenter.extra.getTitle());
        }
    }

    public static void turnToSimpleWebViewActivity(MvpActivity mActivity, String url) {
        SimpleWebViewExtra extra = new SimpleWebViewExtra(url);
        mActivity.turnToActivity(SimpleWebViewActivity.class, extra);
    }

    public static void turnToSimpleWebViewActivity(MvpActivity mActivity, String url, String title) {
        SimpleWebViewExtra extra = new SimpleWebViewExtra(url, title);
        mActivity.turnToActivity(SimpleWebViewActivity.class, extra);
    }

    public static void turnToSimpleWebViewActivity(MvpActivity mActivity, String url, String title, boolean
            isHideTitle) {
        SimpleWebViewExtra extra = new SimpleWebViewExtra(url, title, isHideTitle);
        mActivity.turnToActivity(SimpleWebViewActivity.class, extra);
    }

}
