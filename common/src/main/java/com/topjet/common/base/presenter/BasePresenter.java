package com.topjet.common.base.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public class BasePresenter<V extends IView> implements IPresenter {
    public V mView;
    public Context mContext;
    public MvpActivity mActivity;
    public String TAG;

    public BasePresenter(V mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
        this.mActivity = (MvpActivity) mContext;
        this.TAG = mActivity.getClass().toString();
        initRxBus();
    }

    public BasePresenter(Context mContext) {
        this.mContext = mContext;
        this.mActivity = (MvpActivity) mContext;
        this.TAG = mActivity.getClass().toString();
        initRxBus();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        clearMemory();
        BusManager.getBus().unregister(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public Activity getmActivity() {
        return mActivity;
    }

    public void setmActivity(RxAppCompatActivity mActivity) {
        this.mActivity = (MvpActivity) mActivity;
    }

    /**
     * 为防止内存泄漏,需在页面销毁的时候清除对context,activity实例的引用,并且解除view的绑定
     */
    private void clearMemory() {
        this.mView = null;
        this.mContext = null;
        this.mActivity = null;
    }

    private void initRxBus() {
        BusManager.getBus().register(this);
    }

}