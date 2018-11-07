package com.topjet.common.base.presenter;

import android.content.Context;

import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.base.view.fragment.RxFragment;

/**
 * creator: zhulunjun
 * time:    2017/9/5
 * describe: 带api请求的控制类
 */

public abstract class BaseFragmentApiPresenter<V extends IView, T extends BaseCommand> extends BaseFragmentPresenter<V> {
    protected T mApiCommand;

    public BaseFragmentApiPresenter(V mView, Context mContext, RxFragment mFragment, T mApiCommand) {
        super(mView, mContext, mFragment);
        this.mApiCommand = mApiCommand;
    }

    public BaseFragmentApiPresenter(V mView, Context mContext, RxFragment mFragment) {
        super(mView, mContext, mFragment);
    }

}
