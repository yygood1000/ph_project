package com.topjet.common.base.presenter;

import android.content.Context;

import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.IView;

/**
 * creator: zhulunjun
 * time:    2017/9/5
 * describe: 带api请求的控制类
 */

public abstract class BaseApiPresenter<V extends IView, T extends BaseCommand> extends BasePresenter<V> {

    protected T mApiCommand;

    public BaseApiPresenter(V mView, Context mContext, T mApiCommand) {
        super(mView, mContext);
        this.mApiCommand = mApiCommand;
    }

    public BaseApiPresenter(V mView, Context mContext) {
        super(mView, mContext);
    }
}
