package com.topjet.common.base.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.topjet.common.R;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.base.view.fragment.BaseMvpFragment;

import java.util.ArrayList;

public abstract class BaseFragmentActivity<T extends BasePresenter> extends MvpActivity<T> {
    protected FragmentManager mFragmentManager = getSupportFragmentManager();
    protected ArrayList<BaseMvpFragment> mFragments = getFragments();
    protected Fragment mCurrFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFragmentActivity = true;
    }

    protected void setCurrFragment(Fragment targetFragment, String tag) {
        setCurrFragment(getContainerViewId(), targetFragment, tag);
    }

    /**
     * Note that: Fragments in the same type and with same container ID will not work on right,event with different
     * tags,if you has to use same type Fragments which only distinguished by Arguments,you can use ViewPager
     * instead.
     */
    protected void setCurrFragment(int containerViewId, Fragment targetFragment, String tag) {
        if (mCurrFragment == targetFragment) {
            return;
        }
        if (targetFragment.isAdded()) {
            showFragment(targetFragment);
        } else {
            addFragment(containerViewId, targetFragment, tag);
        }
        mCurrFragment = targetFragment;
    }

    /**
     * 添加Fragment
     */
    private void addFragment(int containerViewId, Fragment targetFragment, String tag) {
        FragmentTransaction commonTransaction = getCommonTransaction();
        hideOtherFragments(commonTransaction, targetFragment);
        commonTransaction.add(containerViewId, targetFragment, tag);
        commonTransaction.commitAllowingStateLoss();
    }

    /**
     * 显示传入的Fragment
     *
     * @param targetFragment 需要显示的Fragment
     */
    private void showFragment(Fragment targetFragment) {
        FragmentTransaction commonTransaction = getCommonTransaction();
        hideOtherFragments(commonTransaction, targetFragment);
        commonTransaction.show(targetFragment);
        commonTransaction.commitAllowingStateLoss();
    }

    /**
     * 隐藏其他的Fragment
     */
    private void hideOtherFragments(FragmentTransaction commonTransaction, Fragment targetFragment) {
        if (!ListUtils.isEmpty(mFragments)) {
            for (Fragment fragment : mFragments) {
                if (fragment.isAdded() && fragment != targetFragment) {
                    commonTransaction.hide(fragment);
                }
            }
        }
    }

    /**
     * 获取FragmentTransaction
     */
    @SuppressLint("CommitTransaction")
    protected FragmentTransaction getCommonTransaction() {
//        return mFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim
//                .slide_out_left);
        return mFragmentManager.beginTransaction();
        // 需要切换动画就取消注释
//        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    protected abstract ArrayList<BaseMvpFragment> getFragments();


    protected int getContainerViewId() {
        return R.id.fl_container;
    }

    public Fragment getCurrFragment() {
        return mCurrFragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

}
