package com.topjet.common.base.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.base.presenter.BaseFragmentPresenter;
import com.topjet.common.base.presenter.IFragmentPresenter;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.view.dialog.NetRequestDialog;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.SystemUtils;
import com.topjet.common.utils.Toaster;
import com.umeng.analytics.MobclickAgent;

import java.util.HashSet;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by yy on 2017.8.1
 * <p>
 * Fragment MVP模式基类
 */

public abstract class BaseMvpFragment<T extends BaseFragmentPresenter> extends RxFragment implements IView {
    private HashSet<IFragmentPresenter> mPresenters = new HashSet<IFragmentPresenter>();
    public T mPresenter;
    public MvpActivity mActivity;
    public Context mContext;
    private Unbinder unbinder;
    private boolean isFirst = true;
    public String TAG;
    public NetRequestDialog netRequestDialog = null;

    //获取presenter
    protected BaseFragmentPresenter getPresenter() {
        if(mPresenter == null){
            mPresenter = (T) new BaseFragmentPresenter(getContext(),this);
        }
        return mPresenter;

    }

    //初始化presenter
    protected abstract void initPresenter();

    //获取LayoutId
    protected abstract int getLayoutResId();

    protected abstract void initView(View v);

    protected abstract void initData();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
        this.mActivity = (MvpActivity) activity;
        TAG = getClass().toString();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst = true;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initPresenter();
        initRxBus();
        getPresenter().onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPresenter().onActivityCreated(savedInstanceState);

    }


    @Override
    public void onStart() {
        super.onStart();
        getPresenter().onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().onResume();
        Logger.i("Fragment", getPresenter().mFragment.getClass().toString() + " onResume");
        if (isFirst) {
            isFirst = false;
//            Logger.i("oye", "第一次进入Fragment，记录页面");
            MobclickAgent.onPageStart(getPresenter().mFragment.getClass().toString()); //统计页面，fragmentName为页面名称，可自定义
        } else {
            if (!isHidden()) {
                // onResume。按Home键或切换别的App,该Fragment处于show状态，记录页面 开始
                MobclickAgent.onPageStart(getPresenter().mFragment.getClass().toString()); //统计页面，fragmentName为页面名称，可自定义
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getPresenter().onPause();
        Logger.i("Fragment", getPresenter().mFragment.getClass().toString() + " onPause");
        if (!isHidden()) {
//            Logger.i("oye", "onPause了。判断该Fragment 没被隐藏，记录页面 结束");
            MobclickAgent.onPageEnd(getPresenter().mFragment.getClass().toString()); //统计页面，fragmentName为页面名称，可自定义
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getPresenter().onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
        if(null != unbinder) {
            unbinder.unbind();
        }
        recycleLoadingDialog();
        BusManager.getBus().unregister(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            MobclickAgent.onPageEnd(getPresenter().mFragment.getClass().toString());
        } else {
            MobclickAgent.onPageStart(getPresenter().mFragment.getClass().toString());
        }
    }

    // toast提示
    public void showToast(String msg) {
        Toaster.showShortToast(msg);
    }

    // 跳转至下个页面
    public void turnToActivity(Class className) {
        Intent intent = new Intent(mContext, className);
        startActivity(intent);
    }

    // 跳转至下个页面并传参
    public void turnToActivity(Class className, Bundle bundle) {
        Intent intent = new Intent(mContext, className);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // 跳转至下个页面并销毁
    public void turnToActivityWithFinish(Class className) {
        Intent intent = new Intent(mContext, className);
        startActivity(intent);
    }

    // 跳转至下个页面并传参，并销毁
    public void turnToActivityWithFinish(Class className, Bundle bundle) {
        Intent intent = new Intent(mContext, className);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void finishPage() {
    }


    /**
     * 跳转至下个页面并传参
     *
     * @param extra 传递的实体类
     */
    public <E extends BaseExtra> void turnToActivity(Class className, E extra) {
        Bundle bundle = new Bundle();
        if (E.getExtraName() == null) {
            E.setExtraName(SystemUtils.nextExtraName());
        }
        bundle.putSerializable(E.getExtraName(), extra);
        turnToActivity(className, bundle);
    }

    /**
     * 设置返回数据 ，并销毁
     */
    public <E extends BaseExtra> void setResultAndFinish(int resultCode, E extra) {
    }

    /**
     * 跳转至下个页面ForResult
     */
    public void turnToActivityForResult(Class className, int requestCode) {
        Intent intent = new Intent(mContext, className);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转至下个页面并传参ForResult
     */
    public void turnToActivityForResult(Class className, int requestCode, Bundle bundle) {
        Intent intent = new Intent(mContext, className);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转至下个页面并传参ForResult
     *
     * @param extra 需要传递的实体类
     */
    public <E extends BaseExtra> void turnToActivityForResult(Class className, int requestCode, E extra) {
        Bundle bundle = new Bundle();
        if (E.getExtraName() == null) {
            E.setExtraName(SystemUtils.nextExtraName());
        }
        bundle.putSerializable(E.getExtraName(), extra);
        turnToActivityForResult(className, requestCode, bundle);
    }


    private void initRxBus() {
        BusManager.getBus().register(this);
    }

    /**
     * 展示/收起加载框Dialog
     */
    @Override
    public void showLoadingDialog() {
        if (netRequestDialog == null) {
            netRequestDialog = new NetRequestDialog(getActivity());
        }
        if (netRequestDialog.isShowing()) {
            netRequestDialog.show();
        } else {
            netRequestDialog.dismiss();
        }
    }
    /**
     * 关闭加载框
     */
    @Override
    public void cancelShowLoadingDialog(){
        if (netRequestDialog != null) {
            netRequestDialog.dismiss();
        }
    }
    /**
     * 回收Dialog
     */
    private void recycleLoadingDialog() {
        if (netRequestDialog != null) {
            netRequestDialog.dismiss();
            netRequestDialog = null;
        }
    }
}
