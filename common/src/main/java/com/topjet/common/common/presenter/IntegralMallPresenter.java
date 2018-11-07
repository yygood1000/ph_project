package com.topjet.common.common.presenter;

import android.content.Context;

import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.common.view.activity.IntegerMallView;

/**
 * Created by yy on 2017/10/12.
 * 积分商城相关页面
 */

public class IntegralMallPresenter extends BasePresenter<IntegerMallView> {
    public IntegralMallPresenter(IntegerMallView mView, Context mContext) {
        super(mView, mContext);
    }


    /**
     * 获取url
     * @return url
     */
    public String getUrl(){
        return H5ParamsProvider.getInstance().getMallUrl(mActivity);
    }


}
