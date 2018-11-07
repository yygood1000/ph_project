package com.topjet.common.common.presenter;

import android.content.Context;

import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.common.modle.extra.SimpleWebViewExtra;
import com.topjet.common.common.view.activity.SimpleWebViewActivity;

/**
 * Created by yy on 2017/10/12.
 * WebView公共跳转页面相关
 */

public class SimpleWebViewPresenter extends BasePresenter<IView> {
    public SimpleWebViewExtra extra;

    public SimpleWebViewPresenter(IView mView, Context mContext) {
        super(mView, mContext);
    }

    @Override
    public void onCreate() {
        extra = (SimpleWebViewExtra) mActivity.getIntentExtra(SimpleWebViewExtra.getExtraName());
        ((SimpleWebViewActivity) mActivity).setHideTitleBar(extra.isHideTitle());
    }
}
