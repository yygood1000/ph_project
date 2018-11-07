package com.topjet.common.common.presenter;

import android.content.Context;

import com.topjet.common.base.presenter.BaseFragmentPresenter;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.base.view.fragment.RxFragment;
import com.topjet.common.common.view.fragment.ListFragmentView;

/**
 * creator: zhulunjun
 * time:    2017/9/8
 * describe:
 */

public class ListFragmentPresenter extends BaseFragmentPresenter<ListFragmentView> {

    public ListFragmentPresenter(ListFragmentView mView, Context mContext, RxFragment mFragment) {
        super(mView, mContext, mFragment);
    }
}
