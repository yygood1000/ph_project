package com.topjet.common.base.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.topjet.common.base.view.activity.IView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.base.view.fragment.RxFragment;


/**
 * Created by yy on 2017/8/1
 * <p>
 * Fragment 的基类 Presenter
 */

public class BaseFragmentPresenter<V extends IView> implements IFragmentPresenter {
    public V mView;//fragment view
    public Context mContext;// 上下文 在BaseMvpFragment.onActivityCreated()中赋值了。
    public RxFragment mFragment;
    public MvpActivity mActivity;

    public BaseFragmentPresenter(V mView, Context mContext, RxFragment mFragment) {
        this.mView = mView;
        this.mContext = mContext;
        this.mActivity = (MvpActivity) mContext;
        this.mFragment = mFragment;
    }

    public BaseFragmentPresenter(Context mContext, RxFragment mFragment) {
        this.mContext = mContext;
        this.mActivity = (MvpActivity) mContext;
        this.mFragment = mFragment;
    }
    public void init(V view) {
        this.mView = view;
    }


    public void init(V view, Activity activity, RxFragment fragment) {
        this.mView = view;
        this.mActivity = (MvpActivity) activity;
        this.mContext = activity;
        this.mFragment = fragment;
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
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

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

    public void setmActivity(Activity mActivity) {
        this.mActivity = (MvpActivity) mActivity;
    }

    /**
     * 为防止内存泄漏,需在页面销毁的时候清除对context实例的引用
     */
    public void clearMemory() {
        this.mContext = null;
        this.mActivity = null;
    }
}
